/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.schedule;

import static com.opengamma.collect.Guavate.toImmutableList;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableList;
import com.opengamma.collect.ArgChecker;
import com.opengamma.collect.Guavate;
import com.opengamma.collect.range.LocalDateRange;

/**
 */
public class Schedule implements Iterable<SchedulePeriod> {

  private final List<SchedulePeriod> rows;

  private Schedule(List<SchedulePeriod> rows) {
    this.rows = rows;
  }

  /**
   * Creates a new schedule for a list of periods.
   *
   * @param periods  the periods defining the unadjusted dates in the schedule
   * @return  a schedule for the periods
   */
  public static Schedule ofPeriods(List<LocalDateRange> periods) {
    ArgChecker.notEmpty(periods, "periods");
    List<SchedulePeriod> rows = periods.stream()
                                    .sorted(Comparator.comparing(LocalDateRange::getStart))
                                    .map(SchedulePeriod::new)
                                    .collect(toImmutableList());
    return new Schedule(rows);
  }

  public SchedulePeriod row(int periodNum) {
    return rows.get(periodNum);
  }

  public int rowCount() {
    return rows.size();
  }

  public <T> Schedule withColumn(ScheduleColumn<T> column) {
    if (column.size() != rows.size()) {
      throw new IllegalArgumentException("Wrong number of rows in column " + column.size() + ", expected " + rows.size());
    }
    ImmutableList<SchedulePeriod> updatedRows =
        zip(rows, column.getValues(), (row, value) -> row.withValue(column.getKey(), value)).collect(toImmutableList());
    return new Schedule(updatedRows);
  }

  // TODO do I need this? impl naively by chaining withColumn()?
  /*public Schedule withColumns(ScheduleColumn... columns) {

  }*/

  @Override
  public Iterator<SchedulePeriod> iterator() {
    return rows.iterator();
  }

  // TODO this is probably generally useful, move somewhere?
  private static <T, U, R> Stream<R> zip(Collection<T> c1, Collection<U> c2, BiFunction<T, U, R> fn) {
    return Guavate.stream(() -> new ZipIterator<>(c1, c2, fn));
  }

  private static final class ZipIterator<T, U, R> implements Iterator<R> {

    private final Iterator<T> i1;
    private final Iterator<U> i2;
    private final BiFunction<T, U, R> fn;

    public ZipIterator(Collection<T> c1, Collection<U> c2, BiFunction<T, U, R> fn) {
      ArgChecker.notNull(c1, "c1");
      ArgChecker.notNull(c2, "c2");
      if (c1.size() != c2.size()) {
        throw new IllegalArgumentException("Collections must be the same size");
      }
      this.fn = ArgChecker.notNull(fn, "fn");
      i1 = c1.iterator();
      i2 = c2.iterator();
    }

    @Override
    public boolean hasNext() {
      return i1.hasNext();
    }

    @Override
    public R next() {
      return fn.apply(i1.next(), i2.next());
    }
  }
}


/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.schedule;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.opengamma.collect.ArgChecker;

/**
 * A column in a schedule representing a single piece of data over a range of dates.
 *
 * @param <T>  the type of the data in the column
 */
public class ScheduleColumn<T> {

  private final FieldKey<T> key;
  private final List<T> values;

  public ScheduleColumn(FieldKey<T> key, List<T> values) {
    this.key = ArgChecker.notNull(key, "key");
    this.values = ImmutableList.copyOf(ArgChecker.notNull(values, "values"));
  }

  public FieldKey<T> getKey() {
    return key;
  }

  public List<T> getValues() {
    return values;
  }

  public int size() {
    return values.size();
  }
}

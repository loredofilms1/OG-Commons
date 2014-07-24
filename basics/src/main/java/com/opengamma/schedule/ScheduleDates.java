/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.schedule;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import com.opengamma.basics.date.BusinessDayCalendar;
import com.opengamma.basics.date.BusinessDayConvention;
import com.opengamma.collect.ArgChecker;

/**
 * Helper methods for calculating dates.
 */
public final class ScheduleDates {

  private ScheduleDates() {
  }

  // TODO @Nullable stub or overload? throw an exception if the dates don't line up
  // TODO RollDateAdjuster or its replacement
  public static List<LocalDate> unadjusted(LocalDate startDate, LocalDate endDate, Period period, boolean eomRule, Stub stub) {
    ArgChecker.isTrue(startDate.isBefore(endDate), "Start date " + startDate + " must be before end date " + endDate);

    // by default we start calculating at the start date.
    // but if there's a stub at the start we need to calculate back from the end
    boolean calcFromStart = (stub == Stub.LONG_END || stub == Stub.SHORT_END || stub == Stub.NONE);
    LocalDate calcStartDate = (calcFromStart ? startDate : endDate);

    // the end of month rule applies if the calculation start date is the last day of the month
    boolean applyEomRule = isLastDayOfMonth(calcStartDate) && eomRule;

    if (calcFromStart) {
      if (applyEomRule) {
        return calculateFromStartWithEom(startDate, endDate, period, stub);
      } else {
        return calculateFromStart(startDate, endDate, period, stub);
      }
    } else {
      if (applyEomRule) {
        return calculateFromEndWithEom(startDate, endDate, period, stub);
      } else {
        return calculateFromEnd(startDate, endDate, period, stub);
      }
    }
  }

  private static List<LocalDate> calculateFromStart(LocalDate startDate, LocalDate endDate, Period period, Stub stub) {
    List<LocalDate> endDates = new ArrayList<>();
    LocalDate periodEnd = startDate.plus(period);

    while (periodEnd.isBefore(endDate) || periodEnd.isEqual(endDate)) {
      periodEnd = periodEnd.plus(period);

      if (periodEnd.isAfter(endDate)) {
        if (stub == Stub.SHORT_END) {
          endDates.add(endDate);
        } else if (stub == Stub.LONG_END) {
          // TODO check for empty list - only happens if (endDate - startDate) < period, long end doesn't make sense
          // what's the best thing? be lenient? or throw exception?
          endDates.set(endDates.size() - 1, endDate);
        } else if (stub == Stub.NONE) {
          // TODO throw exception? need a stub if the dates don't line up
        }
      } else {
        endDates.add(periodEnd);
      }
    }
    return endDates;
  }

  private static List<LocalDate> calculateFromStartWithEom(LocalDate startDate, LocalDate endDate, Period period, Stub stub) {
    // TODO implement ScheduleDates.calculateFromStartWithEom()
    throw new UnsupportedOperationException("calculateFromStartWithEom not implemented");
  }

  private static List<LocalDate> calculateFromEnd(LocalDate startDate, LocalDate endDate, Period period, Stub stub) {
    // TODO implement ScheduleDates.calculateFromEnd()
    throw new UnsupportedOperationException("calculateFromEnd not implemented");
  }

  private static List<LocalDate> calculateFromEndWithEom(LocalDate startDate, LocalDate endDate, Period period, Stub stub) {
    // TODO implement ScheduleDates.calculateFromEndWithEom()
    throw new UnsupportedOperationException("calculateFromEndWithEom not implemented");
  }

  private static boolean isLastDayOfMonth(LocalDate date) {
    return date.equals(date.with(TemporalAdjusters.lastDayOfMonth()));
  }

  public static List<LocalDate> adjusted(BusinessDayCalendar calendar,
                                         BusinessDayConvention businessDayConvention,
                                         int offsetDays, // assume business days for now, maybe need flag for calendar days
                                         boolean relativeToPeriodEnd) {
    throw new UnsupportedOperationException();
  }
}

/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.schedule;

import java.time.LocalDate;
import java.util.List;

import com.opengamma.basics.date.BusinessDayCalendar;
import com.opengamma.basics.date.BusinessDayConvention;
import com.opengamma.collect.range.LocalDateRange;

/**
 * Helper methods for calculating dates.
 */
public final class ScheduleDates {

  private ScheduleDates() {
  }

  public static List<LocalDateRange> unadjusted(LocalDate startDate, LocalDate endDate, boolean eomRule, Stub stub) {
    throw new UnsupportedOperationException();
  }

  public static List<LocalDate> adjusted(BusinessDayCalendar calendar,
                                         BusinessDayConvention businessDayConvention,
                                         int offsetDays,
                                         // assume only business days for now, maybe need flag to allow for calendar days
                                         boolean relativeToPeriodEnd) {
    throw new UnsupportedOperationException();
  }
}

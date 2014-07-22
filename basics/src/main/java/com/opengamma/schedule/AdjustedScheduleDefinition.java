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
import com.opengamma.collect.ArgChecker;
import com.opengamma.collect.range.LocalDateRange;

/**
 * Describes how to create a set of adjusted schedule dates from a set of unadjusted periods.
 */
public class AdjustedScheduleDefinition {

  private final BusinessDayCalendar calendar;
  private final BusinessDayConvention businessDayConvention;
  private final int offsetDays; // assume only business days for now, maybe need flag to allow for calendar days
  private final boolean relativeToPeriodEnd;
  // TODO roll convention (or its replacement)

  public AdjustedScheduleDefinition(BusinessDayCalendar calendar,
                                    BusinessDayConvention businessDayConvention,
                                    int offsetDays,
                                    boolean relativeToPeriodEnd) {
    this.calendar = ArgChecker.notNull(calendar, "calendar");
    this.businessDayConvention = ArgChecker.notNull(businessDayConvention, "businessDayConvention");
    this.offsetDays = offsetDays;
    this.relativeToPeriodEnd = relativeToPeriodEnd;
  }

  // TODO sorted set of periods? iterable?
  public List<LocalDate> calculateScheduleDates(List<LocalDateRange> periods) {
    return ScheduleDates.adjusted(calendar, businessDayConvention, offsetDays, relativeToPeriodEnd);
  }
}

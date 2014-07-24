/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.schedule;

import java.time.LocalDate;

import com.opengamma.basics.date.BusinessDayCalendar;
import com.opengamma.basics.date.BusinessDayConvention;
import com.opengamma.collect.ArgChecker;
import com.opengamma.collect.range.LocalDateRange;

/**
 *
 */
public class AccrualDatesGenerator implements ScheduleGenerator {

  private final AdjustedScheduleDefinition scheduleDefinition;

  public AccrualDatesGenerator(AdjustedScheduleDefinition scheduleDefinition) {
    this.scheduleDefinition = ArgChecker.notNull(scheduleDefinition, "scheduleDefinition");
  }

  @Override
  public Schedule generate(Schedule schedule) {
    BusinessDayConvention businessDayConvention = scheduleDefinition.getBusinessDayConvention();
    BusinessDayCalendar calendar = scheduleDefinition.getCalendar().get();

    return schedule.map(period -> {
      LocalDateRange periodDateRange = period.getDateRange();
      LocalDate accrualStartDate = businessDayConvention.adjust(periodDateRange.getStart(), calendar);
      LocalDate accrualEndDate = businessDayConvention.adjust(periodDateRange.getEndInclusive(), calendar);
      return period.withValues(FieldKeys.ACCRUAL_START_DATE, accrualStartDate,
                               FieldKeys.ACCRUAL_END_DATE, accrualEndDate);
    });
  }
}

/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.schedule;

import java.time.LocalDate;

import com.opengamma.basics.date.BusinessDayCalendar;
import com.opengamma.basics.date.BusinessDayConvention;
import com.opengamma.collect.range.LocalDateRange;

/**
 *
 */
public class AccrualDatesGenerator implements ScheduleGenerator {

  @Override
  public Schedule generate(Schedule schedule) {
    return schedule.map(period -> {
      AdjustedScheduleDefinition scheduleDefinition = period.get(Fields.ACCRUAL_SCHEDULE);
      BusinessDayConvention businessDayConvention = scheduleDefinition.getBusinessDayConvention();
      BusinessDayCalendar calendar = scheduleDefinition.getCalendar().get();
      LocalDateRange periodDateRange = period.getDateRange();
      LocalDate accrualStartDate = businessDayConvention.adjust(periodDateRange.getStart(), calendar);
      LocalDate accrualEndDate = businessDayConvention.adjust(periodDateRange.getEndInclusive(), calendar);
      return period.withValues(Fields.ACCRUAL_START_DATE, accrualStartDate,
                               Fields.ACCRUAL_END_DATE, accrualEndDate);
    });
  }
}

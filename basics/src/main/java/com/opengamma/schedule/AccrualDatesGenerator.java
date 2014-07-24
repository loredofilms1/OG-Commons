/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.schedule;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.opengamma.basics.date.BusinessDayCalendar;
import com.opengamma.basics.date.BusinessDayConvention;
import com.opengamma.collect.range.LocalDateRange;

/**
 *
 */
public class AccrualDatesGenerator implements ScheduleGenerator {

  private final BusinessDayCalendar calendar;
  private final BusinessDayConvention businessDayConvention;

  public AccrualDatesGenerator(BusinessDayCalendar calendar, BusinessDayConvention businessDayConvention) {
    this.calendar = calendar;
    this.businessDayConvention = businessDayConvention;
  }

  @Override
  public Schedule generate(Schedule schedule) {
    // there are 2 obvious ways to do this
    //   derive the adjusted dates from the unadjusted dates in one go and add a column
    //   derive the adjusted dates from the unadjusted dates one at a time and create new periods

    // TODO there needs to be a nicer way to build up a modified schedule than this. ScheduleBuilder?
    List<SchedulePeriod> periods = new ArrayList<>();

    // need to go from the unadjusted schedule of end dates to 2 columns, accrual start and end
    for (SchedulePeriod period : schedule) {
      LocalDateRange periodDateRange = period.getDateRange();
      // adjust periodEndDate to get accrual end date
      // what's the start date? end of previous? end + 1?
      // need calendar and bus day conv
      LocalDate accrualStartDate = businessDayConvention.adjust(periodDateRange.getStart(), calendar);
      LocalDate accrualEndDate = businessDayConvention.adjust(periodDateRange.getEndInclusive(), calendar);
      periods.add(period.withValues(FieldKeys.ACCRUAL_START_DATE, accrualStartDate,
                                    FieldKeys.ACCRUAL_END_DATE, accrualEndDate));
    }
    return new Schedule(schedule.getStartDate(), periods);
  }
}

/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.schedule;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.opengamma.basics.date.BusinessDayCalendar;
import com.opengamma.basics.date.BusinessDayConvention;
import com.opengamma.collect.ArgChecker;

/**
 *
 */
public class PaymentDatesGenerator implements ScheduleGenerator {

  private final AdjustedScheduleDefinition scheduleDefinition;

  public PaymentDatesGenerator(AdjustedScheduleDefinition scheduleDefinition) {
    this.scheduleDefinition = ArgChecker.notNull(scheduleDefinition, "scheduleDefinition");
  }

  @Override
  public Schedule generate(Schedule schedule) {
    boolean relativeToEnd = scheduleDefinition.isRelativeToPeriodEnd();
    BusinessDayConvention businessDayConvention = scheduleDefinition.getBusinessDayConvention();
    BusinessDayCalendar calendar = scheduleDefinition.getCalendar().get();
    int offset = scheduleDefinition.getOffsetDays();

    FieldKey<LocalDate> accrualDateKey = (relativeToEnd ? FieldKeys.ACCRUAL_END_DATE : FieldKeys.ACCRUAL_START_DATE);
    // TODO ScheduleBuilder
    List<SchedulePeriod> periods = new ArrayList<>();

    for (SchedulePeriod period : schedule) {
      Optional<LocalDate> optionalAccrualDate = period.getFieldMap().get(accrualDateKey);
      // TODO what's the best thing to do if a required field is missing? exception?
      LocalDate accrualDate = optionalAccrualDate.get();

      LocalDate paymentDate = businessDayConvention.adjust(accrualDate, calendar).with(calendar.adjustBy(offset));
      periods.add(period.withValues(FieldKeys.PAYMENT_DATE, paymentDate));
    }
    return new Schedule(schedule.getStartDate(), periods);
  }
}

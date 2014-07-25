/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.schedule;

import java.time.LocalDate;

import com.opengamma.basics.date.BusinessDayCalendar;
import com.opengamma.basics.date.BusinessDayConvention;

/**
 *
 */
public class PaymentDatesGenerator implements ScheduleGenerator {

  @Override
  public Schedule generate(Schedule schedule) {
    return schedule.map(period -> {
      AdjustedScheduleDefinition scheduleDefinition = period.get(Fields.PAYMENT_SCHEDULE);
      boolean relativeToEnd = scheduleDefinition.isRelativeToPeriodEnd();
      FieldKey<LocalDate> accrualDateKey = (relativeToEnd ? Fields.ACCRUAL_END_DATE : Fields.ACCRUAL_START_DATE);
      LocalDate accrualDate = period.get(accrualDateKey);
      BusinessDayConvention businessDayConvention = scheduleDefinition.getBusinessDayConvention();
      BusinessDayCalendar calendar = scheduleDefinition.getCalendar().get();
      int offset = scheduleDefinition.getOffsetDays();

      LocalDate paymentDate = businessDayConvention.adjust(accrualDate, calendar).with(calendar.adjustBy(offset));
      return period.withValues(Fields.PAYMENT_DATE, paymentDate);
    });
  }
}

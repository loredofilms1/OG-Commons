/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.schedule;

import java.time.LocalDate;

import com.opengamma.basics.currency.Currency;
import com.opengamma.basics.currency.CurrencyAmount;
import com.opengamma.basics.date.DayCount;

/**
 *
 */
public class CouponGenerator implements ScheduleGenerator {

  @Override
  public Schedule generate(Schedule schedule) {

    return schedule.map(period -> {
      Boolean payer = period.get(Fields.PAYER);
      LocalDate accrualStart = period.get(Fields.ACCRUAL_START_DATE);
      LocalDate accrualEnd = period.get(Fields.ACCRUAL_END_DATE);
      DayCount dayCount = period.get(Fields.DAY_COUNT);
      double rate = period.get(Fields.RATE);
      double notional = period.get(Fields.NOTIONAL);
      Currency currency = period.get(Fields.CURRENCY);

      double sign = (payer ? -1 : 1);
      double dayCountFraction = dayCount.getDayCountFraction(accrualStart, accrualEnd);
      double amount = rate * notional * dayCountFraction * sign;
      return period.withValues(Fields.COUPON_AMOUNT, CurrencyAmount.of(currency, amount));
    });
  }
}

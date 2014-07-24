/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.schedule;

import java.time.LocalDate;

import com.opengamma.basics.currency.Currency;
import com.opengamma.basics.date.DayCount;
import com.opengamma.collect.ArgChecker;

/**
 *
 */
public class CouponGenerator implements ScheduleGenerator {

  private final Currency currency;
  private final DayCount dayCount;
  private final double notional;
  private final double rate;
  private final boolean pay;

  public CouponGenerator(Currency currency, DayCount dayCount, double notional, double rate, boolean pay) {
    this.currency = ArgChecker.notNull(currency, "currency");
    this.dayCount = ArgChecker.notNull(dayCount, "dayCount");
    this.notional = notional;
    this.rate = rate;
    this.pay = pay;
  }

  @Override
  public Schedule generate(Schedule schedule) {
    double sign = (pay ? -1 : 1);

    return schedule.map(period -> {
      LocalDate accrualStart = period.getFieldMap().get(FieldKeys.ACCRUAL_START_DATE).get();
      LocalDate accrualEnd = period.getFieldMap().get(FieldKeys.ACCRUAL_END_DATE).get();
      double dayCountFraction = dayCount.getDayCountFraction(accrualStart, accrualEnd);
      double amount = rate * notional * dayCountFraction * sign;
      return period.withValues(FieldKeys.COUPON_AMOUNT, amount);
    });
  }
}

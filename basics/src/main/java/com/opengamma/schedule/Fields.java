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
 * TODO extensible enum
 */
public final class Fields {

  public static final FieldKey<LocalDate> ACCRUAL_START_DATE = new FieldKey<>("Accrual Start Date", LocalDate.class);
  public static final FieldKey<LocalDate> ACCRUAL_END_DATE = new FieldKey<>("Accrual End Date", LocalDate.class);
  public static final FieldKey<LocalDate> PAYMENT_DATE = new FieldKey<>("Payment Date", LocalDate.class);
  public static final FieldKey<CurrencyAmount> COUPON_AMOUNT = new FieldKey<>("Coupon Amount", CurrencyAmount.class);
  public static final FieldKey<Currency> CURRENCY = new FieldKey<>("Currency", Currency.class);
  public static final FieldKey<DayCount> DAY_COUNT = new FieldKey<>("Day Count", DayCount.class);
  public static final FieldKey<Double> NOTIONAL = new FieldKey<>("Notional", Double.class);
  public static final FieldKey<Double> RATE = new FieldKey<>("Rate", Double.class);
  public static final FieldKey<Boolean> PAYER = new FieldKey<>("Payer", Boolean.class);
  public static final FieldKey<AdjustedScheduleDefinition> ACCRUAL_SCHEDULE = new FieldKey<>("Accrual Schedule", AdjustedScheduleDefinition.class);
  public static final FieldKey<AdjustedScheduleDefinition> PAYMENT_SCHEDULE = new FieldKey<>("Payment Schedule", AdjustedScheduleDefinition.class);

  private Fields() {
  }
}

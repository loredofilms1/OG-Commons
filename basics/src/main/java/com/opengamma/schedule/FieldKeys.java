/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.schedule;

import java.time.LocalDate;

/**
 * TODO extensible enum
 */
public final class FieldKeys {

  public static final FieldKey<LocalDate> ACCRUAL_START_DATE = new FieldKey<>("Accrual Start Date", LocalDate.class);
  public static final FieldKey<LocalDate> ACCRUAL_END_DATE = new FieldKey<>("Accrual End Date", LocalDate.class);
  public static final FieldKey<LocalDate> PAYMENT_DATE = new FieldKey<>("Payment Date", LocalDate.class);
  public static final FieldKey<Double> COUPON_AMOUNT = new FieldKey<>("Coupon Amount", Double.class);

  private FieldKeys() {
  }
}

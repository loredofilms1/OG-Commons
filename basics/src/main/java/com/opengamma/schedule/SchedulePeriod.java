/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.schedule;

import com.opengamma.collect.range.LocalDateRange;

/**
 *
 */
public class SchedulePeriod {

  // TODO does this need to be a field or could it just be a value (or 2) in the field map?
  private final LocalDateRange dateRange;

  private final FieldMap fieldMap;

  public SchedulePeriod(LocalDateRange dateRange) {
    this.dateRange = dateRange;
    fieldMap = new FieldMap();
  }

  private SchedulePeriod(LocalDateRange dateRange, FieldMap fieldMap) {
    this.dateRange = dateRange;
    this.fieldMap = fieldMap;
  }

  public <T> SchedulePeriod withValue(FieldKey<T> key, T value) {
    return new SchedulePeriod(dateRange, fieldMap.withValue(key, value));
  }
}

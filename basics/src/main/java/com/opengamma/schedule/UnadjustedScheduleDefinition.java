/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.schedule;

import java.time.LocalDate;
import java.util.List;

import com.opengamma.collect.ArgChecker;
import com.opengamma.collect.range.LocalDateRange;

/**
 * TODO bean?
 * TODO this could be a static method
 */
public class UnadjustedScheduleDefinition {

  // TODO date range?
  private final LocalDate startDate;
  private final LocalDate endDate;
  private final boolean eomRule;
  private final Stub stub;

  public UnadjustedScheduleDefinition(LocalDate startDate, LocalDate endDate, boolean eomRule, Stub stub) {
    this.startDate = ArgChecker.notNull(startDate, "startDate");
    this.endDate = ArgChecker.notNull(endDate, "endDate");
    this.eomRule = eomRule;
    this.stub = ArgChecker.notNull(stub, "stub");
    // TODO check startDate < endDate
  }

  // TODO return a sorted set?
  public List<LocalDateRange> calculateSchedulePeriods() {
    return ScheduleDates.unadjusted(startDate, endDate, eomRule, stub);
  }
}

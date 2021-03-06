/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.basics.date;

import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.DayOfWeek.THURSDAY;

import java.time.DayOfWeek;
import java.time.LocalDate;

import com.opengamma.collect.ArgChecker;
import com.opengamma.collect.range.LocalDateRange;

/**
 * Standard holiday calendar implementations.
 * <p>
 * See {@link HolidayCalendars} for the description of each.
 */
enum StandardHolidayCalendars implements HolidayCalendar {

  // no holidays
  NO_HOLIDAYS("NoHolidays") {
    @Override
    public boolean isHoliday(LocalDate date) {
      ArgChecker.notNull(date, "date");
      return false;
    }
    @Override
    public boolean isBusinessDay(LocalDate date) {
      ArgChecker.notNull(date, "date");
      return true;
    }
    @Override
    public LocalDate shift(LocalDate date, int amount) {
      ArgChecker.notNull(date, "date");
      return date.plusDays(amount);
    }
    @Override
    public LocalDate next(LocalDate date) {
      ArgChecker.notNull(date, "date");
      return date.plusDays(1);
    }
    @Override
    public LocalDate previous(LocalDate date) {
      ArgChecker.notNull(date, "date");
      return date.minusDays(1);
    }
    @Override
    public int daysBetween(LocalDate startInclusive, LocalDate endExclusive) {
      return Math.toIntExact(endExclusive.toEpochDay() - startInclusive.toEpochDay());
    }
    @Override
    public int daysBetween(LocalDateRange dateRange) {
      return daysBetween(dateRange.getStart(), dateRange.getEndExclusive());
    }
    @Override
    public HolidayCalendar combineWith(HolidayCalendar other) {
      return ArgChecker.notNull(other, "other");
    }
  },

  // Saturday and Sunday only
  SAT_SUN("Sat/Sun") {
    @Override
    public boolean isHoliday(LocalDate date) {
      ArgChecker.notNull(date, "date");
      DayOfWeek dow = date.getDayOfWeek();
      return dow == SATURDAY || dow == SUNDAY;
    }
  },

  // Friday and Saturday only
  FRI_SAT("Fri/Sat") {
    @Override
    public boolean isHoliday(LocalDate date) {
      ArgChecker.notNull(date, "date");
      DayOfWeek dow = date.getDayOfWeek();
      return dow == FRIDAY || dow == SATURDAY;
    }
  },

  // Thursday and Friday only
  THU_FRI("Thu/Fri") {
    @Override
    public boolean isHoliday(LocalDate date) {
      ArgChecker.notNull(date, "date");
      DayOfWeek dow = date.getDayOfWeek();
      return dow == THURSDAY || dow == FRIDAY;
    }
  };

  // name
  private final String name;

  // create
  private StandardHolidayCalendars(String name) {
    this.name = name;
  }

  @Override
  public LocalDate shift(LocalDate date, int amount) {
    // optimize because we know there are 5 business days in a week
    // method implemented here as cannot reach default method from enum subclass
    ArgChecker.notNull(date, "date");
    LocalDate weekAdjusted = date.plusWeeks(amount / 5);
    return HolidayCalendar.super.shift(weekAdjusted, amount % 5);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return name;
  }

}

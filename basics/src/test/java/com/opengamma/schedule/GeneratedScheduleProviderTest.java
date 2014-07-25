package com.opengamma.schedule;

import java.time.LocalDate;
import java.time.Period;

import org.testng.annotations.Test;

import com.google.common.collect.ImmutableList;
import com.opengamma.basics.currency.Currency;
import com.opengamma.basics.date.BusinessDayCalendar;
import com.opengamma.basics.date.BusinessDayConvention;
import com.opengamma.basics.date.DayCount;

@Test
public class GeneratedScheduleProviderTest {

  @Test
  public void vanillaSwapFixedLegSchedule() {
    // assumptions
    //   non-zero period
    //   no notional exchange
    //   no stubs (start, end and period line up properly)
    //   start date is a business day (check if this is generally true, seems sensible)

    // TODO real USD calendar
    BusinessDayCalendar calendar = BusinessDayCalendar.WEEKENDS;
    BusinessDayConvention businessDayConvention = BusinessDayConvention.MODIFIED_FOLLOWING;
    LocalDate startDate = LocalDate.of(2014, 9, 12);
    LocalDate endDate = LocalDate.of(2021, 9, 12);
    UnadjustedScheduleDefinition unadjustedScheduleDefinition =
        new UnadjustedScheduleDefinition(startDate, endDate, Period.ofMonths(6), false, Stub.NONE);

    AdjustedScheduleDefinition accrualScheduleDefinition =
        new AdjustedScheduleDefinition(() -> calendar, businessDayConvention, 0, false);

    int paymentOffset = 2;
    AdjustedScheduleDefinition paymentSchedule =
        new AdjustedScheduleDefinition(() -> calendar, businessDayConvention, paymentOffset, false);

    // TODO choose the generators based on the schedules and other data in the model
    // how do I know which generators are required for a full set and how they depend on each other?
    // do they come as a predefined set? where does the logic live to choose a set?
    // or is there a set of types - accrual dates, payment dates, coupons etc?
    // and generators are categorised and the categories are tried in turn
    ImmutableList<ScheduleGenerator> generators = ImmutableList.of(new AccrualDatesGenerator(),
                                                                   new PaymentDatesGenerator(),
                                                                   new CouponGenerator());

    FieldMap tradeFields = new FieldBuilder().add(Fields.ACCRUAL_SCHEDULE, accrualScheduleDefinition)
                                             .add(Fields.PAYMENT_SCHEDULE, paymentSchedule)
                                             .add(Fields.CURRENCY, Currency.USD)
                                             .add(Fields.NOTIONAL, 100_000_000d)
                                             .add(Fields.RATE, 0.015)
                                             .add(Fields.PAYER, true)
                                             .add(Fields.DAY_COUNT, DayCount.DC_30U_360)
                                             .build();

    GeneratedScheduleProvider scheduleProvider =
        new GeneratedScheduleProvider(unadjustedScheduleDefinition, generators, tradeFields);

    Schedule schedule = scheduleProvider.getSchedule();
    System.out.println(schedule);
  }
}

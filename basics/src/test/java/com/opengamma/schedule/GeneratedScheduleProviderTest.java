package com.opengamma.schedule;

import java.time.LocalDate;
import java.time.Period;

import org.testng.annotations.Test;

import com.opengamma.basics.date.BusinessDayCalendar;
import com.opengamma.basics.date.BusinessDayConvention;

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
    UnadjustedScheduleDefinition unadjustedSchedule =
        new UnadjustedScheduleDefinition(startDate, endDate, Period.ofMonths(6), false, Stub.NONE);

    AdjustedScheduleDefinition accrualSchedule =
        new AdjustedScheduleDefinition(() -> calendar, businessDayConvention, 0, false);
    AccrualDatesGenerator accrualDatesGenerator =
        new AccrualDatesGenerator(accrualSchedule);
    Schedule schedule = Schedule.of(startDate, unadjustedSchedule.calculatePeriods());
    Schedule scheduleWithAccrualDates = accrualDatesGenerator.generate(schedule);

    int paymentOffset = 2;
    AdjustedScheduleDefinition paymentSchedule =
        new AdjustedScheduleDefinition(() -> calendar, businessDayConvention, paymentOffset, false);
    PaymentDatesGenerator paymentDatesGenerator =
        new PaymentDatesGenerator(paymentSchedule);
     Schedule scheduleWithPaymentDates = paymentDatesGenerator.generate(scheduleWithAccrualDates);

    System.out.println(scheduleWithPaymentDates);
  }
}

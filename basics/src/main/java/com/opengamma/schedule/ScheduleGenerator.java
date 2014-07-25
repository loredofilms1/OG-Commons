/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.schedule;

/**
 * Interface for classes that can derive a schedule from an existing schedule.
 */
public interface ScheduleGenerator {

  /**
   * Generates a new schedule from an existing schedule.
   * TODO should this return Optional if the generator can't be applied to the schedule?
   * or should there be another method like canApplyTo()?
   *
   * @param schedule  a schedule
   * @return  a new schedule derived from the existing schedule
   */
  Schedule generate(Schedule schedule);
}

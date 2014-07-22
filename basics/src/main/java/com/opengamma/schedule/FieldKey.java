/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.schedule;

/**
 * TODO this needs to be an extensible enum
 */
public class FieldKey<T> {

  private final String name;
  private final Class<T> type;

  public FieldKey(String name, Class<T> type) {
    this.name = name;
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public Class<T> getType() {
    return type;
  }
}

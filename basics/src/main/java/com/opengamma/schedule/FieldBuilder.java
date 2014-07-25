/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.schedule;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class FieldBuilder {

  private final Map<FieldKey<?>, Object> map = new HashMap<>();

  public <T> FieldBuilder add(FieldKey<T> key, T value) {
    map.put(key, value);
    return this;
  }

  public FieldMap build() {
    return new FieldMap(map);
  }
}

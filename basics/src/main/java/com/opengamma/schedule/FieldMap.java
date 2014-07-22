/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.schedule;

import java.util.Map;
import java.util.Optional;

import com.google.common.collect.ImmutableMap;
import com.opengamma.collect.ArgChecker;

/**
 *
 */
public class FieldMap {

  private final Map<FieldKey<?>, Object> map;

  public FieldMap() {
    this(ImmutableMap.of());
  }

  private FieldMap(Map<FieldKey<?>, Object> map) {
    // TODO check the types of the keys and values match
    this.map = ImmutableMap.copyOf(ArgChecker.notNull(map, "map"));
  }

  @SuppressWarnings("unchecked")
  public <T> Optional<T> get(FieldKey<T> key) {
    ArgChecker.notNull(key, "key");
    T value = (T) map.get(key);
    return Optional.ofNullable(value);
  }

  public <T> FieldMap withValue(FieldKey<T> key, T value) {
    ArgChecker.notNull(key, "key");
    ArgChecker.notNull(value, "value");
    return new FieldMap(ImmutableMap.<FieldKey<?>, Object>builder().putAll(map).put(key, value).build());
  }
}

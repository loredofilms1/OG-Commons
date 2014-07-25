/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.schedule;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.joda.beans.Bean;
import org.joda.beans.BeanDefinition;
import org.joda.beans.ImmutableBean;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectFieldsBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.collect.ArgChecker;
import com.opengamma.collect.range.LocalDateRange;

/**
 * TODO this could be a static method
 */
@BeanDefinition
public class UnadjustedScheduleDefinition implements ImmutableBean {

  // TODO date range?
  @PropertyDefinition(validate = "notNull")
  private final LocalDate startDate;

  @PropertyDefinition(validate = "notNull")
  private final LocalDate endDate;

  @PropertyDefinition
  private final boolean eomRule;

  @PropertyDefinition(validate = "notNull")
  private final Stub stub;

  @PropertyDefinition(validate = "notNull")
  private final Period period;

  public UnadjustedScheduleDefinition(LocalDate startDate, LocalDate endDate, Period period, boolean eomRule, Stub stub) {
    this.period = period;
    this.startDate = ArgChecker.notNull(startDate, "startDate");
    this.endDate = ArgChecker.notNull(endDate, "endDate");
    this.eomRule = eomRule;
    this.stub = ArgChecker.notNull(stub, "stub");
    // TODO check startDate < endDate, period isn't negative
  }

  public List<SchedulePeriod> calculatePeriods(FieldMap defaultFields) {
    ArgChecker.notNull(defaultFields, "defaultFields");
    List<LocalDate> endDates = ScheduleDates.unadjusted(startDate, endDate, period, eomRule, stub);
    List<SchedulePeriod> periods = new ArrayList<>(endDates.size());
    LocalDate start = startDate;

    // TODO this would be much nicer with a partition method, size 2, step 1
    for (LocalDate end : endDates) {
      LocalDateRange range = LocalDateRange.closed(start, end);
      periods.add(new SchedulePeriod(range, defaultFields));
      start = end;
    }
    return periods;
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code UnadjustedScheduleDefinition}.
   * @return the meta-bean, not null
   */
  public static UnadjustedScheduleDefinition.Meta meta() {
    return UnadjustedScheduleDefinition.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(UnadjustedScheduleDefinition.Meta.INSTANCE);
  }

  /**
   * Returns a builder used to create an instance of the bean.
   * @return the builder, not null
   */
  public static UnadjustedScheduleDefinition.Builder builder() {
    return new UnadjustedScheduleDefinition.Builder();
  }

  /**
   * Restricted constructor.
   * @param builder  the builder to copy from, not null
   */
  protected UnadjustedScheduleDefinition(UnadjustedScheduleDefinition.Builder builder) {
    JodaBeanUtils.notNull(builder.startDate, "startDate");
    JodaBeanUtils.notNull(builder.endDate, "endDate");
    JodaBeanUtils.notNull(builder.stub, "stub");
    JodaBeanUtils.notNull(builder.period, "period");
    this.startDate = builder.startDate;
    this.endDate = builder.endDate;
    this.eomRule = builder.eomRule;
    this.stub = builder.stub;
    this.period = builder.period;
  }

  @Override
  public UnadjustedScheduleDefinition.Meta metaBean() {
    return UnadjustedScheduleDefinition.Meta.INSTANCE;
  }

  @Override
  public <R> Property<R> property(String propertyName) {
    return metaBean().<R>metaProperty(propertyName).createProperty(this);
  }

  @Override
  public Set<String> propertyNames() {
    return metaBean().metaPropertyMap().keySet();
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the startDate.
   * @return the value of the property, not null
   */
  public LocalDate getStartDate() {
    return startDate;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the endDate.
   * @return the value of the property, not null
   */
  public LocalDate getEndDate() {
    return endDate;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the eomRule.
   * @return the value of the property
   */
  public boolean isEomRule() {
    return eomRule;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the stub.
   * @return the value of the property, not null
   */
  public Stub getStub() {
    return stub;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the period.
   * @return the value of the property, not null
   */
  public Period getPeriod() {
    return period;
  }

  //-----------------------------------------------------------------------
  /**
   * Returns a builder that allows this bean to be mutated.
   * @return the mutable builder, not null
   */
  public Builder toBuilder() {
    return new Builder(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      UnadjustedScheduleDefinition other = (UnadjustedScheduleDefinition) obj;
      return JodaBeanUtils.equal(getStartDate(), other.getStartDate()) &&
          JodaBeanUtils.equal(getEndDate(), other.getEndDate()) &&
          (isEomRule() == other.isEomRule()) &&
          JodaBeanUtils.equal(getStub(), other.getStub()) &&
          JodaBeanUtils.equal(getPeriod(), other.getPeriod());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash += hash * 31 + JodaBeanUtils.hashCode(getStartDate());
    hash += hash * 31 + JodaBeanUtils.hashCode(getEndDate());
    hash += hash * 31 + JodaBeanUtils.hashCode(isEomRule());
    hash += hash * 31 + JodaBeanUtils.hashCode(getStub());
    hash += hash * 31 + JodaBeanUtils.hashCode(getPeriod());
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(192);
    buf.append("UnadjustedScheduleDefinition{");
    int len = buf.length();
    toString(buf);
    if (buf.length() > len) {
      buf.setLength(buf.length() - 2);
    }
    buf.append('}');
    return buf.toString();
  }

  protected void toString(StringBuilder buf) {
    buf.append("startDate").append('=').append(JodaBeanUtils.toString(getStartDate())).append(',').append(' ');
    buf.append("endDate").append('=').append(JodaBeanUtils.toString(getEndDate())).append(',').append(' ');
    buf.append("eomRule").append('=').append(JodaBeanUtils.toString(isEomRule())).append(',').append(' ');
    buf.append("stub").append('=').append(JodaBeanUtils.toString(getStub())).append(',').append(' ');
    buf.append("period").append('=').append(JodaBeanUtils.toString(getPeriod())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code UnadjustedScheduleDefinition}.
   */
  public static class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code startDate} property.
     */
    private final MetaProperty<LocalDate> startDate = DirectMetaProperty.ofImmutable(
        this, "startDate", UnadjustedScheduleDefinition.class, LocalDate.class);
    /**
     * The meta-property for the {@code endDate} property.
     */
    private final MetaProperty<LocalDate> endDate = DirectMetaProperty.ofImmutable(
        this, "endDate", UnadjustedScheduleDefinition.class, LocalDate.class);
    /**
     * The meta-property for the {@code eomRule} property.
     */
    private final MetaProperty<Boolean> eomRule = DirectMetaProperty.ofImmutable(
        this, "eomRule", UnadjustedScheduleDefinition.class, Boolean.TYPE);
    /**
     * The meta-property for the {@code stub} property.
     */
    private final MetaProperty<Stub> stub = DirectMetaProperty.ofImmutable(
        this, "stub", UnadjustedScheduleDefinition.class, Stub.class);
    /**
     * The meta-property for the {@code period} property.
     */
    private final MetaProperty<Period> period = DirectMetaProperty.ofImmutable(
        this, "period", UnadjustedScheduleDefinition.class, Period.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "startDate",
        "endDate",
        "eomRule",
        "stub",
        "period");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -2129778896:  // startDate
          return startDate;
        case -1607727319:  // endDate
          return endDate;
        case -1570350433:  // eomRule
          return eomRule;
        case 3541166:  // stub
          return stub;
        case -991726143:  // period
          return period;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public UnadjustedScheduleDefinition.Builder builder() {
      return new UnadjustedScheduleDefinition.Builder();
    }

    @Override
    public Class<? extends UnadjustedScheduleDefinition> beanType() {
      return UnadjustedScheduleDefinition.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code startDate} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<LocalDate> startDate() {
      return startDate;
    }

    /**
     * The meta-property for the {@code endDate} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<LocalDate> endDate() {
      return endDate;
    }

    /**
     * The meta-property for the {@code eomRule} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Boolean> eomRule() {
      return eomRule;
    }

    /**
     * The meta-property for the {@code stub} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Stub> stub() {
      return stub;
    }

    /**
     * The meta-property for the {@code period} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Period> period() {
      return period;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -2129778896:  // startDate
          return ((UnadjustedScheduleDefinition) bean).getStartDate();
        case -1607727319:  // endDate
          return ((UnadjustedScheduleDefinition) bean).getEndDate();
        case -1570350433:  // eomRule
          return ((UnadjustedScheduleDefinition) bean).isEomRule();
        case 3541166:  // stub
          return ((UnadjustedScheduleDefinition) bean).getStub();
        case -991726143:  // period
          return ((UnadjustedScheduleDefinition) bean).getPeriod();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      metaProperty(propertyName);
      if (quiet) {
        return;
      }
      throw new UnsupportedOperationException("Property cannot be written: " + propertyName);
    }

  }

  //-----------------------------------------------------------------------
  /**
   * The bean-builder for {@code UnadjustedScheduleDefinition}.
   */
  public static class Builder extends DirectFieldsBeanBuilder<UnadjustedScheduleDefinition> {

    private LocalDate startDate;
    private LocalDate endDate;
    private boolean eomRule;
    private Stub stub;
    private Period period;

    /**
     * Restricted constructor.
     */
    protected Builder() {
    }

    /**
     * Restricted copy constructor.
     * @param beanToCopy  the bean to copy from, not null
     */
    protected Builder(UnadjustedScheduleDefinition beanToCopy) {
      this.startDate = beanToCopy.getStartDate();
      this.endDate = beanToCopy.getEndDate();
      this.eomRule = beanToCopy.isEomRule();
      this.stub = beanToCopy.getStub();
      this.period = beanToCopy.getPeriod();
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case -2129778896:  // startDate
          return startDate;
        case -1607727319:  // endDate
          return endDate;
        case -1570350433:  // eomRule
          return eomRule;
        case 3541166:  // stub
          return stub;
        case -991726143:  // period
          return period;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case -2129778896:  // startDate
          this.startDate = (LocalDate) newValue;
          break;
        case -1607727319:  // endDate
          this.endDate = (LocalDate) newValue;
          break;
        case -1570350433:  // eomRule
          this.eomRule = (Boolean) newValue;
          break;
        case 3541166:  // stub
          this.stub = (Stub) newValue;
          break;
        case -991726143:  // period
          this.period = (Period) newValue;
          break;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
      return this;
    }

    @Override
    public Builder set(MetaProperty<?> property, Object value) {
      super.set(property, value);
      return this;
    }

    @Override
    public Builder setString(String propertyName, String value) {
      setString(meta().metaProperty(propertyName), value);
      return this;
    }

    @Override
    public Builder setString(MetaProperty<?> property, String value) {
      super.setString(property, value);
      return this;
    }

    @Override
    public Builder setAll(Map<String, ? extends Object> propertyValueMap) {
      super.setAll(propertyValueMap);
      return this;
    }

    @Override
    public UnadjustedScheduleDefinition build() {
      return new UnadjustedScheduleDefinition(this);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the {@code startDate} property in the builder.
     * @param startDate  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder startDate(LocalDate startDate) {
      JodaBeanUtils.notNull(startDate, "startDate");
      this.startDate = startDate;
      return this;
    }

    /**
     * Sets the {@code endDate} property in the builder.
     * @param endDate  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder endDate(LocalDate endDate) {
      JodaBeanUtils.notNull(endDate, "endDate");
      this.endDate = endDate;
      return this;
    }

    /**
     * Sets the {@code eomRule} property in the builder.
     * @param eomRule  the new value
     * @return this, for chaining, not null
     */
    public Builder eomRule(boolean eomRule) {
      this.eomRule = eomRule;
      return this;
    }

    /**
     * Sets the {@code stub} property in the builder.
     * @param stub  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder stub(Stub stub) {
      JodaBeanUtils.notNull(stub, "stub");
      this.stub = stub;
      return this;
    }

    /**
     * Sets the {@code period} property in the builder.
     * @param period  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder period(Period period) {
      JodaBeanUtils.notNull(period, "period");
      this.period = period;
      return this;
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(192);
      buf.append("UnadjustedScheduleDefinition.Builder{");
      int len = buf.length();
      toString(buf);
      if (buf.length() > len) {
        buf.setLength(buf.length() - 2);
      }
      buf.append('}');
      return buf.toString();
    }

    protected void toString(StringBuilder buf) {
      buf.append("startDate").append('=').append(JodaBeanUtils.toString(startDate)).append(',').append(' ');
      buf.append("endDate").append('=').append(JodaBeanUtils.toString(endDate)).append(',').append(' ');
      buf.append("eomRule").append('=').append(JodaBeanUtils.toString(eomRule)).append(',').append(' ');
      buf.append("stub").append('=').append(JodaBeanUtils.toString(stub)).append(',').append(' ');
      buf.append("period").append('=').append(JodaBeanUtils.toString(period)).append(',').append(' ');
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}

/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.collect.result;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.ImmutableBean;
import org.joda.beans.ImmutableValidator;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectFieldsBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.opengamma.collect.ArgChecker;
import com.opengamma.collect.Guavate;
import com.opengamma.collect.Messages;

/**
 * An immutable calculation result.
 * <p>
 * There are two types of result - success and failure.
 * A success result contains the calculated non-null result value.
 * A failure result contains details of the {@linkplain Failure failure} that occurred.
 * <p>
 * The result model is typically used in a subsystem following functional programming style.
 * Functions will be written to have {@code Result<T>} as the return type.
 * Instead of using exceptions, code will return failure results.
 * <p>
 * Results can be generated using the factory methods on this class.
 *
 * @param <T> the type of the underlying result for a successful invocation
 */
@BeanDefinition(builderScope = "private")
public final class Result<T>
    implements ImmutableBean, Serializable {
  // two properties are used where one might do to reduce serialized data size

  /**
   * The value.
   * This is only present if the result is a success.
   */
  @PropertyDefinition(get = "field")
  private final T value;
  /**
   * The failure.
   * This is only present if the result is a failure.
   */
  @PropertyDefinition(get = "field")
  private final Failure failure;

  //-------------------------------------------------------------------------
  /**
   * Creates a successful result wrapping a value.
   * <p>
   * This returns a successful result object for the non-null value.
   * <p>
   * Note that passing an instance of {@code Failure} to this method would
   * be a programming error.
   *
   * @param <R> the type of the value
   * @param value  the result value
   * @return a successful result wrapping the value
   */
  public static <R> Result<R> success(R value) {
    return new Result<>(ArgChecker.notNull(value, "value"));
  }

  //-------------------------------------------------------------------------
  /**
   * Creates a failed result specifying the failure reason.
   * <p>
   * The message is produced using a template that contains zero to many "{}" placeholders.
   * Each placeholder is replaced by the next available argument.
   * If there are too few arguments, then the message will be left with placeholders.
   * If there are too many arguments, then the excess arguments are appended to the
   * end of the message. No attempt is made to format the arguments.
   * See {@link Messages#format(String, Object...)} for more details.
   *
   * @param <R> the expected type of the result
   * @param reason  the result reason
   * @param message  a message explaining the failure, uses "{}" for inserting {@code messageArgs}
   * @param messageArgs  the arguments for the message
   * @return a failure result
   */
  public static <R> Result<R> failure(FailureReason reason, String message, Object... messageArgs) {
    String msg = Messages.format(message, messageArgs);
    return new Result<>(Failure.of(reason, msg));
  }

  /**
   * Creates a failed result caused by an exception.
   * <p>
   * The failure will have a reason of {@code ERROR}.
   *
   * @param <R> the expected type of the result
   * @param exception  the cause of the failure
   * @return a failure result
   */
  public static <R> Result<R> failure(Exception exception) {
    return new Result<>(Failure.of(FailureReason.ERROR, exception));
  }

  /**
   * Creates a failed result caused by an exception.
   * <p>
   * The failure will have a reason of {@code ERROR}.
   * <p>
   * The message is produced using a template that contains zero to many "{}" placeholders.
   * Each placeholder is replaced by the next available argument.
   * If there are too few arguments, then the message will be left with placeholders.
   * If there are too many arguments, then the excess arguments are appended to the
   * end of the message. No attempt is made to format the arguments.
   * See {@link Messages#format(String, Object...)} for more details.
   *
   * @param <R> the expected type of the result
   * @param exception  the cause of the failure
   * @param message  a message explaining the failure, uses "{}" for inserting {@code messageArgs}
   * @param messageArgs  the arguments for the message
   * @return a failure result
   */
  public static <R> Result<R> failure(Exception exception, String message, Object... messageArgs) {
    String msg = Messages.format(message, messageArgs);
    return new Result<>(Failure.of(FailureReason.ERROR, msg, exception));
  }

  /**
   * Creates a failed result caused by an exception with a specified reason.
   *
   * @param <R> the expected type of the result
   * @param reason  the result reason
   * @param exception  the cause of the failure
   * @return a failure result
   */
  public static <R> Result<R> failure(FailureReason reason, Exception exception) {
    return new Result<>(Failure.of(reason, exception));
  }

  /**
   * Creates a failed result caused by an exception with a specified reason and message.
   * <p>
   * The message is produced using a template that contains zero to many "{}" placeholders.
   * Each placeholder is replaced by the next available argument.
   * If there are too few arguments, then the message will be left with placeholders.
   * If there are too many arguments, then the excess arguments are appended to the
   * end of the message. No attempt is made to format the arguments.
   * See {@link Messages#format(String, Object...)} for more details.
   *
   * @param <R> the expected type of the result
   * @param reason  the result reason
   * @param exception  the cause of the failure
   * @param message  a message explaining the failure, uses "{}" for inserting {@code messageArgs}
   * @param messageArgs  the arguments for the message
   * @return a failure result
   */
  public static <R> Result<R> failure(FailureReason reason, Exception exception, String message, Object... messageArgs) {
    return new Result<>(Failure.of(reason, Messages.format(message, messageArgs), exception));
  }

  /**
   * Returns a failed result from another failed result.
   * <p>
   * This method ensures the result type matches the expected type.
   * If the specified result is a successful result then an exception is thrown.
   *
   * @param <R> the expected result type
   * @param failureResult  a failure result
   * @return a failure result of the expected type
   * @throws IllegalArgumentException if the result is a success
   */
  @SuppressWarnings("unchecked")
  public static <R> Result<R> failure(Result<?> failureResult) {
    if (failureResult.isSuccess()) {
      throw new IllegalArgumentException("Result must be a failure");
    }
    return (Result<R>) failureResult;
  }

  /**
   * Creates a failed result combining multiple failed results.
   * <p>
   * The input results can be successes or failures, only the failures will be included in the created result.
   * Intended to be used with {@link #anyFailures(Result...)}.
   * <code>
   *   if (Result.anyFailures(result1, result2, result3) {
   *     return Result.failure(result1, result2, result3);
   *   }
   * </code>
   *
   * @param <R> the expected type of the result
   * @param result1  the first result
   * @param result2  the second result
   * @param results  the rest of the results
   * @return a failed result wrapping multiple other failed results
   * @throws IllegalArgumentException if all of the results are successes
   */
  public static <R> Result<R> failure(Result<?> result1, Result<?> result2, Result<?>... results) {
    ArgChecker.notNull(result1, "result1");
    ArgChecker.notNull(result2, "result2");
    ArgChecker.notNull(results, "results");
    ImmutableList<Result<?>> list = ImmutableList.<Result<?>>builder()
      .add(result1)
      .add(result2)
      .addAll(Arrays.asList(results))
      .build();
    return failure(list);
  }

  /**
   * Creates a failed result combining multiple failed results.
   * <p>
   * The input results can be successes or failures, only the failures will be included in the created result.
   * Intended to be used with {@link #anyFailures(Iterable)}.
   * <code>
   *   if (Result.anyFailures(results) {
   *     return Result.failure(results);
   *   }
   * </code>
   *
   * @param <R> the expected type of the result
   * @param results  multiple results, of which at least one must be a failure, not empty
   * @return a failed result wrapping multiple other failed results
   * @throws IllegalArgumentException if results is empty or contains nothing but successes
   */
  public static <R> Result<R> failure(Iterable<? extends Result<?>> results) {
    ArgChecker.notEmpty(results, "results");
    ImmutableSet<FailureItem> items = Guavate.stream(results)
        .filter(Result::isFailure)
        .map(Result::getFailure)
        .flatMap(f -> f.getItems().stream())
        .collect(Guavate.toImmutableSet());
    if (items.isEmpty()) {
      throw new IllegalArgumentException("All results were successes");
    }
    return new Result<>(Failure.of(items));
  }

  //-------------------------------------------------------------------------
  /**
   * Checks if all the results are successful.
   * 
   * @param results  the results to check
   * @return true if all of the results are successes
   */
  public static boolean allSuccessful(Result<?>... results) {
    return Stream.of(results).allMatch(Result::isSuccess);
  }

  /**
   * Checks if all the results are successful.
   * 
   * @param results  the results to check
   * @return true if all of the results are successes
   */
  public static boolean allSuccessful(Iterable<? extends Result<?>> results) {
    return Guavate.stream(results).allMatch(Result::isSuccess);
  }

  /**
   * Checks if any of the results are failures.
   * 
   * @param results  the results to check
   * @return true if any of the results are failures
   */
  public static boolean anyFailures(Result<?>... results) {
    return Stream.of(results).anyMatch(Result::isFailure);
  }

  /**
   * Checks if any of the results are failures.
   * 
   * @param results  the results to check
   * @return true if any of the results are failures
   */
  public static boolean anyFailures(Iterable<? extends Result<?>> results) {
    return Guavate.stream(results).anyMatch(Result::isFailure);
  }

  //-------------------------------------------------------------------------
  /**
   * Creates an instance.
   * 
   * @param value  the value to create from
   */
  private Result(T value) {
    this.value = value;
    this.failure = null;
  }

  /**
   * Creates an instance.
   * 
   * @param failure  the failure to create from
   */
  private Result(Failure failure) {
    this.value = null;
    this.failure = failure;
  }

  @ImmutableValidator
  private void validate() {
    if (value == null && failure == null) {
      throw new IllegalArgumentException("Both value and failure are null");
    }
    if (value != null && failure != null) {
      throw new IllegalArgumentException("Both value and failure are non-null");
    }
  }

  //-------------------------------------------------------------------------
  /**
   * Indicates if this result represents a successful call and has a result available.
   * <p>
   * This is the opposite of {@link #isFailure()}.
   *
   * @return true if the result represents a success and a value is available
   */
  public boolean isSuccess() {
    return value != null;
  }

  /**
   * Indicates if this result represents a failure.
   * <p>
   * This is the opposite of {@link #isSuccess()}.
   *
   * @return true if the result represents a failure
   */
  public boolean isFailure() {
    return failure != null;
  }

  //-------------------------------------------------------------------------
  /**
   * Returns the actual result value if calculated successfully.
   * <p>
   * If this result is a failure then an an IllegalStateException will be thrown.
   * To avoid this, call {@link #isSuccess()} or {@link #isFailure()} first.
   *
   * @return the result value, only available if calculated successfully
   * @throws IllegalStateException if called on a failure result
   */
  public T getValue() {
    if (isFailure()) {
      throw new IllegalStateException("Unable to get a value from a failure result: " + getFailure().getMessage());
    }
    return value;
  }

  /**
   * Returns the failure instance indicating the reason why the calculation failed.
   * <p>
   * If this result is a success then an an IllegalStateException will be thrown.
   * To avoid this, call {@link #isSuccess()} or {@link #isFailure()} first.
   *
   * @return the details of the failure, only available if calculation failed
   * @throws IllegalStateException if called on a success result
   */
  public Failure getFailure() {
    if (isSuccess()) {
      throw new IllegalStateException("Unable to get a failure from a success result");
    }
    return failure;
  }

  //-------------------------------------------------------------------------
  /**
   * Processes a successful result by applying a function that alters the value.
   * <p>
   * This operation allows post-processing of a result value.
   * The specified function represents a conversion to be performed on the value.
   * <p>
   * If this result is a success, then the specified function is invoked.
   * The return value of the specified function is returned to the caller wrapped in a success result.
   * <p>
   * If this result is a failure, then {@code this} is returned.
   * The specified function is not invoked.
   * <p>
   * For example, it allows a {@code double} to be converted to a string:
   * <pre>
   *   result = ...
   *   return result.map(value -> Double.toString(value));
   * </pre>
   *
   * @param <R>  the type of the value in the returned result
   * @param function  the function to transform the value with
   * @return the new result
   */
  public <R> Result<R> map(Function<? super T, ? extends R> function) {
    if (isSuccess()) {
      return Result.success(function.apply(value));
    } else {
      return Result.failure(this);
    }
  }

  /**
   * Processes a successful result by applying a function that returns another result.
   * <p>
   * This operation allows chaining of function calls that produce a result.
   * The specified function will typically call another method that returns a result.
   * <p>
   * If this result is a success, then the specified function is invoked.
   * The return value of the specified function is returned to the caller and may be
   * a success or failure.
   * <p>
   * If this result is a failure, then an equivalent failure is returned.
   * The specified function is not invoked.
   * <p>
   * For example,
   * <pre>{@code
   *   result = ...
   *   return result.flatMap(value -> doSomething(value));
   * }</pre>
   * Identical to {@link #ifSuccess}.
   *
   * @param <R>  the type of the value in the returned result
   * @param function  the function to transform the value with
   * @return the new result
   */
  public <R> Result<R> flatMap(Function<? super T, Result<R>> function) {
    if (isSuccess()) {
      return Objects.requireNonNull(function.apply(value));
    } else {
      return Result.failure(this);
    }
  }

  /**
   * Processes a successful result by applying a function that returns another result.
   * <p>
   * This operation allows chaining of function calls that produce a result.
   * The specified function will typically call another method that returns a result.
   * <p>
   * If this result is a success, then the specified function is invoked.
   * The return value of the specified function is returned to the caller and may be
   * a success or failure.
   * <p>
   * If this result is a failure, then an equivalent failure is returned.
   * The specified function is not invoked.
   * <p>
   * For example,
   * <pre>{@code
   *   result = ...
   *   return result.ifSuccess(value -> doSomething(value));
   * }</pre>
   * Identical to {@link #flatMap}.
   *
   * @param <R>  the type of the value in the returned result
   * @param function  the function to transform the value with
   * @return the new result
   */
  public <R> Result<R> ifSuccess(Function<? super T, Result<R>> function) {
    return flatMap(function);
  }

  /**
   * Combines this result with another result.
   * <p>
   * This operation allows two results to be combined handling succeess and failure.
   * <p>
   * If both results are a success, then the specified function is invoked to combine them.
   * The return value of the specified function is returned to the caller and may be
   * a success or failure.
   * <p>
   * If either result is a failure, then a combination failure is returned.
   * The specified function is not invoked.
   * <pre>
   *   result1 = ...
   *   result2 = ...
   *   return result1.combineWith(result2, (value1, value2) -> doSomething(value1, value2));
   * </pre>
   *
   * @param other  another result
   * @param function  a function for combining values from two results
   * @param <U>  the type of the value in the other result
   * @param <R>  the type of the value in the returned result
   * @return a the result of combining the result values or a failure if either result is a failure
   */
  public <U, R> Result<R> combineWith(Result<U> other, BiFunction<T, U, Result<R>> function) {
    ArgChecker.notNull(other, "other");
    ArgChecker.notNull(function, "function");
    if (isSuccess() && other.isSuccess()) {
      return Objects.requireNonNull(function.apply(value, other.value));
    } else {
      return Result.failure(this, other);
    }
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code Result}.
   * @return the meta-bean, not null
   */
  @SuppressWarnings("rawtypes")
  public static Result.Meta meta() {
    return Result.Meta.INSTANCE;
  }

  /**
   * The meta-bean for {@code Result}.
   * @param <R>  the bean's generic type
   * @param cls  the bean's generic type
   * @return the meta-bean, not null
   */
  @SuppressWarnings("unchecked")
  public static <R> Result.Meta<R> metaResult(Class<R> cls) {
    return Result.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(Result.Meta.INSTANCE);
  }

  /**
   * The serialization version id.
   */
  private static final long serialVersionUID = 1L;

  private Result(
      T value,
      Failure failure) {
    this.value = value;
    this.failure = failure;
    validate();
  }

  @SuppressWarnings("unchecked")
  @Override
  public Result.Meta<T> metaBean() {
    return Result.Meta.INSTANCE;
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
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      Result<?> other = (Result<?>) obj;
      return JodaBeanUtils.equal(value, other.value) &&
          JodaBeanUtils.equal(failure, other.failure);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(value);
    hash = hash * 31 + JodaBeanUtils.hashCode(failure);
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(96);
    buf.append("Result{");
    buf.append("value").append('=').append(value).append(',').append(' ');
    buf.append("failure").append('=').append(JodaBeanUtils.toString(failure));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code Result}.
   * @param <T>  the type
   */
  public static final class Meta<T> extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    @SuppressWarnings("rawtypes")
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code value} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<T> value = (DirectMetaProperty) DirectMetaProperty.ofImmutable(
        this, "value", Result.class, Object.class);
    /**
     * The meta-property for the {@code failure} property.
     */
    private final MetaProperty<Failure> failure = DirectMetaProperty.ofImmutable(
        this, "failure", Result.class, Failure.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "value",
        "failure");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 111972721:  // value
          return value;
        case -1086574198:  // failure
          return failure;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends Result<T>> builder() {
      return new Result.Builder<T>();
    }

    @SuppressWarnings({"unchecked", "rawtypes" })
    @Override
    public Class<? extends Result<T>> beanType() {
      return (Class) Result.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code value} property.
     * @return the meta-property, not null
     */
    public MetaProperty<T> value() {
      return value;
    }

    /**
     * The meta-property for the {@code failure} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Failure> failure() {
      return failure;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 111972721:  // value
          return ((Result<?>) bean).value;
        case -1086574198:  // failure
          return ((Result<?>) bean).failure;
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
   * The bean-builder for {@code Result}.
   * @param <T>  the type
   */
  private static final class Builder<T> extends DirectFieldsBeanBuilder<Result<T>> {

    private T value;
    private Failure failure;

    /**
     * Restricted constructor.
     */
    private Builder() {
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case 111972721:  // value
          return value;
        case -1086574198:  // failure
          return failure;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Builder<T> set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case 111972721:  // value
          this.value = (T) newValue;
          break;
        case -1086574198:  // failure
          this.failure = (Failure) newValue;
          break;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
      return this;
    }

    @Override
    public Builder<T> set(MetaProperty<?> property, Object value) {
      super.set(property, value);
      return this;
    }

    @Override
    public Builder<T> setString(String propertyName, String value) {
      setString(meta().metaProperty(propertyName), value);
      return this;
    }

    @Override
    public Builder<T> setString(MetaProperty<?> property, String value) {
      super.setString(property, value);
      return this;
    }

    @Override
    public Builder<T> setAll(Map<String, ? extends Object> propertyValueMap) {
      super.setAll(propertyValueMap);
      return this;
    }

    @Override
    public Result<T> build() {
      return new Result<T>(
          value,
          failure);
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(96);
      buf.append("Result.Builder{");
      buf.append("value").append('=').append(JodaBeanUtils.toString(value)).append(',').append(' ');
      buf.append("failure").append('=').append(JodaBeanUtils.toString(failure));
      buf.append('}');
      return buf.toString();
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}

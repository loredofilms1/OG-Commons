/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.basics.currency;

import static com.opengamma.collect.TestHelper.assertSerialization;
import static com.opengamma.collect.TestHelper.assertThrows;
import static com.opengamma.collect.TestHelper.coverImmutableBean;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Test {@link FxRate}.
 */
@Test
public class FxRateTest {

  private static final Currency AUD = Currency.AUD;
  private static final Currency CAD = Currency.CAD;
  private static final Currency EUR = Currency.EUR;
  private static final Currency GBP = Currency.GBP;
  private static final Currency USD = Currency.USD;

  //-------------------------------------------------------------------------
  public void test_of_CurrencyCurrencyDouble() {
    FxRate test = FxRate.of(GBP, USD, 1.5d);
    assertEquals(test.getPair(), CurrencyPair.of(GBP, USD));
    assertEquals(test.getRate(), 1.5d, 0);
    assertEquals(test.toString(), "GBP/USD 1.5");
  }

  public void test_of_CurrencyCurrencyDouble_reverseStandardOrder() {
    FxRate test = FxRate.of(USD, GBP, 0.8d);
    assertEquals(test.getPair(), CurrencyPair.of(USD, GBP));
    assertEquals(test.getRate(), 0.8d, 0);
    assertEquals(test.toString(), "USD/GBP 0.8");
  }

  public void test_of_CurrencyCurrencyDouble_same() {
    FxRate test = FxRate.of(USD, USD, 1d);
    assertEquals(test.getPair(), CurrencyPair.of(USD, USD));
    assertEquals(test.getRate(), 1d, 0);
    assertEquals(test.toString(), "USD/USD 1");
  }

  public void test_of_CurrencyCurrencyDouble_invalid() {
    assertThrows(() -> FxRate.of(GBP, USD, -1.5d), IllegalArgumentException.class);
    assertThrows(() -> FxRate.of(GBP, GBP, 2d), IllegalArgumentException.class);
  }

  public void test_of_CurrencyCurrencyDouble_null() {
    assertThrows(() -> FxRate.of((Currency) null, USD, 1.5d), IllegalArgumentException.class);
    assertThrows(() -> FxRate.of(USD, (Currency) null, 1.5d), IllegalArgumentException.class);
    assertThrows(() -> FxRate.of((Currency) null, (Currency) null, 1.5d), IllegalArgumentException.class);
  }

  //-------------------------------------------------------------------------
  public void test_of_CurrencyPairDouble() {
    FxRate test = FxRate.of(CurrencyPair.of(GBP, USD), 1.5d);
    assertEquals(test.getPair(), CurrencyPair.of(GBP, USD));
    assertEquals(test.getRate(), 1.5d, 0);
    assertEquals(test.toString(), "GBP/USD 1.5");
  }

  public void test_of_CurrencyPairDouble_reverseStandardOrder() {
    FxRate test = FxRate.of(CurrencyPair.of(USD, GBP), 0.8d);
    assertEquals(test.getPair(), CurrencyPair.of(USD, GBP));
    assertEquals(test.getRate(), 0.8d, 0);
    assertEquals(test.toString(), "USD/GBP 0.8");
  }

  public void test_of_CurrencyPairDouble_same() {
    FxRate test = FxRate.of(CurrencyPair.of(USD, USD), 1d);
    assertEquals(test.getPair(), CurrencyPair.of(USD, USD));
    assertEquals(test.getRate(), 1d, 0);
    assertEquals(test.toString(), "USD/USD 1");
  }

  public void test_of_CurrencyPairDouble_invalid() {
    assertThrows(() -> FxRate.of(CurrencyPair.of(GBP, USD), -1.5d), IllegalArgumentException.class);
    assertThrows(() -> FxRate.of(CurrencyPair.of(USD, USD), 2d), IllegalArgumentException.class);
  }

  public void test_of_CurrencyPairDouble_null() {
    assertThrows(() -> FxRate.of((CurrencyPair) null, 1.5d), IllegalArgumentException.class);
  }

  //-------------------------------------------------------------------------
  @DataProvider(name = "parseGood")
  Object[][] data_parseGood() {
    return new Object[][] {
        {"USD/EUR 205.123", USD, EUR, 205.123d},
        {"USD/EUR 3.00000000", USD, EUR, 3d},
        {"USD/EUR 2", USD, EUR, 2d},
        {"USD/EUR 0.1", USD, EUR, 0.1d},
        {"EUR/USD 0.001", EUR, USD, 0.001d},
        {"EUR/EUR 1", EUR, EUR, 1d},
        {"cAd/GbP 1.25", CAD, GBP, 1.25d},
    };
  }

  @Test(dataProvider = "parseGood")
  public void test_parse_String_good(String input, Currency base, Currency counter, double rate) {
    assertEquals(FxRate.parse(input), FxRate.of(base, counter, rate));
  }

  @DataProvider(name = "parseBad")
  Object[][] data_parseBad() {
    return new Object[][] {
      {"AUD 1.25"},
      {"AUD/GB 1.25"},
      {"AUD GBP 1.25"},
      {"AUD:GBP 1.25"},
      {"123/456"},
      {"EUR/GBP -1.25"},
      {"EUR/GBP 0"},
      {"EUR/EUR 1.25"},
      {""},
      {null},
    };
  }

  @Test(dataProvider = "parseBad", expectedExceptions = IllegalArgumentException.class)
  public void test_parse_String_bad(String input) {
    FxRate.parse(input);
  }

  //-------------------------------------------------------------------------
  public void test_inverse() {
    FxRate test = FxRate.of(GBP, USD, 1.25d);
    assertEquals(test.inverse(), FxRate.of(USD, GBP, 0.8d));
  }

  public void test_inverse_same() {
    FxRate test = FxRate.of(GBP, GBP, 1d);
    assertEquals(test.inverse(), FxRate.of(GBP, GBP, 1d));
  }

  //-------------------------------------------------------------------------
  public void test_equals_hashCode() {
    FxRate a1 = FxRate.of(AUD, GBP, 1.25d);
    FxRate a2 = FxRate.of(AUD, GBP, 1.25d);
    FxRate b = FxRate.of(USD, GBP, 1.25d);
    FxRate c = FxRate.of(USD, GBP, 1.35d);
    
    assertEquals(a1.equals(a1), true);
    assertEquals(a1.equals(a2), true);
    assertEquals(a1.equals(b), false);
    assertEquals(a1.equals(c), false);
    
    assertEquals(b.equals(a1), false);
    assertEquals(b.equals(a2), false);
    assertEquals(b.equals(b), true);
    assertEquals(b.equals(c), false);
    
    assertEquals(c.equals(a1), false);
    assertEquals(c.equals(a2), false);
    assertEquals(c.equals(b), false);
    assertEquals(c.equals(c), true);
    
    assertEquals(a1.hashCode(), a2.hashCode());
  }

  public void test_equals_bad() {
    FxRate test = FxRate.of(AUD, GBP, 1.25d);
    assertFalse(test.equals(""));
    assertFalse(test.equals(null));
  }

  //-----------------------------------------------------------------------
  public void test_serialization() {
    assertSerialization(FxRate.of(GBP, USD, 1.25d));
    assertSerialization(FxRate.of(GBP, GBP, 1d));
  }

  //-----------------------------------------------------------------------
  public void coverage() {
    coverImmutableBean(FxRate.of(GBP, USD, 1.25d));
  }

}

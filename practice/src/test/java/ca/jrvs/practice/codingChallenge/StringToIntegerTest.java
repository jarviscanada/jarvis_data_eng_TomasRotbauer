package ca.jrvs.practice.codingChallenge;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

public class StringToIntegerTest {

  private StringToInteger atoi = new StringToInteger();

  @Test
  public void builtInParsingAtoi() {
    Assert.assertEquals(123, atoi.builtInParsingAtoi("  +123aa "));
    Assert.assertEquals(-123, atoi.builtInParsingAtoi("  -123 aa "));
    Assert.assertEquals(0, atoi.builtInParsingAtoi("  -+123 aa "));
    Assert.assertEquals(0, atoi.builtInParsingAtoi(""));
    Assert.assertEquals(0, atoi.builtInParsingAtoi("asd4444"));
    Assert.assertEquals(0, atoi.builtInParsingAtoi("+"));
    Assert.assertEquals(0, atoi.builtInParsingAtoi("a"));
    Assert.assertEquals(1, atoi.builtInParsingAtoi("1"));
    Assert.assertEquals(2147483647, atoi.builtInParsingAtoi("2147483648"));
    Assert.assertEquals(-2147483648, atoi.builtInParsingAtoi("-2147483649"));
  }

  @Test
  public void myAtoi() {
    Assert.assertEquals(123, atoi.builtInParsingAtoi("  +123aa "));
    Assert.assertEquals(-123, atoi.builtInParsingAtoi("  -123 aa "));
    Assert.assertEquals(0, atoi.builtInParsingAtoi("  -+123 aa "));
    Assert.assertEquals(0, atoi.builtInParsingAtoi(""));
    Assert.assertEquals(0, atoi.builtInParsingAtoi("asd4444"));
    Assert.assertEquals(0, atoi.builtInParsingAtoi("+"));
    Assert.assertEquals(0, atoi.builtInParsingAtoi("a"));
    Assert.assertEquals(1, atoi.builtInParsingAtoi("1"));
    Assert.assertEquals(2147483647, atoi.builtInParsingAtoi("2147483648"));
    Assert.assertEquals(-2147483648, atoi.builtInParsingAtoi("-2147483649"));
  }
}
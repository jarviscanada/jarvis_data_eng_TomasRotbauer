package ca.jrvs.practice.codingChallenge;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ValidParenthesesTest {

  private final String[] arr = {"(){}[]", "({[]})", "(})", "({[(", "(()[]{([])})", "}", "([{)}])"};
  private ValidParentheses validParentheses;

  @Before
  public void setUp() {
    validParentheses = new ValidParentheses();
  }

  @Test
  public void isValid0() {
    Assert.assertTrue(validParentheses.isValid(arr[0]));
  }
  @Test
  public void isValid1() {
    Assert.assertTrue(validParentheses.isValid(arr[1]));
  }
  @Test
  public void isValid2() {
    Assert.assertFalse(validParentheses.isValid(arr[2]));
  }
  @Test
  public void isValid3() {
    Assert.assertFalse(validParentheses.isValid(arr[3]));
  }
  @Test
  public void isValid4() {
    Assert.assertTrue(validParentheses.isValid(arr[4]));
  }
  @Test
  public void isValid5() {
    Assert.assertFalse(validParentheses.isValid(arr[5]));
  }
  @Test
  public void isValid6() {
    Assert.assertFalse(validParentheses.isValid(arr[6]));
  }
}
package ca.jrvs.practice.codingChallenge;

public class StringToInteger {

  public int builtInParsingAtoi(String s) {
    int retVal = 0;
    int i = 0;

    s = s.trim();
    if (s.length() == 0)
      return 0;

    if (s.charAt(0) == '+' || s.charAt(0) == '-') {
      i++;
      if (i == s.length())
        return 0;
    }

    while (s.codePointAt(i) >= 48 && s.codePointAt(i) <= 57) {
      i++;
      if (i == s.length())
        break;
    }

    try {
      retVal = Integer.parseInt(s.substring(0,i));
    } catch (NumberFormatException ex) {
      retVal = Integer.MAX_VALUE;
    }
    return retVal;
  }

  public int myAtoi(String s) {
    //if (s.matches("^\\s*[+-]?\\d*.*$")
    int retVal = 0;
    int index = 0;
    int sign = 1;
    int currentInt;
    s = s.trim();

    if (s.charAt(0) == '-') {
      sign = -1;
      index++;
    }
    else if (s.charAt(0) == '+')
      index++;

    currentInt = s.codePointAt(index) - 48;
    while (currentInt >= 0 && currentInt <= 9) {
      retVal *= 10;
      retVal += currentInt;
      index++;
      if (index >= s.length())
        break;
      currentInt = s.codePointAt(index) - 48;
    }

    return sign * retVal;
  }

}

package ca.jrvs.practice.javaRegex;

public interface RegexExc {

  /**
   * return true if filename extension is jpg or jpeg (case insensitive)
   *
   * @param filename name of file to test
   * @return boolean
   */
  boolean matchJpeg(String filename);

  /**
   * return true if ip is valid to simplify the problem, IP address range is from 0.0.0.0 to
   * 999.999.999.999
   *
   * @param ip ip address to test
   * @return boolean
   */
  boolean matchIp(String ip);

  /**
   * return true if line is empty (e.g. empty, white space, tabs, etc..)
   *
   * @param line line to test
   * @return boolean
   */
  boolean isEmptyLine(String line);

}
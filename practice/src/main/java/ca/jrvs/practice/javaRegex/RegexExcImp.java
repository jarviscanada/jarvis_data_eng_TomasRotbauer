package ca.jrvs.apps.practice;

public class RegexExcImp implements RegexExc {

  /**
   * return true if filename extension is jpg or jpeg (case insensitive)
   *
   * @param filename name of file to test
   * @return boolean
   */
  @Override
  public boolean matchJpeg(String filename) {
    return filename.matches("^[^/\\\\?%*:|\"<>\\n]+\\.jpe?g$");
  }

  /**
   * return true if ip is valid to simplify the problem, IP address range is from 0.0.0.0 to
   * 999.999.999.999
   *
   * @param ip ip address to test
   * @return boolean
   */
  @Override
  public boolean matchIp(String ip) {
    return ip.matches("(\\d{1,3}\\.){3}\\d{1,3}");
  }

  /**
   * return true if line is empty (e.g. empty, white space, tabs, etc..)
   *
   * @param line line to test
   * @return boolean
   */
  @Override
  public boolean isEmptyLine(String line) {
    return line.matches("^\\s*$");
  }
}

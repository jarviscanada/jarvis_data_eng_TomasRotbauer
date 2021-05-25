package ca.jrvs.apps.grep;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class JavaGrepImp implements JavaGrep {

  final Logger logger = LoggerFactory.getLogger(JavaGrep.class);

  private String regex;
  private String rootPath;
  private String outFile;

  public static void main(String[] args) {
    if (args.length < 3) {
      throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
    }

    //Use default logger config
    BasicConfigurator.configure();

    JavaGrepImp javaGrepImp = new JavaGrepImp();
    javaGrepImp.setRegex(args[0]);
    javaGrepImp.setRootPath(args[1]);
    javaGrepImp.setOutFile(args[2]);

    try {
      javaGrepImp.process();
    } catch (Exception ex) {
      javaGrepImp.logger.error(ex.getMessage(), ex);
    }
  }

  /**
   * Top level search workflow
   *
   * @throws IOException
   */
  @Override
  public void process() throws IOException {
    List<String> matchedLines = new ArrayList<>();

    listFiles(rootPath).forEach(f -> {
      readLines(f).forEach(l -> {
        if (containsPattern(l)) {
          matchedLines.add(l);
        }
      });
    });

    writeToFile(matchedLines);
  }

  /**
   * Traverse a given directory and return all files
   *
   * @param rootDir input directory
   * @return files under rootDir
   */
  @Override
  public List<File> listFiles(String rootDir) {
    List<File> files = new ArrayList<>();
    File dir = new File(rootDir);

    if (!dir.exists())
      return files;

    Arrays.stream(dir.listFiles()).forEach(f -> {
      if (f.isDirectory()) {
        files.addAll(listFiles(f.getAbsolutePath()));
      }
      else files.add(f);
    });

    logger.info("Returning directory files: " + files);
    return files;
  }

  /**
   * Read a file and return all the lines
   * <p>
   * Explain FileReader, BufferReader, and character encoding
   *
   * @param inputFile file to be read
   * @return lines
   * @throws IllegalArgumentException
   */
  @Override
  public List<String> readLines(File inputFile) {
    List<String> lines = new ArrayList<>();
    try {
      lines = Files.readAllLines(inputFile.toPath(), Charset.defaultCharset());
    } catch (IOException e) {
      e.printStackTrace();
      logger.error(e.getMessage());
    }

    lines.forEach(l -> logger.info(l));
    return lines;
  }

  /**
   * check if a line contains the regex pattern (passed by user)
   *
   * @param line input string
   * @return true if there is a match
   */
  @Override
  public boolean containsPattern(String line) {
    return false;
  }

  /**
   * Write lines to a file
   * <p>
   * Explore: FileOutputStream, OutputStreamWriter, and BufferedWriter
   *
   * @param lines matched line
   * @throws IOException if write failed
   */
  @Override
  public void writeToFile(List<String> lines) throws IOException {

  }

  @Override
  public String getRootPath() {
    return rootPath;
  }

  @Override
  public void setRootPath(String rootPath) {
    this.rootPath = rootPath;
  }

  @Override
  public String getRegex() {
    return regex;
  }

  @Override
  public void setRegex(String regex) {
    this.regex = regex;
  }

  @Override
  public String getOutFile() {
    return outFile;
  }

  @Override
  public void setOutFile(String outFile) {
    this.outFile = outFile;
  }
}

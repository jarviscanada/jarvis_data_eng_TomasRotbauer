package ca.jrvs.apps.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JDBCExecutor {

  final Logger logger = LoggerFactory.getLogger(JDBCExecutor.class);

  public static void main(String[] args) {
    BasicConfigurator.configure();
    JDBCExecutor jdbcExecutor = new JDBCExecutor();

    DatabaseConnectionManager dcm = new DatabaseConnectionManager(
        "localhost", "hplussport",
        "postgres", "password");

    try {
      Connection connection = dcm.getConnection();
      CustomerDAO customerDAO = new CustomerDAO(connection);
      customerDAO.findAllSorted(20).forEach(c -> jdbcExecutor.logger.info(c.toString()));
      jdbcExecutor.logger.info("Paged");
      for (int i = 1; i < 3; i++) {
        jdbcExecutor.logger.info("Page number: " + i);
        customerDAO.findAllPaged(10, i).forEach(c -> jdbcExecutor.logger.info(c.toString()));
      }
    } catch (SQLException ex) {
      jdbcExecutor.logger.error(ex.getMessage());
    }
  }
}

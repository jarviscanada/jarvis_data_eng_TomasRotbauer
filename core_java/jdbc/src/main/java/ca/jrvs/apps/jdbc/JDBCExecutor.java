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
      Customer customer = new Customer();
      customer.setFirstName("Patrick");
      customer.setLastName("Star");
      customer.setEmail("patrick.star@bikinibottom.com");
      customer.setPhone("987-654-3210");
      customer.setAddress("120 Conch Street");
      customer.setCity("Bikini Bottom");
      customer.setState("Under The Sea");
      customer.setZipCode("45678");

      Customer dbCustomer = customerDAO.create(customer);
      jdbcExecutor.logger.info(dbCustomer.toString());
      dbCustomer = customerDAO.findById(dbCustomer.getId());
      jdbcExecutor.logger.info(dbCustomer.toString());
      dbCustomer.setEmail("pstar@bikinibottom.com");
      dbCustomer = customerDAO.update(dbCustomer);
      jdbcExecutor.logger.info(dbCustomer.toString());
      customerDAO.delete(dbCustomer.getId());
    } catch (SQLException ex) {
      jdbcExecutor.logger.error(ex.getMessage());
    }
  }
}

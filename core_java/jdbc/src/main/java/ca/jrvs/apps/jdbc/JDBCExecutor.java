package ca.jrvs.apps.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
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
      OrderDAO orderDAO = new OrderDAO(connection);
      List<Order> orders = orderDAO.getOrdersForCustomer(789);
      orders.forEach(o -> jdbcExecutor.logger.info(o.toString()));
    } catch (SQLException ex) {
      jdbcExecutor.logger.error(ex.getMessage());
    }
  }
}

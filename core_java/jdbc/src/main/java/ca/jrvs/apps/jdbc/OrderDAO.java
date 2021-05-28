package ca.jrvs.apps.jdbc;

import ca.jrvs.apps.jdbc.util.DataAccessObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OrderDAO extends DataAccessObject<Order> {

  private static final String GET_ONE = "SELECT"
      + " c.customer_id, c.first_name, c.last_name, c.email, o.order_id,"
      + " o.creation_date, o.total_due, o.status,"
      + " s.salesperson_id, s.first_name, s.last_name, s.email,"
      + " ol.quantity,"
      + " p.code, p.name, p.size, p.variety, p.price"
      + " FROM orders o"
      + " JOIN customer c on o.customer_id = c.customer_id"
      + " JOIN salesperson s on o.salesperson_id=s.salesperson_id"
      + " JOIN order_item ol on ol.order_id=o.order_id"
      + " JOIN product p on ol.product_id = p.product_id"
      + " WHERE o.order_id = ?;";

  public OrderDAO(Connection connection) {
    super(connection);
  }

  @Override
  public Order findById(long id) {
    Order order = new Order();
    try (PreparedStatement statement = this.connection.prepareStatement(GET_ONE)) {
      statement.setLong(1, id);
      ResultSet rs = statement.executeQuery();
      while (rs.next()) {
        order.setId(rs.getLong("order_id"));
        order.setCreationDate(rs.getTimestamp("creation_date"));
        order.setTotalDue(rs.getDouble("total_due"));
        order.setStatus(rs.getString("status"));
        order.setCustomerId(rs.getLong("customer_id"));
        order.setSalespersonId(rs.getLong("salesperson_id"));
      }
    } catch (SQLException ex) {
      logger.error(ex.getMessage());
      throw new RuntimeException(ex);
    }
    return order;
  }

  @Override
  public List<Order> findAll() {
    return null;
  }

  @Override
  public Order update(Order dto) {
    return null;
  }

  @Override
  public Order create(Order dto) {
    return null;
  }

  @Override
  public void delete(long id) {

  }


}
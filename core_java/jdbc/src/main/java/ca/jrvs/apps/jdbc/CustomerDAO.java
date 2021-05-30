package ca.jrvs.apps.jdbc;

import ca.jrvs.apps.jdbc.util.DataAccessObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO extends DataAccessObject<Customer> {

  private static final String INSERT = "INSERT INTO customer (first_name, last_name, "
      + "email, phone, address, city, state, zipcode) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

  private static final String GET_ONE = "SELECT customer_id, first_name, last_name, "
      + "email, phone, address, city, state, zipcode FROM customer WHERE customer_id=?;";

  private static final String UPDATE = "UPDATE customer SET first_name = ?, last_name = ?, "
      + "email = ?, phone = ?, address = ?, city = ?, state = ?, zipcode = ? WHERE customer_id = ?;";

  private static final String DELETE = "DELETE FROM customer WHERE customer_id = ?;";

  private static final String GET_ALL_LMT = "SELECT customer_id, first_name, last_name, email, "
      + "phone, address, city, state, zipcode FROM customer ORDER BY last_name, first_name LIMIT ?;";

  private static final String GET_ALL_PAGED = "SELECT customer_id, first_name, last_name, email, "
      + "phone, address, city, state, zipcode FROM customer ORDER BY last_name, first_name LIMIT ?"
      + "OFFSET ?;";

  public CustomerDAO(Connection connection) {
    super(connection);
  }

  @Override
  public Customer findById(long id) {
    Customer customer = new Customer();
    try (PreparedStatement statement = this.connection.prepareStatement(GET_ONE)) {
      statement.setLong(1, id);
      ResultSet rs = statement.executeQuery();
      while (rs.next()) {
        setUpCustomer(rs, customer);
      }
    } catch (SQLException ex) {
      logger.error(ex.getMessage());
      throw new RuntimeException(ex);
    }
    return customer;
  }

  @Override
  public List<Customer> findAll() {
    return null;
  }

  @Override
  public Customer update(Customer dto) {
    Customer customer;
    try {
      this.connection.setAutoCommit(false);
    } catch (SQLException ex) {
    logger.error(ex.getMessage());
    throw new RuntimeException(ex);
  }
    try (PreparedStatement statement = this.connection.prepareStatement(UPDATE)) {
      setUpStatement(dto, statement);
      statement.setLong(9, dto.getId());
      statement.execute();
      this.connection.commit();
      customer = this.findById(dto.getId());
    } catch (SQLException ex) {
      try {
        this.connection.rollback();
      } catch (SQLException e) {
        logger.error(e.getMessage());
        throw new RuntimeException(e);
      }
      logger.error(ex.getMessage());
      throw new RuntimeException(ex);
    }
    return customer;
  }

  @Override
  public Customer create(Customer dto) {
    try (PreparedStatement statement = this.connection.prepareStatement(INSERT)) {
      setUpStatement(dto, statement);
      statement.execute();
      int id = this.getLastVal(CUSTOMER_SEQUENCE);
      return this.findById(id);
    } catch (SQLException ex) {
      logger.error(ex.getMessage());
      throw new RuntimeException(ex);
    }
  }

  @Override
  public void delete(long id) {
    try (PreparedStatement statement = this.connection.prepareStatement(DELETE)) {
      statement.setLong(1, id);
      statement.execute();
    } catch (SQLException ex) {
      logger.error(ex.getMessage());
      throw new RuntimeException(ex);
    }
  }

  public List<Customer> findAllSorted(int limit) {
    List<Customer> customers = new ArrayList<>();
    try (PreparedStatement statement = this.connection.prepareStatement(GET_ALL_LMT)) {
      statement.setInt(1, limit);
      ResultSet rs = statement.executeQuery();
      while (rs.next()) {
        Customer customer = new Customer();
        setUpCustomer(rs, customer);
        customers.add(customer);
      }
    } catch (SQLException ex) {
      logger.error(ex.getMessage());
      throw new RuntimeException(ex);
    }
    return customers;
  }

  public List<Customer> findAllPaged(int limit, int pageNumber) {
    int offset = (pageNumber - 1) * limit;
    List<Customer> customers = new ArrayList<>();
    try (PreparedStatement statement = this.connection.prepareStatement(GET_ALL_PAGED)) {
      if (limit < 1) {
        limit = 10;
      }
      statement.setInt(1, limit);
      statement.setInt(2, offset);
      ResultSet rs = statement.executeQuery();
      while (rs.next()) {
        Customer customer = new Customer();
        setUpCustomer(rs, customer);
        customers.add(customer);
      }
    } catch (SQLException ex) {
      logger.error(ex.getMessage());
      throw new RuntimeException(ex);
    }
    return customers;
  }

  private void setUpStatement(Customer dto, PreparedStatement statement) throws SQLException {
    statement.setString(1, dto.getFirstName());
    statement.setString(2, dto.getLastName());
    statement.setString(3, dto.getEmail());
    statement.setString(4, dto.getPhone());
    statement.setString(5, dto.getAddress());
    statement.setString(6, dto.getCity());
    statement.setString(7, dto.getState());
    statement.setString(8, dto.getZipCode());
  }

  private void setUpCustomer(ResultSet rs, Customer customer) throws SQLException {
    customer.setId(rs.getLong("customer_id"));
    customer.setFirstName(rs.getString("first_name"));
    customer.setLastName(rs.getString("last_name"));
    customer.setEmail(rs.getString("email"));
    customer.setPhone(rs.getString("phone"));
    customer.setAddress(rs.getString("address"));
    customer.setCity(rs.getString("city"));
    customer.setState(rs.getString("state"));
    customer.setZipCode(rs.getString("zipcode"));
  }
}

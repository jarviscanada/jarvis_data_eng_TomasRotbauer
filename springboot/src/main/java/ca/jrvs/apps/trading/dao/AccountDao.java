package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.Account;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class AccountDao extends JdbcCrudDao<Account> {

  private static final Logger logger = LoggerFactory.getLogger(TraderDao.class);

  private final String TABLE_NAME = "account";
  private final String ID_COLUMN = "id";

  private JdbcTemplate jdbcTemplate;
  private SimpleJdbcInsert simpleInsert;

  @Autowired
  public AccountDao(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
    this.simpleInsert = new SimpleJdbcInsert(dataSource).withTableName(TABLE_NAME)
        .usingGeneratedKeyColumns(ID_COLUMN);
  }

  @Override
  public JdbcTemplate getJdbcTemplate() {
    return jdbcTemplate;
  }

  @Override
  public SimpleJdbcInsert getSimpleJdbcInsert() {
    return simpleInsert;
  }

  @Override
  public String getTableName() {
    return TABLE_NAME;
  }

  @Override
  public String getIdColumnName() {
    return ID_COLUMN;
  }

  @Override
  Class<Account> getEntityClass() {
    return Account.class;
  }

  /**
   * helper method that updates one quote
   *
   * @param entity
   */
  @Override
  public int updateOne(Account entity) {
    throw new UnsupportedOperationException("Not implemented");
  }

  public void updateBalance(Integer accountId, Double newAmount) {
    if (accountId == null) {
      throw new NullPointerException("Account id can't be null");
    }
    String updateSql = "UPDATE " + getTableName() + " SET amount =? " + "WHERE "
        + getIdColumnName() + " =?";
    getJdbcTemplate().update(updateSql, newAmount, accountId);
  }

  @Override
  public <S extends Account> Iterable<S> saveAll(Iterable<S> iterable) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void delete(Account account) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void deleteAll(Iterable<? extends Account> iterable) {
    throw new UnsupportedOperationException("Not implemented");
  }
}

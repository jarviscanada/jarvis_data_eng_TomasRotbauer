package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.Position;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PositionDao {

  private static final Logger logger = LoggerFactory.getLogger(TraderDao.class);

  private final String TABLE_NAME = "position";
  private final String ID_COLUMN = "account_id";
  private final String TICKER_COLUMN = "ticker";

  private JdbcTemplate jdbcTemplate;

  @Autowired
  public PositionDao(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  public JdbcTemplate getJdbcTemplate() {
    return jdbcTemplate;
  }

  public String getTableName() {
    return TABLE_NAME;
  }

  public String getIdColumnName() {
    return ID_COLUMN;
  }

  public String getTickerColumnName() {
    return TICKER_COLUMN;
  }

  Class<Position> getEntityClass() {
    return Position.class;
  }

  /**
   * Save an entity and update auto-generated integer ID
   * @param entity to be saved
   * @return saved entity
   */
  public Position save(Position entity) {
    throw new UnsupportedOperationException("Position view is read-only");
  }

  /**
   * helper method that updates one quote
   *
   * @param entity
   */
  public int updateOne(Position entity) {
    throw new UnsupportedOperationException("Not implemented");
  }

  public Optional<Position> findById(Position id) {
    Optional<Position> entity = Optional.empty();
    String selectSql = "SELECT * FROM " + getTableName() + " WHERE " + getIdColumnName() + " =?"
        + " AND " + getTickerColumnName() + " =?";

    try {
      entity = Optional.ofNullable(getJdbcTemplate()
          .queryForObject(selectSql,
              BeanPropertyRowMapper.newInstance(getEntityClass()), id.getAccountId(), id.getTicker()));
    } catch(IncorrectResultSizeDataAccessException e) {
      logger.debug("Can't find position", e);
    }
    return entity;
  }

  public boolean existsById(Position id) {
    return findById(id).isPresent();
  }

  public List<Position> findAll() {
    String selectSql = "SELECT * FROM " + getTableName();
    return getJdbcTemplate().query(selectSql, BeanPropertyRowMapper.newInstance(getEntityClass()));
  }

  public List<Position> findAllById(Iterable<Position> ids) {
    List<Position> results = new ArrayList<>();
    Optional<Position> row;
    for (Position id : ids) {
      row = findById(id);
      if (!row.isPresent())
        continue;
      results.add(row.get());
    }
    return results;
  }

  public long count() {
    return findAll().size();
  }

  public void deleteById(Integer id) {
    throw new UnsupportedOperationException("Position view is read-only");
  }

  public void deleteAll() {
    throw new UnsupportedOperationException("Position view is read-only");
  }

  public <S extends Position> Iterable<S> saveAll(Iterable<S> iterable) {
    throw new UnsupportedOperationException("Position view is read-only");
  }

  public void delete(Position trader) {
    throw new UnsupportedOperationException("Position view is read-only");
  }

  public void deleteAll(Iterable<? extends Position> iterable) {
    throw new UnsupportedOperationException("Not implemented");
  }
}

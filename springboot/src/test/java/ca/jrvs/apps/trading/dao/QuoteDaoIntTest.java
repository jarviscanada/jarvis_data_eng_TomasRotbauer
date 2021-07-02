package ca.jrvs.apps.trading.dao;

import static org.junit.Assert.*;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.Quote;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class})
@Sql({"classpath:schema.sql"})
public class QuoteDaoIntTest {

  @Autowired
  private QuoteDao quoteDao;

  private Quote savedQuote;

  @Before
  public void insertOne() {
    savedQuote = new Quote();
    savedQuote.setAskPrice(10d);
    savedQuote.setAskSize(10);
    savedQuote.setBidPrice(10.2d);
    savedQuote.setBidSize(10);
    savedQuote.setId("aapl");
    savedQuote.setLastPrice(10.1d);
    quoteDao.save(savedQuote);
  }

  @Test
  public void testFindById() {
    Optional<Quote> found;
    found = quoteDao.findById("aapl");
    assertTrue(found.isPresent());
    assertEquals(found.get().getId(), "aapl");
    assertEquals(found.get().getBidPrice(), 10.2d, 0.0);
    assertEquals(found.get().getAskPrice(), 10d, 0.0);
    assertEquals(found.get().getAskSize(), 10, 0.0);
    assertEquals(found.get().getBidSize(), 10d, 0.0);
    assertEquals(found.get().getLastPrice(), 10.1d, 0.0);
  }

  @Test
  public void testSaveAll() {
    List<Quote> quotes = new ArrayList<>();
    quotes.add(savedQuote);
    Quote otherQuote = new Quote();
    otherQuote.setId("amzn");
    otherQuote.setBidPrice(11.0);
    otherQuote.setLastPrice(12.9);
    otherQuote.setBidSize(1);
    otherQuote.setAskSize(2);
    otherQuote.setAskPrice(90.0);
    otherQuote.setTicker("amzn");
    quotes.add(otherQuote);
    quoteDao.saveAll(quotes);

    assertEquals(quoteDao.findAll().size(), 2);
    assertEquals(quoteDao.count(), 2);
    assertEquals(quoteDao.findAll().get(0).getId(), "aapl");
    assertEquals(quoteDao.findAll().get(1).getId(), "amzn");
  }

  @Test
  public void testExistsById() {
    assertTrue(quoteDao.existsById("aapl"));
  }

  @Test
  public void testDeleteById() {
    quoteDao.deleteById("aapl");
    assertFalse(quoteDao.existsById("aapl"));
  }

  @After
  public void deleteAll() {
    quoteDao.deleteAll();
  }
}
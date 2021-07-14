package ca.jrvs.apps.trading.service;

import static org.junit.Assert.*;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.dao.ResourceNotFoundException;
import ca.jrvs.apps.trading.model.domain.Quote;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@Import(TestConfig.class)
//@SpringBootTest(classes = {TestConfig.class})
@Sql({"classpath:schema.sql"})
public class QuoteServiceIntTest {

  private static Quote savedQuote;

  @Autowired
  private QuoteService quoteService;

  @Autowired
  private QuoteDao quoteDao;

  @BeforeClass
  public static void configureQuote() {
    savedQuote = new Quote();
    savedQuote.setAskPrice(10d);
    savedQuote.setAskSize(10);
    savedQuote.setBidPrice(10.2d);
    savedQuote.setBidSize(10);
    savedQuote.setId("AAPL");
    savedQuote.setLastPrice(10.1d);
  }

  @After
  public void cleanUp() {
    quoteDao.deleteAll();
  }

  @Test
  public void updateMarketData() throws ResourceNotFoundException {
    savedQuote.setLastPrice(-1.0);
    quoteDao.save(savedQuote);
    quoteService.updateMarketData();
    assertTrue(quoteDao.findById("AAPL").isPresent());
    double lastPrice = quoteDao.findById("AAPL").get().getLastPrice();
    assertFalse(lastPrice == -1.0);
  }

  @Test
  public void saveQuotes() {
    List<String> tickers = new ArrayList<>();
    tickers.add("AMZN");
    tickers.add("GOOGL");
    List<Quote> quotes = quoteService.saveQuotes(tickers);
    assertEquals(quotes.size(), 2);
    assertEquals(quotes.get(0).getId(), "AMZN");
    assertEquals(quotes.get(1).getId(), "GOOGL");
    assertTrue(quoteDao.existsById("AMZN"));
    assertTrue(quoteDao.existsById("GOOGL"));
  }

  @Test
  public void saveQuote() {
    quoteService.saveQuote(savedQuote);
    assertTrue(quoteDao.existsById("AAPL"));
    assertEquals(quoteDao.findById("AAPL").get().getId(), "AAPL");
  }

  @Test
  public void findIexQuoteByTicker() {
    assertEquals(quoteService.findIexQuoteByTicker("AMZN").getSymbol(), "AMZN");
    assertEquals(quoteService.findIexQuoteByTicker("AAPL").getSymbol(), "AAPL");

    try {
      quoteService.findIexQuoteByTicker("AMQN");
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void findAllQuotes() {
    List<Quote> quotes = new ArrayList<>();
    quotes.add(savedQuote);
    Quote otherQuote = new Quote();
    otherQuote.setId("AMZN");
    otherQuote.setBidPrice(11.0);
    otherQuote.setLastPrice(12.9);
    otherQuote.setBidSize(1);
    otherQuote.setAskSize(2);
    otherQuote.setAskPrice(90.0);
    otherQuote.setTicker("AMZN");
    quotes.add(otherQuote);
    quoteDao.saveAll(quotes);

    assertEquals(quoteService.findAllQuotes().size(), 2);
    assertEquals(quoteService.findAllQuotes().get(0).getId(), "AAPL");
    assertEquals(quoteService.findAllQuotes().get(1).getId(), "AMZN");
  }
}
package ca.jrvs.apps.trading.dao;

import static org.junit.Assert.*;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.Position;
import ca.jrvs.apps.trading.model.domain.Quote;
import ca.jrvs.apps.trading.model.domain.SecurityOrder;
import ca.jrvs.apps.trading.model.domain.Trader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.assertj.core.util.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class})
@Sql({"classpath:schema.sql"})
public class PositionDaoIntTest {

  @Autowired
  private PositionDao positionDao;

  @Autowired
  private SecurityOrderDao securityOrderDao;

  @Autowired
  private AccountDao accountDao;

  @Autowired
  private QuoteDao quoteDao;

  @Autowired
  private TraderDao traderDao;

  private static Position position;
  private static SecurityOrder savedSecurityOrder;
  private static Account savedAccount;
  private static Quote savedQuote;
  private static Trader savedTrader;

  @BeforeClass
  public static void setUpAccount() {
    savedTrader = new Trader();
    savedTrader.setId(1);
    savedTrader.setFirstName("John");
    savedTrader.setLastName("Wick");
    savedTrader.setCountry("USA");
    SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
    try {
      savedTrader.setDob(ft.parse("1980-09-09"));
    } catch (ParseException e) {
      System.out.println("Cannot parse the date specified");
    }
    savedTrader.setEmail("johnwick@gmail.com");

    savedAccount = new Account();
    savedAccount.setId(1);
    savedAccount.setTraderId(1);
    savedAccount.setAmount(2.00);

    savedQuote = new Quote();
    savedQuote.setAskPrice(10d);
    savedQuote.setAskSize(10);
    savedQuote.setBidPrice(10.2d);
    savedQuote.setBidSize(10);
    savedQuote.setId("AMZN");
    savedQuote.setLastPrice(10.1d);

    savedSecurityOrder = new SecurityOrder();
    savedSecurityOrder.setId(1);
    savedSecurityOrder.setAccountId(1);
    savedSecurityOrder.setSize(2);
    savedSecurityOrder.setNotes("Amazon order");
    savedSecurityOrder.setPrice(33.33);
    savedSecurityOrder.setStatus("FILLED");
    savedSecurityOrder.setTicker("AMZN");

    position = new Position();
    position.setPosition(2);
    position.setTicker("AMZN");
    position.setAccountId(1);

  }

  @Before
  public void insertOne() {
    traderDao.save(savedTrader);
    accountDao.save(savedAccount);
    quoteDao.save(savedQuote);
    securityOrderDao.save(savedSecurityOrder);
  }

  @After
  public void deleteOne() {
    securityOrderDao.deleteById(1);
    accountDao.deleteById(1);
    quoteDao.deleteById("AMZN");
    traderDao.deleteById(1);
  }

  @Test
  public void findAllById() {
    Position dummy = new Position();
    dummy.setPosition(0);
    dummy.setTicker("AAPL");
    dummy.setAccountId(0);
    List<Position> positions = Lists
        .newArrayList(positionDao.findAllById(Arrays.asList(position, dummy)));
    assertEquals(1, positions.size());
    assertEquals(position.getPosition(), positions.get(0).getPosition(), 0);
  }

  @Test
  public void findAll() {
    List<Position> positions = Lists
        .newArrayList(positionDao.findAll());
    assertEquals(1, positionDao.count());
    assertEquals(1, positions.size());
    assertEquals(position.getPosition(), positions.get(0).getPosition());
  }

  @Test
  public void findById() {
    Position dummy = new Position();
    dummy.setTicker("AAPL");
    dummy.setAccountId(3);

    Optional<Position> result = positionDao.findById(position);
    assertTrue(positionDao.existsById(position));
    assertFalse(positionDao.existsById(dummy));
    assertTrue(result.isPresent());
    assertEquals(2, result.get().getPosition(), 0);
    assertEquals(1, result.get().getAccountId(), 0);
    assertEquals("AMZN", result.get().getTicker());
  }

  @Test
  public void existsByTraderId() {
    assertTrue(positionDao.existsByAccountId(1));
    assertFalse(positionDao.existsByAccountId(0));
  }
}
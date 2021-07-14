package ca.jrvs.apps.trading.dao;

import static org.junit.Assert.*;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.Account;
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
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@Import(TestConfig.class)
//@SpringBootTest(classes = {TestConfig.class})
@Sql({"classpath:schema.sql"})
public class SecurityOrderDaoIntTest {

  @Autowired
  private SecurityOrderDao securityOrderDao;

  @Autowired
  private AccountDao accountDao;

  @Autowired
  private QuoteDao quoteDao;

  @Autowired
  private TraderDao traderDao;

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
    savedSecurityOrder.setStatus("All good");
    savedSecurityOrder.setTicker("AMZN");
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
    List<Account> accounts = Lists
        .newArrayList(accountDao.findAllById(Arrays.asList(savedAccount.getId(), -1)));
    assertEquals(1, accounts.size());
    assertEquals(savedAccount.getAmount(), accounts.get(0).getAmount(), 0);
  }

  @Test
  public void findAll() {
    List<SecurityOrder> orders = Lists
        .newArrayList(securityOrderDao.findAll());
    assertEquals(1, securityOrderDao.count());
    assertEquals(1, orders.size());
    assertEquals(savedSecurityOrder.getNotes(), orders.get(0).getNotes());
  }

  @Test
  public void findById() {
    Optional<SecurityOrder> result = securityOrderDao.findById(1);
    assertTrue(securityOrderDao.existsById(1));
    assertFalse(securityOrderDao.existsById(42));
    assertTrue(result.isPresent());
    assertEquals(1, result.get().getId(), 0);
    assertEquals(1, result.get().getAccountId(), 0);
    assertEquals("Amazon order", result.get().getNotes());
    assertEquals("All good", result.get().getStatus());
    assertEquals(33.33, result.get().getPrice(), 0);
    assertEquals(2, result.get().getSize(), 0);
    assertEquals("AMZN", result.get().getTicker());
  }

  @Test
  public void deleteById() {
    securityOrderDao.deleteById(0);
    assertTrue(securityOrderDao.existsById(1));
    securityOrderDao.deleteById(1);
    assertFalse(securityOrderDao.existsById(1));
  }

  @Test
  public void deleteAll() {
    assertTrue(securityOrderDao.existsById(1));
    securityOrderDao.deleteAll();
    assertFalse(securityOrderDao.existsById(1));
  }

  @Test
  public void deleteByAccountId() {
    assertTrue(securityOrderDao.existsById(1));
    securityOrderDao.deleteByAccountId(1);
    assertFalse(securityOrderDao.existsById(1));
  }
}
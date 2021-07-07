package ca.jrvs.apps.trading.service;

import static org.junit.Assert.*;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.TraderDao;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.Trader;
import ca.jrvs.apps.trading.model.domain.TraderAccountView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
public class TraderAccountServiceIntTest {

  private static Trader savedTrader;

  @BeforeClass
  public static void setUpTrader() {
    savedTrader = new Trader();
    savedTrader.setId(null);
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
  }

  private TraderAccountView savedView;
  @Autowired
  private TraderAccountService traderAccountService;
  @Autowired
  private TraderDao traderDao;
  @Autowired
  private AccountDao accountDao;

  @Before
  public void setUp() {
    savedTrader.setId(null);
  }

  @After
  public void cleanUp() {
    accountDao.deleteAll();
    traderDao.deleteAll();
  }

  @Test
  public void createTraderAndAccount() {
    traderAccountService.createTraderAndAccount(savedTrader);
    assertEquals(1, traderDao.count());
    assertEquals(1, accountDao.count());
    Trader trader = traderDao.findAll().get(0);
    Account account = accountDao.findAll().get(0);
    assertEquals(trader.getId(), account.getId());
    assertEquals(0, account.getAmount(), 0);
    assertEquals("John", trader.getFirstName());
  }

  @Test
  public void deleteTraderById() {
    try {
      traderAccountService.deleteTraderById(1);
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    } catch (Exception e) {
      fail();
    }

    TraderAccountView result = traderAccountService.createTraderAndAccount(savedTrader);
    accountDao.updateBalance(result.getAccount().getId(), 24.4);

    try {
      traderAccountService.deleteTraderById(result.getTrader().getId());
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    } catch (Exception e) {
      fail();
    }

    accountDao.updateBalance(result.getAccount().getId(), 0.0);
    traderAccountService.deleteTraderById(result.getTrader().getId());
    assertFalse(traderDao.existsById(result.getTrader().getId()));
    assertFalse(accountDao.existsById(result.getAccount().getId()));
  }

  @Test
  public void deposit() {
    TraderAccountView result = traderAccountService.createTraderAndAccount(savedTrader);
    traderAccountService.deposit(result.getTrader().getId(), 380.0);
    assertEquals(380.0, accountDao.findAll().get(0).getAmount(), 0.0);

    try {
      traderAccountService.deposit(result.getTrader().getId(), -5.0);
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    } catch (Exception e) {
      fail();
    } finally {
      assertEquals(380.0, accountDao.findAll().get(0).getAmount(), 0.0);
    }
  }

  @Test
  public void withdraw() {
    TraderAccountView result = traderAccountService.createTraderAndAccount(savedTrader);
    traderAccountService.deposit(result.getTrader().getId(), 380.0);
    assertEquals(380.0, accountDao.findAll().get(0).getAmount(), 0.0);

    traderAccountService.withdraw(result.getTrader().getId(), 300.0);
    assertEquals(80.0, accountDao.findAll().get(0).getAmount(), 0.0);

    try {
      traderAccountService.withdraw(result.getTrader().getId(), -5.0);
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    } catch (Exception e) {
      fail();
    } finally {
      assertEquals(80.0, accountDao.findAll().get(0).getAmount(), 0.0);
    }

    try {
      traderAccountService.withdraw(result.getTrader().getId(), 80.01);
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    } catch (Exception e) {
      fail();
    } finally {
      assertEquals(80.0, accountDao.findAll().get(0).getAmount(), 0.0);
    }
  }
}
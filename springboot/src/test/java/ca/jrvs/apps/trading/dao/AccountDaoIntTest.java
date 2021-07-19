package ca.jrvs.apps.trading.dao;

import static org.junit.Assert.*;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.Account;
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
public class AccountDaoIntTest {

  @Autowired
  private AccountDao accountDao;

  @Autowired
  private TraderDao traderDao;

  private static Account savedAccount;
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
  }

  @Before
  public void insertOne() {
    traderDao.save(savedTrader);
    accountDao.save(savedAccount);
  }

  @After
  public void deleteOne() {
    accountDao.deleteById(1);
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
    List<Account> accounts = Lists
        .newArrayList(accountDao.findAll());
    assertEquals(1, accountDao.count());
    assertEquals(1, accounts.size());
    assertEquals(savedAccount.getAmount(), accounts.get(0).getAmount(), 0);
  }

  @Test
  public void findById() {
    Optional<Account> result = accountDao.findById(1);
    assertTrue(accountDao.existsById(1));
    assertFalse(accountDao.existsById(42));
    assertTrue(result.isPresent());
    assertEquals(1, result.get().getId(), 0);
    assertEquals(2.0, result.get().getAmount(), 0);
    assertEquals(1, result.get().getTraderId(), 0);
  }

  @Test
  public void deleteById() {
    accountDao.deleteById(0);
    assertTrue(accountDao.existsById(1));
    accountDao.deleteById(1);
    assertFalse(accountDao.existsById(1));
  }

  @Test
  public void deleteAll() {
    assertTrue(accountDao.existsById(1));
    accountDao.deleteAll();
    assertFalse(accountDao.existsById(1));
  }

  @Test
  public void updateBalance() {
    accountDao.updateBalance(1, 29.0);
    assertEquals(29.0, accountDao.findById(1).get().getAmount(), 0);
  }
}
package ca.jrvs.apps.trading.dao;

import static org.junit.Assert.*;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.Trader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
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
public class TraderDaoIntTest {

  @Autowired
  private TraderDao traderDao;

  private static Trader savedTrader;

  @BeforeClass
  public static void setUpTrader() {
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
  }

  @Before
  public void insertOne() {
    traderDao.save(savedTrader);
  }

  @After
  public void deleteOne() {
    traderDao.deleteById(1);
  }

  @Test
  public void findAllById() {
    List<Trader> traders = Lists
        .newArrayList(traderDao.findAllById(Arrays.asList(savedTrader.getId(), -1)));
    assertEquals(1, traders.size());
    assertEquals(savedTrader.getCountry(), traders.get(0).getCountry());
  }

  @Test
  public void findAll() {
    List<Trader> traders = Lists
        .newArrayList(traderDao.findAll());
    assertEquals(1, traderDao.count());
    assertEquals(1, traders.size());
    assertEquals(savedTrader.getCountry(), traders.get(0).getCountry());
  }

  @Test
  public void findById() {
    Optional<Trader> result = traderDao.findById(1);
    assertTrue(traderDao.existsById(1));
    assertFalse(traderDao.existsById(42));
    assertTrue(result.isPresent());
    assertEquals(1, result.get().getId(), 0);
    assertEquals("John", result.get().getFirstName());
    assertEquals("1980-09-09 00:00:00.0", result.get().getDob().toString());
  }

  @Test
  public void deleteById() {
    traderDao.deleteById(0);
    assertTrue(traderDao.existsById(1));
    traderDao.deleteById(1);
    assertFalse(traderDao.existsById(1));
  }

  @Test
  public void deleteAll() {
    assertTrue(traderDao.existsById(1));
    traderDao.deleteAll();
    assertFalse(traderDao.existsById(1));
  }
}
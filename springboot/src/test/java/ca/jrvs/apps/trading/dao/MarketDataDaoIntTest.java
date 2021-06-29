package ca.jrvs.apps.trading.dao;

import static org.junit.Assert.*;

import ca.jrvs.apps.trading.model.config.MarketDataConfig;
import ca.jrvs.apps.trading.model.domain.IexQuote;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.Before;
import org.junit.Test;

public class MarketDataDaoIntTest {

  private MarketDataDao dao;

  @Before
  public void setUp() {
    PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
    cm.setMaxTotal(50);
    cm.setDefaultMaxPerRoute(50);
    MarketDataConfig marketDataConfig = new MarketDataConfig();
    marketDataConfig.setHost("https://cloud.iexapis.com/v1/");
    //WARNING: DO NOT commit any key or password to GitHub. Use env var instead.
    marketDataConfig.setToken(System.getenv("IEX_PUB_TOKEN"));

    dao = new MarketDataDao(cm, marketDataConfig);
  }

  @Test
  public void findAllById() throws IOException {
    //happy path
    List<IexQuote> quoteList = dao.findAllById(Arrays.asList("AAPL", "FB"));
    assertEquals(2, quoteList.size());
    assertEquals("AAPL", quoteList.get(0).getSymbol());

    //sad path
    try {
      dao.findAllById(Arrays.asList("APPL", "FB2"));
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    } catch(Exception e) {
      fail();
    }
  }

  @Test
  public void findById() {
    String ticker = "AAPL";
    IexQuote iexQuote = dao.findById(ticker).get();
    assertEquals(ticker, iexQuote.getSymbol());
  }
}
package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.config.MarketDataConfig;
import ca.jrvs.apps.trading.model.domain.IexQuote;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.data.repository.CrudRepository;

/**
 * MarketDataDao is responsible for getting Quotes from IEX
 */
public class MarketDataDao implements CrudRepository<IexQuote, String> {

  private static final String IEX_BATCH_PATH = "/stock/market/batch?symbols=%s&types=quote&token=";
  private final String IEX_BATCH_URL;

  private static final int HTTP_OK = 200;
  private static final int HTTP_NOT_FOUND = 404;

  private final Logger logger = LoggerFactory.getLogger(MarketDataDao.class);
  private final HttpClientConnectionManager httpClientConnectionManager;

  @Autowired
  public MarketDataDao(HttpClientConnectionManager httpClientConnectionManager,
      MarketDataConfig marketDataConfig) {
    this.httpClientConnectionManager = httpClientConnectionManager;
    IEX_BATCH_URL = marketDataConfig.getHost() + IEX_BATCH_PATH + marketDataConfig.getToken();
  }

  @Override
  public <S extends IexQuote> S save(S s) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public <S extends IexQuote> Iterable<S> saveAll(Iterable<S> iterable) {
    throw new UnsupportedOperationException("Not implemented");
  }

  /**
   * Get an IexQuote (helper method findAllById)
   *
   * @param ticker
   * @throws IllegalArgumentException if a given ticker is invalid
   * @throws DataRetrievalFailureException if HTTP request failed
   */
  @Override
  public Optional<IexQuote> findById(String ticker) {
    Optional<IexQuote> iexQuote;
    List<IexQuote> quotes = findAllById(Collections.singletonList(ticker));

    if (quotes.size() == 0) {
      return Optional.empty();
    } else if (quotes.size() == 1) {
      iexQuote = Optional.of(quotes.get(0));
    } else {
      throw new DataRetrievalFailureException("Unexpected number of quotes");
    }
    return iexQuote;
  }

  @Override
  public boolean existsById(String s) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public Iterable<IexQuote> findAll() {
    throw new UnsupportedOperationException("Not implemented");
  }

  /**
   * Get quotes from IEX
   * @param tickers is a list of tickers
   * @return a list of IexQuote objects
   * @throws IllegalArgumentException if a given ticker is invalid or tickers is empty
   * @throws DataRetrievalFailureException if HTTP request failed
   */
  @Override
  public List<IexQuote> findAllById(Iterable<String> tickers) {
    String symbols = String.join(",", tickers);
    String url = String.format(IEX_BATCH_URL, symbols);

    //HTTP response
    String response = executeHttpGet(url)
        .orElseThrow(() -> new IllegalArgumentException("Invalid ticker"));

    //Array of JSON documents
    JSONObject IexQuotesJson = new JSONObject(response);

    //Get number of documents
    if (IexQuotesJson.length() == 0) {
      throw new IllegalArgumentException("Invalid ticker");
    }

    ObjectMapper m = new ObjectMapper();
    m.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    List<IexQuote> quoteArray = new ArrayList<>();
    for (String ticker : tickers) {
      try {
        quoteArray.add(m.readValue(IexQuotesJson.getString(ticker), IexQuote.class));
      } catch (IOException e) {
        logger.error("Couldn't unmarshal JSON to IexQuote", e);
      }
    }

    return quoteArray;
  }

  /**
   * Execute a get and return http entity/body as a string
   *
   * Tip: use EntityUtils.toString to process HTTP entity
   * @param url resource URL
   * @return http response body or Optional.empty for 404 reason
   * @throws DataRetrievalFailureException if HTTP failed or status code is unexpected
   */
  private Optional<String> executeHttpGet(String url) throws DataRetrievalFailureException {
    URI uri = null;
    try {
      uri = new URI(url);
    } catch (URISyntaxException e) {
      logger.error("Couldn't create URI with " + url, e);
    }
    HttpGet request = new HttpGet(uri);
    HttpResponse response = null;

    try {
      response = getHttpClient().execute(request);
    } catch (IOException e) {
      logger.error("Couldn't execute HTTP request", e);
    }

    String responseJsonStr = parseResponseBody(response);
    if (responseJsonStr == null)
      return Optional.empty();

    return Optional.of(responseJsonStr);
  }

  private String parseResponseBody(HttpResponse response) {

    //Check response status
    int status = response.getStatusLine().getStatusCode();

    if (status == HTTP_NOT_FOUND)
      return null;

    if (status != HTTP_OK) {
      try {
        System.out.println(EntityUtils.toString(response.getEntity()));
      } catch (IOException e) {
        System.out.println("Response has no entity");
      }
      throw new DataRetrievalFailureException("Unexpected HTTP status: " + status);
    }

    if (response.getEntity() == null)
      throw new DataRetrievalFailureException("Empty response body");

    //Convert Response entity to string
    String jsonStr;
    try {
      jsonStr = EntityUtils.toString(response.getEntity());
    } catch (IOException e) {
      throw new DataRetrievalFailureException("Failed to convert entity to String", e);
    }

    return jsonStr;
  }

  /**
   * Borrow a HTTP client from the httpClientConnectionManager
   * @return an httpClient
   */
  private CloseableHttpClient getHttpClient() {
    return HttpClients.custom()
        .setConnectionManager(httpClientConnectionManager)
        //prevent connectionManager shutdown when calling httpClient.close()
        .setConnectionManagerShared(true)
        .build();
  }

  @Override
  public long count() {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void deleteById(String s) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void delete(IexQuote iexQuote) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void deleteAll(Iterable<? extends IexQuote> iterable) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void deleteAll() {
    throw new UnsupportedOperationException("Not implemented");
  }
}

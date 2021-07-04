package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.MarketDataDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.dao.ResourceNotFoundException;
import ca.jrvs.apps.trading.model.domain.IexQuote;
import ca.jrvs.apps.trading.model.domain.Quote;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class QuoteService {

  private static final Logger logger = LoggerFactory.getLogger(QuoteService.class);

  private QuoteDao quoteDao;
  private MarketDataDao marketDataDao;

  @Autowired
  public QuoteService(QuoteDao quoteDao, MarketDataDao marketDataDao) {
    this.quoteDao = quoteDao;
    this.marketDataDao = marketDataDao;
  }

  /**
   * Update quote table against IEX source
   *  - get all quotes from the db
   *  - foreach ticker get iexQuote
   *  - convert iexQuote to quote entity
   *  - persist quote to db
   *
   * @throws ca.jrvs.apps.trading.dao.ResourceNotFoundException if ticker is not found from IEX
   * @throws org.springframework.dao.DataAccessException if unable to retrieve data
   * @throws IllegalArgumentException for invalid input
   */
  public List<Quote> updateMarketData() throws ResourceNotFoundException {
    List<Quote> quotes = quoteDao.findAll();
    Optional<IexQuote> iexQuote;
    Quote quote;
    for (int i = 0; i < quotes.size(); i++) {
      iexQuote = marketDataDao.findById(quotes.get(i).getId());
      if (!iexQuote.isPresent()) {
        logger.error("Ticker " + quotes.get(i).getId() +
            " was not found in IEX");
        throw new ResourceNotFoundException("Ticker " + quotes.get(i).getId() +
            " was not found in IEX");
      }
      quote = buildQuoteFromIexQuote(iexQuote.get());
      quotes.set(i, quote);
      quoteDao.save(quote);
    }
    return quotes;
  }

  /**
   * Helper method. Map an IexQuote to a Quote entity.
   * Note: `iexQuote.getLatestPrice() == null` if the stock market is closed.
   * Make sure to set a default value field(s).
   */
  protected static Quote buildQuoteFromIexQuote(IexQuote iexQuote) {
    Quote quote = new Quote();

    quote.setTicker(iexQuote.getSymbol());
    quote.setAskPrice(iexQuote.getIexAskPrice());
    quote.setAskSize((int)iexQuote.getIexAskSize());
    quote.setBidSize((int)iexQuote.getIexBidSize());
    quote.setId(iexQuote.getSymbol());
    quote.setLastPrice(iexQuote.getLatestPrice());
    quote.setBidPrice(iexQuote.getIexBidPrice());
    return quote;
  }

  /**
   * Validate (against IEX) and save given tickers to quote table.
   *
   *  - Get iexQuote(s)
   *  - convert each iexQuote to Quote entity
   *  - persist the quote to db
   *
   * @param tickers a list of tickers/symbols
   * @throws IllegalArgumentException if ticker is not found from IEX
   */
  public List<Quote> saveQuotes(List<String> tickers) {
    List<Quote> saved = new ArrayList<>();

    tickers.forEach(t -> saved.add(saveQuote(t)));

    return saved;
  }

  /**
   * Helper method
   */
  public Quote saveQuote(String ticker) {
    Optional<IexQuote> iexQuote;
    iexQuote = marketDataDao.findById(ticker);
    if (!iexQuote.isPresent()) {
      logger.error("Ticker " + ticker + " was not found in IEX");
      throw new IllegalArgumentException("Ticker " + ticker + " was not found in IEX");
    }

    return quoteDao.save(buildQuoteFromIexQuote(iexQuote.get()));
  }

  /**
   * Find an IexQuote
   *
   * @param ticker id
   * @return IexQuote object
   * @throws IllegalArgumentException if ticker is invalid
   */
  public IexQuote findIexQuoteByTicker(String ticker) {
    return marketDataDao.findById(ticker)
        .orElseThrow(() -> new IllegalArgumentException(ticker + " is invalid"));
  }

  /**
   * Update a given quote to quote table without validation
   * @param quote entity
   */
  public Quote saveQuote(Quote quote) {
    return quoteDao.save(quote);
  }

  /**
   * Find all quotes from the quote table
   * @return a list of quotes
   */
  public List<Quote> findAllQuotes() {
    return quoteDao.findAll();
  }
}

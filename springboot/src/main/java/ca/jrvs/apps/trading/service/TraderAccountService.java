package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.PositionDao;
import ca.jrvs.apps.trading.dao.SecurityOrderDao;
import ca.jrvs.apps.trading.dao.TraderDao;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.Trader;
import ca.jrvs.apps.trading.model.domain.TraderAccountView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TraderAccountService {

  private TraderDao traderDao;
  private AccountDao accountDao;
  private PositionDao positionDao;
  private SecurityOrderDao securityOrderDao;

  @Autowired
  public TraderAccountService(TraderDao traderDao, AccountDao accountDao,
      PositionDao positionDao, SecurityOrderDao securityOrderDao) {
    this.traderDao = traderDao;
    this.accountDao = accountDao;
    this.positionDao = positionDao;
    this.securityOrderDao = securityOrderDao;
  }

  /**
   * Create a new trader and initialize a new account with 0 amount.
   * - validate user input (all fields must be non empty)
   * - create a trader
   * - create an account
   * - create, setup, and return a new traderAccountView
   *
   * Assumption: to simplify the logic, each trader has only one account where traderId == accountId
   *
   * @param trader cannot be null. All fields cannot be null except for id (auto-generated by db)
   * @return traderAccountView
   * @throws IllegalArgumentException if a trader has null fields or id is not null.
   */
  public TraderAccountView createTraderAndAccount(Trader trader) {
    if (trader == null || trader.getFirstName() == null
        || trader.getFirstName().isEmpty() || trader.getLastName() == null
        || trader.getLastName().isEmpty() || trader.getCountry() == null
        || trader.getCountry().isEmpty() || trader.getDob() == null
        || trader.getEmail() == null || trader.getEmail().isEmpty())
      throw new IllegalArgumentException("Trader must have all fields defined");

    if (trader.getId() != null)
      throw new IllegalArgumentException("Trader ID is determined by the database - leave as null");

    int id = traderDao.save(trader).getId();
    Account account = new Account();
    account.setId(id);
    account.setAmount(0.0);
    account.setTraderId(id);
    accountDao.save(account);

    TraderAccountView traderAccountView = new TraderAccountView();
    traderAccountView.setAccount(account);
    traderAccountView.setTrader(trader);
    return traderAccountView;
  }

  /**
   * A trader can be deleted iff it has no open position and 0 cash balance
   *  - validate traderID
   *  - get trader account by traderId and check positions
   *  - get positions by accountId and check positions
   *  delete all securityOrders, account, trader (in this order)
   *
   * @param traderId must not be null
   * @throws IllegalArgumentException if traderId is null or not found or unable to delete
   */
  public void deleteTraderById(Integer traderId) {
    if (traderId == null)
      throw new IllegalArgumentException("traderId must not be null");
    if (!traderDao.existsById(traderId))
      throw new IllegalArgumentException("traderId does not exist");

    Account account = accountDao.findById(traderId).get();
    if (account.getAmount() != 0.0)
      throw new IllegalArgumentException("Cannot delete a trader owning an account with non-zero balance");

    if (positionDao.existsByAccountId(traderId))
      throw new IllegalArgumentException("Trader must not have any open positions");

    securityOrderDao.deleteByAccountId(traderId);
    accountDao.deleteById(traderId);
    traderDao.deleteById(traderId);
  }

  /**
   * Deposit a fund to an account by traderId
   * - validate user input
   * - account = accountDao.findByTraderId
   * - accountDao.updateAmountById
   *
   * @param traderId must not be null
   * @param fund must be greater than 0
   * @return updated account
   * @throws IllegalArgumentException if traderId is null or not found, and fund is less or equal to 0
   */
  public Account deposit(Integer traderId, Double fund) {
    if (traderId == null || fund <= 0 || !accountDao.existsById(traderId))
      throw new IllegalArgumentException("traderId must be valid and not null and fund must be positive");

    Account account = accountDao.findById(traderId).get();
    account.setAmount(account.getAmount() + fund);
    accountDao.updateBalance(traderId, account.getAmount());
    return account;
  }

  /**
   * Withdraw a fund to an account by traderId
   *
   * - validate user input
   * - account = accountDao.findByTraderId
   * - accountDao.updateAmountById
   *
   * @param traderId traderId
   * @param fund amount can't be 0
   * @return updated Account
   * @throws IllegalArgumentException if traderId is null or not found,
   * fund is less or equal to 0, and insufficient fund
   */
  public Account withdraw(Integer traderId, double fund) {
    if (traderId == null || fund <= 0 || !accountDao.existsById(traderId))
      throw new IllegalArgumentException("traderId must be valid and not null and fund must be positive");

    Account account = accountDao.findById(traderId).get();
    Double newBalance = account.getAmount() - fund;
    if (newBalance < 0)
      throw new IllegalArgumentException("Insufficient funds for withdrawal");

    account.setAmount(newBalance);
    accountDao.updateBalance(traderId, newBalance);
    return account;
  }
}
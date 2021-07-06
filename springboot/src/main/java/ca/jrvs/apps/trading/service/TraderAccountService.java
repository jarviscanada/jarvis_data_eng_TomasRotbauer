package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.PositionDao;
import ca.jrvs.apps.trading.dao.SecurityOrderDao;
import ca.jrvs.apps.trading.dao.TraderDao;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.Trader;
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

  }
}

package ca.jrvs.apps.trading;

import static java.lang.System.exit;

import ca.jrvs.apps.trading.controller.QuoteController;
import ca.jrvs.apps.trading.service.QuoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.stereotype.Component;

/**
 * For learning purposes, manually configure DataSource and JdbcTemplate
 */
@Component
@SpringBootApplication(exclude = {JdbcTemplateAutoConfiguration.class,
    DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class Application implements CommandLineRunner {

  private Logger logger = LoggerFactory.getLogger(Application.class);

  //@Value("${app.init.dailyList}")
  //private String[] initDailyList;

  private QuoteService quoteService;

  @Autowired
  public Application(QuoteService quoteService) {
    this.quoteService = quoteService;
  }

  public static void main(String args[]) throws Exception {
    SpringApplication app = new SpringApplication(Application.class);
    app.run(args);
    exit(0);
  }

  @Override
  public void run(String... args) throws Exception {
    if (args.length != 2)
      throw new IllegalArgumentException("USAGE: Application show ticker_id");
    if (args[0].equals("show")) {
      System.out.println(quoteService.findIexQuoteByTicker(args[1]));
    }
    else
      throw new IllegalArgumentException("Command " + args[1] + " not supported.");
    return;
  }
}

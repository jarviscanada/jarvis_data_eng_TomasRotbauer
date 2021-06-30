package ca.jrvs.apps.trading.model.domain;

public class Quote implements Entity<String> {

  private String ticker;
  private Double lastPrice;
  private Double listPrice;
  private Integer bidSize;
  private Double askPrice;
  private Integer askSize;

  @Override
  public String getId() {
    return ticker;
  }

  @Override
  public void setId(String s) {
    ticker = s;
  }
}

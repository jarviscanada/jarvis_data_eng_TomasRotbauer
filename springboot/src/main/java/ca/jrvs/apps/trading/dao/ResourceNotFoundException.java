package ca.jrvs.apps.trading.dao;

public class ResourceNotFoundException extends Exception {
  public ResourceNotFoundException(String errorMessage) {
    super(errorMessage);
  }
}

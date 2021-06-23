package ca.jrvs.apps.twitter.dao;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.example.JsonParser;
import ca.jrvs.apps.twitter.model.Tweet;
import com.google.gdata.util.common.base.PercentEscaper;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TwitterDao implements CrdDao<Tweet, String> {

  final Logger logger = LoggerFactory.getLogger(TwitterDao.class);

  //URI constants
  private static final String API_BASE_URI = "https://api.twitter.com";
  private static final String POST_PATH = "/1.1/statuses/update.json";
  private static final String SHOW_PATH = "/1.1/statuses/show.json";
  private static final String DELETE_PATH = "/1.1/statuses/destroy";
  //URI symbols
  private static final String QUERY_SYM = "?";
  private static final String AMPERSAND = "&";
  private static final String EQUAL = "=";

  //Response code
  private static final int HTTP_OK = 200;

  private final HttpHelper httpHelper;

  @Autowired
  public TwitterDao(HttpHelper httpHelper) {
    //Use default logger config
    BasicConfigurator.configure();
    this.httpHelper = httpHelper;
  }

  /**
   * Create an entity(Tweet) to the underlying storage
   *
   * @param entity entity that to be created
   * @return created entity
   */
  @Override
  public Tweet create(Tweet entity) {
    //Construct URI
    PercentEscaper percentEscaper = new PercentEscaper("", false);
    URI uri = null;
    try {
      uri = new URI(API_BASE_URI + POST_PATH + QUERY_SYM + "status" + EQUAL
      + percentEscaper.escape(entity.getText()) + AMPERSAND + "long" + EQUAL
      + entity.getCoordinates().getCoordinates().get(0).toString()
      + AMPERSAND + "lat" + EQUAL +
          entity.getCoordinates().getCoordinates().get(1).toString());
    } catch (URISyntaxException e) {
      logger.error("Couldn't create new URI object", e);
    }

    //Construct HTTP request
    HttpResponse response = httpHelper.httpPost(uri);

    //Validate response and convert response to Tweet object
    return parseResponseBody(response, HTTP_OK);
  }

  /**
   * Find an entity(Tweet) by its id
   *
   * @param l entity id
   * @return Tweet entity
   */
  @Override
  public Tweet findById(String l) {
    //Construct URI
    URI uri = null;
    try {
      uri = new URI(API_BASE_URI + SHOW_PATH + QUERY_SYM + "id" + EQUAL
          + l);
    } catch (URISyntaxException e) {
      logger.error("Couldn't create new URI object", e);
    }

    //Construct HTTP request
    HttpResponse response = httpHelper.httpGet(uri);

    //Validate response and convert response to Tweet object
    return parseResponseBody(response, HTTP_OK);
  }

  /**
   * Delete an entity(Tweet) by its ID
   *
   * @param l of the entity to be deleted
   * @return deleted entity
   */
  @Override
  public Tweet deleteById(String l) {
    //Construct URI
    URI uri = null;
    try {
      uri = new URI(API_BASE_URI + DELETE_PATH + "/" + l + ".json");
    } catch (URISyntaxException e) {
      logger.error("Couldn't create new URI object", e);
    }

    //Construct HTTP request
    HttpResponse response = httpHelper.httpPost(uri);

    //Validate response and convert response to Tweet object
    return parseResponseBody(response, HTTP_OK);
  }

  protected Tweet parseResponseBody(HttpResponse response, int expectedStatusCode) {
    Tweet tweet;

    //Check response status
    int status = response.getStatusLine().getStatusCode();
    if (status != expectedStatusCode) {
      try {
        System.out.println(EntityUtils.toString(response.getEntity()));
      } catch (IOException e) {
        System.out.println("Response has no entity");
      }
      throw new RuntimeException("Unexpected HTTP status: " + status);
    }

    if (response.getEntity() == null)
      throw new RuntimeException("Empty response body");

    //Convert Response entity to string
    String jsonStr;
    try {
      jsonStr = EntityUtils.toString(response.getEntity());
    } catch (IOException e) {
      throw new RuntimeException("Failed to convert entity to String", e);
    }

    //Convert JSON string to Tweet object
    try {
      tweet = JsonParser.toObjectFromJson(jsonStr, Tweet.class);
    } catch (IOException e) {
      throw new RuntimeException("Unable to convert JSON string to Object", e);
    }

    return tweet;
  }
}

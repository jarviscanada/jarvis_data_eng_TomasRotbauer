package ca.jrvs.apps.twitter.dao;

import static org.junit.Assert.*;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Coordinates;
import ca.jrvs.apps.twitter.model.Tweet;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TwitterDaoIntTest {

  private TwitterDao twitterDao;
  private HttpHelper httpHelper;
  private Tweet tweet;
  private static Random random = new Random();
  private static final String CONSUMER_KEY = System.getenv("consumerKey");
  private static final String CONSUMER_SECRET = System.getenv("consumerSecret");
  private static final String ACCESS_TOKEN = System.getenv("accessToken");
  private static final String TOKEN_SECRET = System.getenv("tokenSecret");

  @Before
  public void setUp() {
    httpHelper = new TwitterHttpHelper(CONSUMER_KEY, CONSUMER_SECRET, ACCESS_TOKEN, TOKEN_SECRET);
    twitterDao = new TwitterDao(httpHelper);
    tweet = new Tweet();

    tweet.setText(String.valueOf(random.nextLong()) + " #abc");
    Coordinates coordinates = new Coordinates();
    List<Float> l1 = new ArrayList<>();
    l1.add(1.0f);
    l1.add(-1.0f);
    coordinates.setCoordinates(l1);
    tweet.setCoordinates(coordinates);
  }

  @Test
  public void create() {
    Tweet response = twitterDao.create(tweet);
    assertEquals(tweet.getText(), response.getText());
    assertNotNull(response.getCoordinates());
    assertEquals(2, response.getCoordinates().getCoordinates().size());
    assertEquals(1.0, (float)response.getCoordinates().getCoordinates().get(0), 0.001);
    assertEquals(-1.0, (float)response.getCoordinates().getCoordinates().get(1), 0.001);
    assertTrue("#abc".contains(response.getEntities().getHashtags().get(0).getText()));
  }

  @Test
  public void findById() {
    Assert.assertEquals("1404868813666111491",
        twitterDao.findById("1404868813666111491").getId_str());
  }

  @Test
  public void deleteById() {
    String id_str = twitterDao.create(tweet).getId_str();
    Assert.assertEquals(id_str, twitterDao.deleteById(id_str).getId_str());
  }
}
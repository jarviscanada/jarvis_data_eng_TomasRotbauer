package ca.jrvs.apps.twitter.service;

import static org.junit.Assert.*;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Coordinates;
import ca.jrvs.apps.twitter.model.Tweet;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TwitterServiceIntTest {

  private static final String CONSUMER_KEY = System.getenv("consumerKey");
  private static final String CONSUMER_SECRET = System.getenv("consumerSecret");
  private static final String ACCESS_TOKEN = System.getenv("accessToken");
  private static final String TOKEN_SECRET = System.getenv("tokenSecret");

  private static final Tweet tweet = new Tweet();
  private HttpHelper httpHelper;
  private CrdDao<Tweet, String> dao;
  private TwitterService twitterService;

  @BeforeClass
  public static void setUpTweetCoordinates() {
    Coordinates coordinates = new Coordinates();
    List<Float> l1 = new ArrayList<>();
    l1.add(1.0f);
    l1.add(-1.0f);
    coordinates.setCoordinates(l1);
    tweet.setCoordinates(coordinates);
  }

  @Before
  public void setUp() {
    tweet.setText(String.valueOf(System.currentTimeMillis()) + " #abc");
    httpHelper =  new TwitterHttpHelper(CONSUMER_KEY, CONSUMER_SECRET, ACCESS_TOKEN, TOKEN_SECRET);
    dao = new TwitterDao(httpHelper);
    twitterService = new TwitterService(dao);
  }

  @Test
  public void postTweet() {
    Tweet response = twitterService.postTweet(tweet);
    assertEquals(tweet.getText(), response.getText());
    assertNotNull(response.getCoordinates());
    assertEquals(2, response.getCoordinates().getCoordinates().size());
    assertEquals(1.0, (float)response.getCoordinates().getCoordinates().get(0), 0.001);
    assertEquals(-1.0, (float)response.getCoordinates().getCoordinates().get(1), 0.001);
    assertTrue("#abc".contains(response.getEntities().getHashtags().get(0).getText()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void postTweetBadCoordinates() {
    Coordinates coordinates = new Coordinates();
    List<Float> l1 = new ArrayList<>();
    l1.add(200.0f);
    l1.add(-200.0f);
    coordinates.setCoordinates(l1);
    tweet.setCoordinates(coordinates);
    twitterService.postTweet(tweet);
  }

  @Test
  public void showTweet() {
    String[] fields = {"id_str"};
    Tweet response = twitterService.showTweet("1404868813666111491", fields);
    assertNull(response.getText());
    assertEquals("1404868813666111491", response.getId_str());
  }

  @Test
  public void deleteTweets() {
    String[] ids = new String[5];
    for (int i = 0; i < 5; i++) {
      tweet.setText(String.valueOf(System.currentTimeMillis()) + " #abc");
      ids[i] = twitterService.postTweet(tweet).getId_str();
    }
    List<Tweet> responses = twitterService.deleteTweets(ids);
    assertNotNull(responses);
    assertEquals(5, responses.size());
    for (int i = 0; i < responses.size(); i++)
      assertEquals(ids[i], responses.get(i).getId_str());

  }
}
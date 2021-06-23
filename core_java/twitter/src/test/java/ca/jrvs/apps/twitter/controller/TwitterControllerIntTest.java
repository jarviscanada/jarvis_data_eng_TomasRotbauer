package ca.jrvs.apps.twitter.controller;

import static org.junit.Assert.*;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Coordinates;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.TwitterService;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class TwitterControllerIntTest {

  private static final String CONSUMER_KEY = System.getenv("consumerKey");
  private static final String CONSUMER_SECRET = System.getenv("consumerSecret");
  private static final String ACCESS_TOKEN = System.getenv("accessToken");
  private static final String TOKEN_SECRET = System.getenv("tokenSecret");

  private static final Tweet tweet = new Tweet();
  private HttpHelper httpHelper;
  private CrdDao<Tweet, String> dao;
  private TwitterService twitterService;
  private TwitterController twitterController;

  @Before
  public void setUp() {
    Coordinates coordinates = new Coordinates();
    List<Float> l1 = new ArrayList<>();
    l1.add(1.0f);
    l1.add(-1.0f);
    coordinates.setCoordinates(l1);
    tweet.setCoordinates(coordinates);
    tweet.setText(System.currentTimeMillis() + " #abc");
    httpHelper =  new TwitterHttpHelper(CONSUMER_KEY, CONSUMER_SECRET, ACCESS_TOKEN, TOKEN_SECRET);
    dao = new TwitterDao(httpHelper);
    twitterService = new TwitterService(dao);
    twitterController = new TwitterController(twitterService);
  }

  @Test
  public void postTweet() {
    String[] args = {"post", "Test post" + System.currentTimeMillis() +  " #abc", "1.0:-1.0"};
    Tweet response = twitterController.postTweet(args);
    assertEquals(args[1], response.getText());
    assertNotNull(response.getCoordinates());
    assertEquals(2, response.getCoordinates().getCoordinates().size());
    assertEquals(1.0, response.getCoordinates().getCoordinates().get(0), 0.001);
    assertEquals(-1.0, response.getCoordinates().getCoordinates().get(1), 0.001);
    assertTrue("#abc".contains(response.getEntities().getHashtags().get(0).getText()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void postTweetBadInput() {
    String[] args = {"post", "Test post #abc", "1.0"};
    twitterController.postTweet(args);
  }

  @Test
  public void showTweet() {
    String[] args = {"show", "1404868813666111491", "id_str", "text", "created_at", "coordinates"};
    Tweet response = twitterController.showTweet(args);
    assertNotNull(response);
    assertNull(response.getFavorite_count());
    assertEquals("1404868813666111491", response.getId_str());
  }

  @Test(expected = IllegalArgumentException.class)
  public void showTweetBadInput() {
    String[] args = {"show"};
    twitterController.showTweet(args);
  }

  @Test
  public void deleteTweet() {
    String[] args = new String[6];
    args[0] = "delete";
    for (int i = 1; i < 6; i++) {
      tweet.setText(System.currentTimeMillis() + " #abc");
      args[i] = twitterService.postTweet(tweet).getId_str();
    }
    List<Tweet> responses = twitterController.deleteTweet(args);
    assertNotNull(responses);
    assertEquals(5, responses.size());
    for (int i = 0; i < responses.size(); i++)
      assertEquals(args[i+1], responses.get(i).getId_str());
  }

  @Test(expected = IllegalArgumentException.class)
  public void deleteTweetBadInput() {
    String[] args = {"delete"};
    twitterController.deleteTweet(args);
  }

}
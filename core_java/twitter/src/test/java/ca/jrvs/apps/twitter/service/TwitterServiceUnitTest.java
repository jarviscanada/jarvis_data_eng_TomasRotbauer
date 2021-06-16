package ca.jrvs.apps.twitter.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.model.Coordinates;
import ca.jrvs.apps.twitter.model.Entities;
import ca.jrvs.apps.twitter.model.Tweet;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TwitterServiceUnitTest {

  private static Tweet tweet = new Tweet();

  @Mock
  CrdDao dao;

  @InjectMocks
  TwitterService twitterService;

  @BeforeClass
  public static void setUpTweet() {
    Coordinates coordinates = new Coordinates();
    List<Float> l1 = new ArrayList<>();
    l1.add(1.0f);
    l1.add(-1.0f);
    coordinates.setCoordinates(l1);
    tweet.setCoordinates(coordinates);
    tweet.setId_str("123456789");
    tweet.setRetweeted(false);
    tweet.setFavorited(true);
    tweet.setFavorite_count(4);
    tweet.setEntities(new Entities());
    tweet.setRetweet_count(0);
    tweet.setId(12456789L);
  }

  @Before
  public void setUp() {
    tweet.setText(String.valueOf(System.currentTimeMillis()) + " #abc");
  }

  @Test(expected = IllegalArgumentException.class)
  public void postTweetTooLong() {
    tweet.setText("Character Counter - Character Count "
        + "Tool is a free character counter tool that provides instant character count "
        + "& word count statistics for a given text.");
    twitterService.postTweet(tweet);
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
  public void postTweet() {
    when(dao.create(any())).thenReturn(tweet);
    assertNotNull(twitterService.postTweet(tweet));
  }

  @Test(expected = IllegalArgumentException.class)
  public void showTweetIdInvalid() {
    String[] fields = {"id"};
    twitterService.showTweet("123456789123456789123", fields);
  }

  @Test
  public void showTweet() {
    when(dao.findById(any())).thenReturn(tweet);
    String[] fields = {"id"};
    Tweet response = twitterService.showTweet("123456789", fields);
    assertNotNull(response);
    assertNotNull(response.getId());
    assertNull(response.getCoordinates());
  }

  @Test(expected = IllegalArgumentException.class)
  public void deleteTweetsIdInvalid() {
    String[] ids = {"123456789123456789123"};
    twitterService.deleteTweets(ids);
  }

  @Test
  public void deleteTweets() {
    when(dao.deleteById(any())).thenReturn(new Tweet());
    String[] ids = {"12345678912345678912"};
    List<Tweet> responses = twitterService.deleteTweets(ids);
    assertNotNull(responses);
  }

}

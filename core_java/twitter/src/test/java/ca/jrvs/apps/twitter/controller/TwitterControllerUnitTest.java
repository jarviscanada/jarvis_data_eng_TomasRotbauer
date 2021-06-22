package ca.jrvs.apps.twitter.controller;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.twitter.model.Coordinates;
import ca.jrvs.apps.twitter.model.Entities;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.Service;
import java.util.ArrayList;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TwitterControllerUnitTest {

  private static Tweet tweet = new Tweet();

  @Mock
  Service service;

  @InjectMocks
  TwitterController twitterController;

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

  @Test
  public void postTweet() {
    when(service.postTweet(any())).thenReturn(tweet);
    String[] args = {"post", "test post" + System.currentTimeMillis()};
    Tweet response = twitterController.postTweet(args);
    assertNotNull(response);
  }

  @Test(expected = IllegalArgumentException.class)
  public void postTweetBadInput() {
    String[] args = {"post", "Test post #abc", "1.0"};
    twitterController.postTweet(args);
  }

  @Test
  public void showTweet() {
    when(service.showTweet(any(), any())).thenReturn(tweet);
    String[] args = {"show", "73", "id"};
    Tweet response = twitterController.showTweet(args);
    assertNotNull(response);
  }

  @Test(expected = IllegalArgumentException.class)
  public void showTweetBadInput() {
    String[] args = {"show"};
    twitterController.showTweet(args);
  }

  @Test
  public void deleteTweet() {
    when(service.deleteTweets(any())).thenReturn(new ArrayList<Tweet>());
    String[] args = {"delete", "73", "74", "75"};
    List<Tweet> response = twitterController.deleteTweet(args);
    assertNotNull(response);
  }

  @Test(expected = IllegalArgumentException.class)
  public void deleteTweetBadInput() {
    String[] args = {"delete"};
    twitterController.deleteTweet(args);
  }
}

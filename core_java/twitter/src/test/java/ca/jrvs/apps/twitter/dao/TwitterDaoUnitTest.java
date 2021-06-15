package ca.jrvs.apps.twitter.dao;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.example.JsonParser;
import ca.jrvs.apps.twitter.model.Coordinates;
import ca.jrvs.apps.twitter.model.Tweet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TwitterDaoUnitTest {

  @Mock
  HttpHelper mockHelper;

  @InjectMocks
  TwitterDao dao;

  private TwitterDao twitterDao;
  private HttpHelper httpHelper;
  private Tweet tweet;
  private static Random random = new Random();
  private static final String CONSUMER_KEY = System.getenv("consumerKey");
  private static final String CONSUMER_SECRET = System.getenv("consumerSecret");
  private static final String ACCESS_TOKEN = System.getenv("accessToken");
  private static final String TOKEN_SECRET = System.getenv("tokenSecret");

  //Test happy path
  //however, we don't want to call parseResponseBody.
  //we will make a spyDao which can fake parseResponseBody return value
  private static final String tweetJsonStr = "{\n"
      + "   \"created_at\":\"Mon Feb 18 21:24:39 +0000 2019\",\n"
      + "   \"id\":1097607853932564480,\n"
      + "   \"id_str\":\"1097607853932564480\",\n"
      + "   \"text\":\"test with loc223\",\n"
      + "   \"entities\":{\n"
      + "      \"hashtags\":[\n"
      + "         {\n"
      + "            \"text\":\"documentation\",\n"
      + "            \"indices\":[\n"
      + "               211,\n"
      + "               225\n"
      + "            ]\n"
      + "         },\n"
      + "         {\n"
      + "            \"text\":\"parsingJSON\",\n"
      + "            \"indices\":[\n"
      + "               226,\n"
      + "               238\n"
      + "            ]\n"
      + "         },\n"
      + "         {\n"
      + "            \"text\":\"GeoTagged\",\n"
      + "            \"indices\":[\n"
      + "               239,\n"
      + "               249\n"
      + "            ]\n"
      + "         }\n"
      + "      ],\n"
      + "      \"user_mentions\":[\n"
      + "         {\n"
      + "            \"name\":\"Twitter API\",\n"
      + "            \"indices\":[\n"
      + "               4,\n"
      + "               15\n"
      + "            ],\n"
      + "            \"screen_name\":\"twitterapi\",\n"
      + "            \"id\":6253282,\n"
      + "            \"id_str\":\"6253282\"\n"
      + "         }\n"
      + "      ]\n"
      + "   },\n"
      + "   \"coordinates\":{\n"
      + "      \"coordinates\":[\n"
      + "         -75.14310264,\n"
      + "         40.05701649\n"
      + "      ],\n"
      + "      \"type\":\"Point\"\n"
      + "   },\n"
      + "   \"retweet_count\":0,\n"
      + "   \"favorite_count\":0,\n"
      + "   \"favorited\":false,\n"
      + "   \"retweeted\":false\n"
      + "}";

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
    when(mockHelper.httpPost(isNotNull())).thenThrow(new RuntimeException("mock"));
    try {
      dao.create(tweet);
      fail();
    } catch (RuntimeException e) {
      assertTrue(true);
    }

    when(mockHelper.httpPost(isNotNull())).thenReturn(null);
    TwitterDao spyDao = Mockito.spy(dao);
    Tweet expectedTweet = null;
    try {
      expectedTweet = JsonParser.toObjectFromJson(tweetJsonStr, Tweet.class);
    } catch (IOException e) {
      e.printStackTrace();
    }
    //mock parseResponseBody
    doReturn(expectedTweet).when(spyDao).parseResponseBody(any(), anyInt());
    Tweet response = spyDao.create(tweet);
    assertNotNull(response);
    assertNotNull(response.getText());
  }

  @Test
  public void findById() {

  }

  @Test
  public void deleteById() {
  }
}
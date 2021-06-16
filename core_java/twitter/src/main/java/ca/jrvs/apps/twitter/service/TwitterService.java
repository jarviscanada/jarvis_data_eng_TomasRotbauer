package ca.jrvs.apps.twitter.service;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.model.Tweet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TwitterService implements Service {

  private CrdDao dao;

  //@Autowired
  public TwitterService(CrdDao dao) {
    this.dao = dao;
  }

  /**
   * Validate and post a user input Tweet
   *
   * @param tweet tweet to be created
   * @return created tweet
   * @throws IllegalArgumentException if text exceed max number of allowed characters or lat/long
   *                                  out of range
   */
  @Override
  public Tweet postTweet(Tweet tweet) throws IllegalArgumentException {
    validatePostTweet(tweet);

    return (Tweet) dao.create(tweet);
  }

  /**
   * Search a tweet by ID
   *
   * @param id     tweet id
   * @param fields set fields not in the list to null
   * @return Tweet object which is returned by the Twitter API
   * @throws IllegalArgumentException if id or fields param is invalid
   */
  @Override
  public Tweet showTweet(String id, String[] fields) throws IllegalArgumentException {
    validateTweetId(id);
    Tweet response = (Tweet) dao.findById(id);
    Tweet retVal = new Tweet();

    for (String field : fields)
      if (field.equals("created_at"))
        retVal.setCreated_at(response.getCreated_at());
      else if (field.equals("id"))
        retVal.setId(response.getId());
      else if (field.equals("id_str"))
        retVal.setId_str(response.getId_str());
      else if (field.equals("text"))
        retVal.setText(response.getText());
      else if (field.equals("entities"))
        retVal.setEntities(response.getEntities());
      else if (field.equals("coordinates"))
        retVal.setCoordinates(response.getCoordinates());
      else if (field.equals("retweet_count"))
        retVal.setRetweet_count(response.getRetweet_count());
      else if (field.equals("favorite_count"))
        retVal.setFavorite_count(response.getFavorite_count());
      else if (field.equals("favorited"))
        retVal.setFavorited(response.getFavorited());
      else if (field.equals("retweeted"))
        retVal.setRetweeted(response.getRetweeted());
      else
        throw new IllegalArgumentException("Invalid field: " + "\"" + field + "\"");

    return retVal;
  }

  /**
   * Delete Tweet(s) by id(s).
   *
   * @param ids tweet IDs which will be deleted
   * @return A list of Tweets
   * @throws IllegalArgumentException if one of the IDs is invalid.
   */
  @Override
  public List<Tweet> deleteTweets(String[] ids) throws IllegalArgumentException {
    List<Tweet> retVal = new ArrayList<>();
    Arrays.stream(ids).forEach(id -> {
      validateTweetId(id);
      retVal.add((Tweet)dao.deleteById(id));
    });
    return retVal;
  }

  private void validatePostTweet(Tweet tweet) {
    if (tweet.getText().length() > 140)
      throw new IllegalArgumentException("Tweet text must not exceed 140 characters");

    if (tweet.getCoordinates() != null) {
      if (tweet.getCoordinates().getCoordinates().size() != 2)
        throw new IllegalArgumentException("Coordinates must contain exactly two values");

      float lon = tweet.getCoordinates().getCoordinates().get(0);
      float lat = tweet.getCoordinates().getCoordinates().get(1);
      if (lon < -180.0 || lon > 180.0)
        throw new IllegalArgumentException("Longitude value is out of range");
      if (lat < -90.0 || lat > 90.0)
        throw new IllegalArgumentException("Latitude value is out of range");
    }
  }

  private void validateTweetId(String id) {
    if (id.length() > 20)
      throw new IllegalArgumentException("Tweet id too long");
  }
}

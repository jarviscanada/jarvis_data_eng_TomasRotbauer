package ca.jrvs.apps.twitter.controller;

import ca.jrvs.apps.twitter.model.Coordinates;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.Service;
import java.util.ArrayList;
import java.util.List;

public class TwitterController implements Controller {

  private static final String COORD_SEP = ":";
  private static final String COMMA = ",";
  private static final String POST_USAGE = "Usage: TwitterCLIApp post \"tweet text\""
      + " [\"longitude:latitude\"]";
  private static final String SHOW_USAGE = "Usage: TwitterCLIApp show tweet_id";
  private static final String DELETE_USAGE = "Usage: TwitterCLIApp delete tweet_id1 [tweet_id2 ...]";

  private Service service;

  //@Autowired
  public TwitterController(Service service) {
    this.service = service;
  }

  /**
   * Parse user argument and post a tweet by calling service classes
   *
   * @param args
   * @return a posted tweet
   * @throws IllegalArgumentException if args are invalid
   */
  @Override
  public Tweet postTweet(String[] args) {
    if (args.length < 2 || args.length > 3)
      throw new IllegalArgumentException(POST_USAGE);

    String tweet_text = args[1];
    Tweet post = new Tweet();

    if (args.length == 3) {
      Coordinates coordinates = new Coordinates();
      List<Float> points = new ArrayList<>();
      String[] long_lat = args[2].split(COORD_SEP);

      if (long_lat.length != 2)
        throw new IllegalArgumentException("Two coordinates are needed.\n" + POST_USAGE);

      try {
        points.add(Float.parseFloat(long_lat[0]));
        points.add(Float.parseFloat(long_lat[1]));
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException("Coordinates need to be real numbers.\n"
                    + POST_USAGE, e);
      }

      coordinates.setCoordinates(points);
      coordinates.setType("Point");
      post.setCoordinates(coordinates);
    }

    if (tweet_text.isEmpty())
      throw new IllegalArgumentException("Tweet text cannot be empty.\n" + POST_USAGE);

    post.setText(tweet_text);
    return service.postTweet(post);
  }

  /**
   * Parse user argument and search a tweet by calling service classes
   *
   * @param args
   * @return a tweet
   * @throws IllegalArgumentException if args are invalid
   */
  @Override
  public Tweet showTweet(String[] args) {
    return null;
  }

  /**
   * Parse user argument and delete tweets by calling service classes
   *
   * @param args
   * @return a list of deleted tweets
   * @throws IllegalArgumentException if args are invalid
   */
  @Override
  public List<Tweet> deleteTweet(String[] args) {
    return null;
  }
}

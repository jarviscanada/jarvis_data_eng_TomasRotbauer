package ca.jrvs.apps.twitter;

import ca.jrvs.apps.twitter.controller.Controller;
import ca.jrvs.apps.twitter.controller.TwitterController;
import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.example.JsonParser;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.Service;
import ca.jrvs.apps.twitter.service.TwitterService;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.ArrayList;
import java.util.List;

public class TwitterCLIApp {
  public static final String USAGE = "Usage: TwitterCLIApp <post | show | delete> [options]";

  private static final String CONSUMER_KEY = System.getenv("consumerKey");
  private static final String CONSUMER_SECRET = System.getenv("consumerSecret");
  private static final String ACCESS_TOKEN = System.getenv("accessToken");
  private static final String TOKEN_SECRET = System.getenv("tokenSecret");

  private static Controller controller;

  public static void main(String[] args) throws IllegalArgumentException {
    TwitterCLIApp app = new TwitterCLIApp();

    HttpHelper httpHelper = new TwitterHttpHelper(CONSUMER_KEY, CONSUMER_SECRET,
        ACCESS_TOKEN, TOKEN_SECRET);
    CrdDao<Tweet, String> dao = new TwitterDao(httpHelper);
    Service service = new TwitterService(dao);
    controller = new TwitterController(service);

    app.run(args);
  }

  public void run(String[] args) {
    if (args.length == 0)
      throw new IllegalArgumentException(USAGE);

    String command = args[0];
    List<Tweet> response;

    if (command.equals("post")) {
      response = new ArrayList<>();
      response.add(controller.postTweet(args));
    }
    else if (command.equals("show")) {
      response = new ArrayList<>();
      response.add(controller.showTweet(args));
    }
    else if (command.equals("delete"))
      response = controller.deleteTweet(args);
    else
      throw new IllegalArgumentException(USAGE);

    response.forEach(t -> {
      try {
        System.out.println(JsonParser.toJson(t, true, true));
      } catch (JsonProcessingException e) {
        e.printStackTrace();
      }
    });

  }
}

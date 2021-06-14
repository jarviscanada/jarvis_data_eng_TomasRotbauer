package ca.jrvs.apps.twitter;

import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import com.google.gdata.util.common.base.PercentEscaper;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

public class TwitterCLIApp {

  private static final String CONSUMER_KEY = System.getenv("consumerKey");
  private static final String CONSUMER_SECRET = System.getenv("consumerSecret");
  private static final String ACCESS_TOKEN = System.getenv("accessToken");
  private static final String TOKEN_SECRET = System.getenv("tokenSecret");

  public static void main(String[] args) throws URISyntaxException, IOException {
    TwitterHttpHelper httpHelper = new TwitterHttpHelper(CONSUMER_KEY, CONSUMER_SECRET,
        ACCESS_TOKEN, TOKEN_SECRET);

    URI uri = new URI("https://api.twitter.com/1.1/statuses/show.json?id=1404474031647215621");

    HttpResponse response = httpHelper.httpGet(uri);
    System.out.println(EntityUtils.toString(response.getEntity()));
  }
}

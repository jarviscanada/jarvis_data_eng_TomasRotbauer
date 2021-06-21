package ca.jrvs.apps.twitter.dao.helper;

import java.io.IOException;
import java.net.URI;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Component;

@Component
public class TwitterHttpHelper implements HttpHelper {

  /**
   * Dependencies are specified as private member variables
   */
  private final OAuthConsumer consumer;
  private final HttpClient httpClient;

  /**
   * Constructor
   * Setup dependencies using secrets
   *
   * @param consumerKey
   * @param consumerSecret
   * @param accessToken
   * @param tokenSecret
   */
  public TwitterHttpHelper(String consumerKey, String consumerSecret, String accessToken,
      String tokenSecret) {
    consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
    consumer.setTokenWithSecret(accessToken, tokenSecret);
    httpClient = HttpClientBuilder.create().build();
  }

  /**
   * Default constructor (not used for now)
   */
  public TwitterHttpHelper() {
    String consumerKey = System.getenv("consumerKey");
    String consumerSecret = System.getenv("consumerSecret");
    String accessToken = System.getenv("accessToken");
    String tokenSecret = System.getenv("tokenSecret");
    consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
    consumer.setTokenWithSecret(accessToken, tokenSecret);
    httpClient = HttpClientBuilder.create().build();
  }

  /**
   * Execute a HTTP Post call
   *
   * @param uri
   * @return
   */
  @Override
  public HttpResponse httpPost(URI uri) {
    HttpPost request = new HttpPost(uri);
    return sendRequest(request);
  }

  /**
   * Execute a HTTP Get call
   *
   * @param uri
   * @return
   */
  @Override
  public HttpResponse httpGet(URI uri) {
    HttpGet request = new HttpGet(uri);
    return sendRequest(request);
  }

  private <T> HttpResponse sendRequest(T request) {
    HttpResponse response = null;

    try {
      consumer.sign(request);
    } catch (OAuthMessageSignerException | OAuthExpectationFailedException
        | OAuthCommunicationException e) {
      e.printStackTrace();
    }

    try {
      response = httpClient.execute((HttpUriRequest) request);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return response;
  }
}
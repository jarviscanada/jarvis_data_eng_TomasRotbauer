package ca.jrvs.apps.twitter.model;

import java.util.List;

public class Entities {

  private List<Hashtag> hashtags;
  private List<UserMentions> user_mentions;

  public List<Hashtag> getHashtags() {
    return hashtags;
  }

  public void setHashtags(List<Hashtag> hashtags) {
    this.hashtags = hashtags;
  }

  public List<UserMentions> getUser_mentions() {
    return user_mentions;
  }

  public void setUser_mentions(List<UserMentions> user_mentions) {
    this.user_mentions = user_mentions;
  }

}

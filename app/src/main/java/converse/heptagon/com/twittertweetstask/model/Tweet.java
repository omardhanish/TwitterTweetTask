package converse.heptagon.com.twittertweetstask.model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import converse.heptagon.com.twittertweetstask.network.model.TweetStatuses;

public class Tweet implements Serializable {
    private final String username;
    private final String content;
    private final Date createdAt;
    private final String imageUrl;

    public Tweet(String username, String content, Date createdAt, String imageUrl) {
        this.username = username;
        this.content = content;
        this.createdAt = createdAt;
        this.imageUrl = imageUrl;
    }

    public static List<Tweet> buildTweets(TweetStatuses statuses) {
        List<Tweet> tweetList = new ArrayList<>();
        for (TweetStatuses.Status status : statuses.getStatuses()) {
            String content = status.getText();
            String username = status.getUserName();
            Date createdAt = status.getCreatedAt();
            String imageUrl = status.getImageUrl();
            tweetList.add(new Tweet(username, content, createdAt, imageUrl));
        }
        return tweetList;
    }

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}

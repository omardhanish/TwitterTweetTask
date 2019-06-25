package converse.heptagon.com.twittertweetstask.repository;

import java.util.List;

import converse.heptagon.com.twittertweetstask.model.Tweet;
import retrofit2.Call;

public interface TwittertRepo {

    void getTweetList(String query, RepoCallback<List<Tweet>> callbac);

    void callRequestAccessAndTweetList(String query, RepoCallback<List<Tweet>> callback);

    void requestAccessToken(RepoCallback<Void> callback);

    void doGetTweetList(final String query, final RepoCallback<List<Tweet>> callback);

    void cancel();

    void cacelSafe(Call call);

    void removeAccessToken();

    void saveAccessToken(String accessToken);

    String getAccessToken();

    String getHeader();

}

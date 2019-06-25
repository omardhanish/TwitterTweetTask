package converse.heptagon.com.twittertweetstask.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import converse.heptagon.com.twittertweetstask.model.Tweet;
import converse.heptagon.com.twittertweetstask.network.DateDeserializer;
import converse.heptagon.com.twittertweetstask.network.TweetApi;
import converse.heptagon.com.twittertweetstask.network.model.Oauth2Token;
import converse.heptagon.com.twittertweetstask.network.model.TweetStatuses;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TwitterRepository implements TwittertRepo
{

    private static final String SP_TWITTER_REPOSITORY = "TWITTER_TASK";
    private static final String SP_KEY_ACCESS_TOKEN = "ACCESS_TOKEN";

    private final TweetApi mTwitterApi;
    private final SharedPreferences mSharedPreferences;
    private final String mConsumerKey;
    private final String mConsumerSecret;

    private Call<TweetStatuses> mStatusesCall;
    private Call<Oauth2Token> mTokenCall;

    public TwitterRepository(Context context, String baseUrl, String consumerKey, String consumerSecret) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        mTwitterApi = retrofit.create(TweetApi.class);

        mSharedPreferences = context.getSharedPreferences(SP_TWITTER_REPOSITORY, Context.MODE_PRIVATE);

        mConsumerKey = consumerKey;
        mConsumerSecret = consumerSecret;
    }

    @Override
    public void getTweetList(String query, RepoCallback<List<Tweet>> callback) {
        String accessToken = getAccessToken();
        if (TextUtils.isEmpty(accessToken)) {
            callRequestAccessAndTweetList(query, callback);
        } else {
            doGetTweetList(query, callback);
        }
    }

    @Override
    public void callRequestAccessAndTweetList(final String query,final RepoCallback<List<Tweet>> callback) {
        requestAccessToken(new RepoCallback<Void>() {
            @Override
            public void onSuccess(Void object) {
                doGetTweetList(query, callback);
            }

            @Override
            public void onFailure(Throwable error) {
                if (callback != null) {
                    callback.onFailure(new RuntimeException("Failed to authenticate"));
                }
            }
        });
    }

    @Override
    public void requestAccessToken(final RepoCallback<Void> callback) {
        cacelSafe(mTokenCall);
        mTokenCall = mTwitterApi.getAccessToken(getHeader(), "client_credentials");
        mTokenCall.enqueue(new Callback<Oauth2Token>() {
            @Override
            public void onResponse(Call<Oauth2Token> call, Response<Oauth2Token> response) {
                if (callback != null) {
                    if (response.isSuccessful()) {
                        saveAccessToken(response.body().getAccessToken());
                        callback.onSuccess(null);
                    }
                }
            }

            @Override
            public void onFailure(Call<Oauth2Token> call, Throwable t) {
                if (callback != null) {
                    callback.onFailure(t);
                }
            }
        });
    }

    @Override
    public void doGetTweetList(final String query,final RepoCallback<List<Tweet>> callback)
    {
        cacelSafe(mStatusesCall);
        mStatusesCall = mTwitterApi.getStatuses(getAccessToken(), query);
        mStatusesCall.enqueue(new Callback<TweetStatuses>() {
            @Override
            public void onResponse(Call<TweetStatuses> call, Response<TweetStatuses> response) {
                if (callback != null) {
                    if (response.isSuccessful()) {
                        callback.onSuccess(Tweet.buildTweets(response.body()));
                    } else {

                        callback.onFailure(new RuntimeException("Failed to obtain statuses"));
                    }
                }
            }

            @Override
            public void onFailure(Call<TweetStatuses> call, Throwable t) {
                if (callback != null) {
                    callback.onFailure(t);
                }
            }
        });
    }

    @Override
    public void cancel() {
        cacelSafe(mStatusesCall);
        cacelSafe(mTokenCall);
    }

    @Override
    public void cacelSafe(Call call) {
        if (call != null && !call.isCanceled()) {
            call.cancel();
        }
    }

    @Override
    public void removeAccessToken() {
        mSharedPreferences.edit().remove(SP_KEY_ACCESS_TOKEN).apply();
    }

    @Override
    public void saveAccessToken(String accessToken) {
        mSharedPreferences.edit().putString(SP_KEY_ACCESS_TOKEN, accessToken).apply();
    }

    @Override
    public String getAccessToken() {
        String accessToken = mSharedPreferences.getString(SP_KEY_ACCESS_TOKEN, null);
        if (TextUtils.isEmpty(accessToken)) {
            return null;
        }
        return "Bearer " + accessToken;
    }

    @Override
    public String getHeader() {
        try {
            String consumerKeyAndSecret = mConsumerKey + ":" + mConsumerSecret;
            byte[] data = consumerKeyAndSecret.getBytes("UTF-8");
            String base64 = Base64.encodeToString(data, Base64.NO_WRAP);

            return "Basic " + base64;
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
}

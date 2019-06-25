package converse.heptagon.com.twittertweetstask;

import android.app.Application;

import converse.heptagon.com.twittertweetstask.repository.TwitterRepository;

public class BaseApplication extends Application {

    private TwitterRepository mTwitterRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        mTwitterRepository = new TwitterRepository(this,
                getString(R.string.url),
                getString(R.string.key_consumer),
                getString(R.string.key_consumer_secret));
    }

    public TwitterRepository getRepository() {
        return mTwitterRepository;
    }
}

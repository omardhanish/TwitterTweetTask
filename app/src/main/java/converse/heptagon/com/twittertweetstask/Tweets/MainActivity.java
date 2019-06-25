package converse.heptagon.com.twittertweetstask.Tweets;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import converse.heptagon.com.twittertweetstask.R;
import converse.heptagon.com.twittertweetstask.model.Tweet;
import converse.heptagon.com.twittertweetstask.model.TweetDateFormatter;
import converse.heptagon.com.twittertweetstask.view.MyProgressDialog;

public class MainActivity extends AppCompatActivity
{
    private ViewModelMainActivity mModel;
    private RecyclerView mRecyclerView;
    private AdapterTweets mAdapterTweets;
    private List<Tweet> mTweetList = new ArrayList<>();
    private String mSearchData;
    private Handler mHandler;
    private static final int UPDATE_TIME = 10000;
    private Runnable mRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHandler = new Handler();
        mModel = ViewModelProviders.of(this).get(ViewModelMainActivity.class);
        initViews();

        mAdapterTweets = new AdapterTweets(this , new TweetDateFormatter() , mTweetList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapterTweets);

        startObserving();
    }

    private void initViews(){
        mRecyclerView = findViewById(R.id.recyclerView);
    }

    private void startObserving()
    {
        final Observer<Bundle> nameObserver = new Observer<Bundle>() {
            @Override
            public void onChanged(@Nullable final Bundle bundleEvent) {
                if (bundleEvent != null) {
                    if (bundleEvent.containsKey(ViewModelMainActivity.EVENT)) {
                        switch (bundleEvent.getString(ViewModelMainActivity.EVENT)) {
                            case "SHOW_LOADER":
                                MyProgressDialog.show(MainActivity.this , null);
                                break;
                            case "HIDE_LOADER":
                                MyProgressDialog.dismiss();
                                break;
                            case "ERROR_LOADER":
                                Snackbar.make(findViewById(android.R.id.content), getString(R.string.something_went_wrong),Snackbar.LENGTH_SHORT).show();
                                break;
                            case "EMPTY_DATA":
                                mTweetList.clear();
                                Snackbar.make(findViewById(android.R.id.content),"Your Text Here",Snackbar.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }
            }
        };

        mModel.getBundleEvent().observe(this, nameObserver);

        final Observer<List<Tweet>> loadListObservor = new Observer<List<Tweet>>() {
            @Override
            public void onChanged(@Nullable List<Tweet> tweets) {
                if(tweets != null)
                {
                    mTweetList.clear();
                    mTweetList.addAll(tweets);

                    if(mAdapterTweets != null)
                        mAdapterTweets.notifyDataSetChanged();
                }
            }
        };
        mModel.getLoadList().observe(MainActivity.this , loadListObservor);
    }

    private void updateTweets()
    {
        if(mHandler != null){
            mRunnable = new Runnable() {
                @Override
                public void run() {
                    if(mModel != null){
                        mModel.getTweetList(mSearchData);
                        mHandler.postDelayed(mRunnable , UPDATE_TIME);
                    }
                }
            };

            mHandler.post(mRunnable);
        }
    }

    private void removeUpdates(){
        if(mHandler!=null && mRunnable != null){
            mHandler.removeCallbacks(mRunnable);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search , menu);
        MenuItem menuItem = menu.findItem(R.id.menu_search);
        final SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("search tweet");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mSearchData = s;
                submitQuery();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                removeUpdates();
                return false;
            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.setQuery(mSearchData, false);
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    private void submitQuery() {
        if (TextUtils.isEmpty(mSearchData)) {
            Toast.makeText(MainActivity.this, "Please enter something", Toast.LENGTH_SHORT).show();
        } else {
            removeUpdates();
            updateTweets();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateTweets();
    }

    @Override
    protected void onStop() {
        super.onStop();
        removeUpdates();
    }

}

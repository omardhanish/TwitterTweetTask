package converse.heptagon.com.twittertweetstask.Tweets;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import converse.heptagon.com.twittertweetstask.R;
import converse.heptagon.com.twittertweetstask.model.Tweet;
import converse.heptagon.com.twittertweetstask.model.TweetDateFormatter;

public class AdapterTweets extends RecyclerView.Adapter<AdapterTweets.ViewHolder>
{
    private final TweetDateFormatter mFormatter;
    private List<Tweet> mTweetList;
    private Context mContext;

    public AdapterTweets(Context context, TweetDateFormatter formatter , List<Tweet> tweetList) {
        this.mFormatter = formatter;
        this.mTweetList = tweetList;
        this.mContext  = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View tweetItemView = inflater.inflate(R.layout.row_tweet_layout, viewGroup, false);
        return new ViewHolder(tweetItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i)
    {
        Tweet data = mTweetList.get(i);

        viewHolder.tv_time.setText(mFormatter.format(mContext, data.getCreatedAt()));
        viewHolder.tv_username.setText(data.getUsername());
        viewHolder.tv_tweet.setText(data.getContent());
    }

    @Override
    public int getItemCount() {
        return mTweetList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_username , tv_time , tv_tweet;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_username = itemView.findViewById(R.id.tv_username);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_tweet = itemView.findViewById(R.id.tv_tweet);

        }
    }

}

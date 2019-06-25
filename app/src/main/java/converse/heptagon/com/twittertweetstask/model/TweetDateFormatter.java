package converse.heptagon.com.twittertweetstask.model;

import android.content.Context;


import java.util.Date;

import converse.heptagon.com.twittertweetstask.R;

public class TweetDateFormatter {
    public String format(Context context, Date date) {
        long SECONDS_PER_DAY = 24 * 60 * 60;
        long diff = (new Date().getTime() - date.getTime()) / SECONDS_PER_DAY;

        if (diff < 60) {
            return context.getString(R.string.seconds, diff);
        }

        if (diff < 60 * 60) {
            return context.getString(R.string.minutes, diff / 60);
        }

        if (diff < 24 * 60 * 60) {
            return context.getString(R.string.hours, diff / (60 * 60));
        }

        return context.getString(R.string.days, diff / SECONDS_PER_DAY);
    }
}

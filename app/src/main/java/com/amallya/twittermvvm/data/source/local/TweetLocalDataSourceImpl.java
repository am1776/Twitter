package com.amallya.twittermvvm.data.source.local;

import android.content.Context;

import com.amallya.twittermvvm.RestApplication;
import com.amallya.twittermvvm.models.Tweet;
import com.amallya.twittermvvm.utils.AppExecutors;

import java.util.List;

/**
 * Created by anmallya on 3/14/2018.
 */

public class TweetLocalDataSourceImpl implements TweetLocalDataSource {

    private AppExecutors appExecutors;
    private Context context;

    public TweetLocalDataSourceImpl(Context context, AppExecutors appExecutors){
        this.appExecutors = appExecutors;
        this.context = context;
    }

    @Override
    public void insertTweets(final List<Tweet> newTweets) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                TweetsDatabase.getInstance(context).tweetDao().insertTweets(newTweets);
            }
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void deleteAllTweets() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                TweetsDatabase.getInstance(context).tweetDao().deleteAllTweets();
            }
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getTweets(long max, final GetTweetsCallBack callBack) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<Tweet> tweets = TweetsDatabase.getInstance(context).tweetDao().getAllTweets();
                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onTweetsObtained(tweets);
                    }
                });
            }
        };
        appExecutors.diskIO().execute(runnable);
    }
}

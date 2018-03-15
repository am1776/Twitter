package com.amallya.twittermvvm.data.local;

import com.amallya.twittermvvm.RestApplication;
import com.amallya.twittermvvm.data.TweetLocalDataSource;
import com.amallya.twittermvvm.models.Tweet;
import com.amallya.twittermvvm.utils.AppExecutors;

import java.util.List;

/**
 * Created by anmallya on 3/14/2018.
 */

public class TweetLocalDataSourceImpl implements TweetLocalDataSource {

    AppExecutors appExecutors;

    public TweetLocalDataSourceImpl(){
        appExecutors = new AppExecutors();
    }

    @Override
    public void insertTweets(final List<Tweet> newTweets) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                TweetsDatabase.getInstance(RestApplication.getContext()).tweetDao().insertTweets(newTweets);
            }
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void deleteAllTweets() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                TweetsDatabase.getInstance(RestApplication.getContext()).tweetDao().deleteAllTweets();
            }
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getTweets(long max, final GetTweetsCallBack callBack) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<Tweet> tweets = TweetsDatabase.getInstance(RestApplication.getContext()).tweetDao().getAllTweets();
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

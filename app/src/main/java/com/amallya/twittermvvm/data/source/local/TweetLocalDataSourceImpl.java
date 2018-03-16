package com.amallya.twittermvvm.data.source.local;

import android.content.Context;

import com.amallya.twittermvvm.RestApplication;
import com.amallya.twittermvvm.data.source.DataSource;
import com.amallya.twittermvvm.models.Response;
import com.amallya.twittermvvm.models.Tweet;
import com.amallya.twittermvvm.utils.AppExecutors;

import java.util.List;

import static com.amallya.twittermvvm.models.Response.GENERIC_SUCCESS_MSG;
import static com.amallya.twittermvvm.models.Response.Status.SUCCESS;

/**
 * Created by anmallya on 3/14/2018.
 */

public class TweetLocalDataSourceImpl implements TweetLocalDataSource<DataSource.ResultCallBack> {

    private AppExecutors appExecutors;
    private Context context;

    public TweetLocalDataSourceImpl(Context context, AppExecutors appExecutors){
        this.appExecutors = appExecutors;
        this.context = context;
    }

    @Override
    public void insertTweets(final List<Tweet> newTweets, ResultCallBack resultCallBack) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                TweetsDatabase.getInstance(context).tweetDao().insertTweets(newTweets);
            }
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void deleteAllTweets(ResultCallBack resultCallBack) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                TweetsDatabase.getInstance(context).tweetDao().deleteAllTweets();
            }
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getTweets(long max, final ResultCallBack callBack) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<Tweet> tweets = TweetsDatabase.getInstance(context).tweetDao().getAllTweets();
                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        Response<List<Tweet>> response = new Response(GENERIC_SUCCESS_MSG, SUCCESS);
                        response.setData(tweets);
                        callBack.onResultObtained(response);
                    }
                });
            }
        };
        appExecutors.diskIO().execute(runnable);
    }
}

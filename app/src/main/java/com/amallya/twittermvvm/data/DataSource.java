package com.amallya.twittermvvm.data;

import com.amallya.twittermvvm.RestApplication;
import com.amallya.twittermvvm.data.local.TweetsDatabase;
import com.amallya.twittermvvm.models.Tweet;

import java.util.List;

/**
 * Created by anmallya on 3/14/2018.
 */

public interface DataSource {

    void getTweets(long max, GetTweetsCallBack callBack);

    interface GetTweetsCallBack{
        void onTweetsObtained(List<Tweet> tweets);
    }
}

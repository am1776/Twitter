package com.amallya.twittermvvm.data.source;

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

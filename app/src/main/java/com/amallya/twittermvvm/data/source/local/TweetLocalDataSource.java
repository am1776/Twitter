package com.amallya.twittermvvm.data.source.local;

import com.amallya.twittermvvm.data.source.DataSource;
import com.amallya.twittermvvm.models.Tweet;

import java.util.List;

/**
 * Created by anmallya on 3/14/2018.
 */

public interface TweetLocalDataSource extends DataSource {

    void insertTweets(List<Tweet> newTweets);

    void deleteAllTweets();
}

package com.amallya.twittermvvm.data.repo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import com.amallya.twittermvvm.RestApplication;
import com.amallya.twittermvvm.data.DataSource;
import com.amallya.twittermvvm.data.TweetLocalDataSource;
import com.amallya.twittermvvm.data.local.TweetLocalDataSourceImpl;
import com.amallya.twittermvvm.data.local.TweetsDatabase;
import com.amallya.twittermvvm.data.remote.TweetRemoteDataSourceImpl;
import com.amallya.twittermvvm.models.Tweet;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by anmallya on 3/12/2018.
 */

public class TweetListRepo {

    private DataSource localDataSource, remoteDataSource;
    final MutableLiveData<List<Tweet>> tweetListObservable;
    private static final int TWEET_ID_MAX_DEFAULT = -1;
    private long maxTweetId = TWEET_ID_MAX_DEFAULT;
    private List<Tweet> tweetList;

    public TweetListRepo(){
        localDataSource = new TweetLocalDataSourceImpl();
        remoteDataSource = new TweetRemoteDataSourceImpl();
        tweetListObservable = new MutableLiveData<>();;
        tweetList = new ArrayList<>();
    }

    public LiveData<List<Tweet>> getTweets(){
        return tweetListObservable;
    }

    private void fetchTweets(){
        final DataSource dataSource =  isConnectedToInternet() ? remoteDataSource : localDataSource;
        dataSource.getTweets(maxTweetId, new DataSource.GetTweetsCallBack(){
            @Override
            public void onTweetsObtained(List<Tweet> tweetListNew) {
                updateLocalDB(dataSource, tweetListNew);
                updateMaxTweetId(tweetListNew);
                tweetList.addAll(tweetListNew);
                tweetListObservable.setValue(tweetList);
            }});
    }

    public void refreshTweets(){
        tweetList.clear();
        maxTweetId = TWEET_ID_MAX_DEFAULT;
        fetchTweets();
    }

    public void loadMoreTweets(){
        fetchTweets();
    }


    private void updateMaxTweetId(List<Tweet> tweetListNew){
        if (tweetListNew.size() > 0){
            maxTweetId = tweetListNew.get(tweetListNew.size() - 1).getId();
        }
    }

    private void updateLocalDB(DataSource dataSource, List<Tweet>  tweets){
        if(!(dataSource instanceof TweetLocalDataSource)){
            ((TweetLocalDataSource)localDataSource).insertTweets(tweets);
        }
    }

    public Tweet fetchSelectedTweet(int position){
        return tweetList.get(position);
    }

    private boolean isConnectedToInternet(){
        return true;
    }
}

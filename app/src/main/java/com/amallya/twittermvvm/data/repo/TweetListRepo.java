package com.amallya.twittermvvm.data.repo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.amallya.twittermvvm.data.source.DataSource;
import com.amallya.twittermvvm.data.source.local.TweetLocalDataSource;
import com.amallya.twittermvvm.data.source.local.TweetLocalDataSourceImpl;
import com.amallya.twittermvvm.data.source.remote.TweetRemoteDataSourceImpl;
import com.amallya.twittermvvm.models.Response;
import com.amallya.twittermvvm.models.Tweet;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by anmallya on 3/12/2018.
 */

public class TweetListRepo extends BaseRepo {

    private DataSource localDataSource, remoteDataSource;
    final MutableLiveData<Response<List<Tweet>>> tweetListObservable;
    private static final int TWEET_ID_MAX_DEFAULT = -1;
    private long maxTweetId = TWEET_ID_MAX_DEFAULT;
    private List<Tweet> tweetList;

    public TweetListRepo(DataSource localDataSource, DataSource remoteDataSource){
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
        tweetListObservable = new MutableLiveData<>();;
        tweetList = new ArrayList<>();
    }

    public LiveData<Response<List<Tweet>>> getTweets(){
        return tweetListObservable;
    }

    private void fetchTweets(){
        final DataSource dataSource =  isConnectedToInternet() ? remoteDataSource : localDataSource;
        dataSource.getTweets(maxTweetId, new DataSource.ResultCallBack<List<Tweet>>(){
            @Override
            public void onResultObtained(Response<List<Tweet>> response) {
                if(response.getErrorCode() == Response.Status.SUCCESS){
                    List<Tweet> tweetListNew = response.getData();
                    updateLocalDB(dataSource, tweetListNew);
                    updateMaxTweetId(tweetListNew);
                    tweetList.addAll(tweetListNew);
                    response.setData(tweetList);
                }
                tweetListObservable.setValue(response);
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
            ((TweetLocalDataSource)localDataSource).insertTweets(tweets, new DataSource.ResultCallBack<List<Tweet>>(){
                @Override
                public void onResultObtained(Response<List<Tweet>> response) {

                }});
        }
    }

    public Tweet fetchSelectedTweet(int position){
        return tweetList.get(position);
    }

    private boolean isConnectedToInternet(){
        return true;
    }
}

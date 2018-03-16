package com.amallya.twittermvvm;

import android.content.Context;

import com.amallya.twittermvvm.data.repo.TweetActionsRepo;
import com.amallya.twittermvvm.data.repo.TweetListRepo;
import com.amallya.twittermvvm.data.repo.UserCredRepo;
import com.amallya.twittermvvm.data.source.DataSource;
import com.amallya.twittermvvm.data.source.local.TweetLocalDataSourceImpl;
import com.amallya.twittermvvm.data.source.remote.TweetRemoteDataSourceImpl;
import com.amallya.twittermvvm.data.source.remote.TwitterClient;
import com.amallya.twittermvvm.utils.AppExecutors;

/**
 * Created by anmallya on 3/15/2018.
 */

public class Injection {

    public static TweetListRepo provideTweetListRepo(Context context) {
        DataSource localDataSource = provideLocalDataSource(context);
        DataSource remoteDataSource =  provideRemoteDataSource(context);
        return new TweetListRepo(localDataSource, remoteDataSource);
    }

    public static TweetActionsRepo provideTweetActionsRepo(Context context) {
        DataSource remoteDataSource =  provideRemoteDataSource(context);
        return new TweetActionsRepo(remoteDataSource);
    }

    public static UserCredRepo provideUserCredRepo(Context context) {
        DataSource remoteDataSource =  provideRemoteDataSource(context);
        return new UserCredRepo(remoteDataSource);
    }

    public static DataSource provideRemoteDataSource(Context context){
        TwitterClient twitterClient = provideTwitterClient();
        return new TweetRemoteDataSourceImpl(twitterClient);
    }

    public static DataSource provideLocalDataSource(Context context){
        AppExecutors appExecutors = provideAppExecutors();
        return new TweetLocalDataSourceImpl(context, appExecutors);
    }

    public static AppExecutors provideAppExecutors(){
        return new AppExecutors();
    }

    public static TwitterClient provideTwitterClient(){
        return RestApplication.getRestClient();
    }
}

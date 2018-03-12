package com.amallya.twittermvvm;

import android.app.Application;
import android.content.Context;

import com.amallya.twittermvvm.models.User;


import com.amallya.twittermvvm.network.TwitterClient;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.config.FlowManager;


/**
 * Created by anmallya on 3/10/2018.
 */

public class RestApplication extends Application {
    private static Context context;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        RestApplication.user = user;
    }

    private static User user;

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(new FlowConfig.Builder(this).build());
        FlowLog.setMinimumLoggingLevel(FlowLog.Level.V);
        RestApplication.context = this;
    }

    public static TwitterClient getRestClient() {
        return (TwitterClient) TwitterClient.getInstance(TwitterClient.class, RestApplication.context);
    }
}
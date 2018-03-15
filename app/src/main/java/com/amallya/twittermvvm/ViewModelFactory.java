package com.amallya.twittermvvm;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.VisibleForTesting;

import com.amallya.twittermvvm.data.repo.TweetActionsRepo;
import com.amallya.twittermvvm.data.repo.TweetListRepo;
import com.amallya.twittermvvm.data.repo.UserCredRepo;
import com.amallya.twittermvvm.ui.main.MainViewModel;
import com.amallya.twittermvvm.ui.tweet_detail.TweetDetailViewModel;
import com.amallya.twittermvvm.ui.tweets.TweetViewModel;

/**
 * Created by anmallya on 3/15/2018.
 */

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    @SuppressLint("StaticFieldLeak")
    private static volatile ViewModelFactory INSTANCE;

    private final Application application;

    private final TweetActionsRepo tweetActionsRepo;
    private final TweetListRepo tweetListRepo;
    private final UserCredRepo userCredRepo;


    public static ViewModelFactory getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (ViewModelFactory.class) {
                if (INSTANCE == null) {
                    Context context = application.getApplicationContext();
                    INSTANCE = new ViewModelFactory(application, Injection.provideTweetListRepo(context), Injection.provideTweetActionsRepo(context), Injection.provideUserCredRepo(context));
                }
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }

    private ViewModelFactory(Application application, TweetListRepo tweetListRepo, TweetActionsRepo tweetActionsRepo, UserCredRepo userCredRepo) {
        this.application = application;
        this.tweetListRepo = tweetListRepo;
        this.tweetActionsRepo = tweetActionsRepo;
        this.userCredRepo = userCredRepo;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(TweetViewModel.class)) {
            return (T) new TweetViewModel(tweetListRepo, tweetActionsRepo);
        } else if (modelClass.isAssignableFrom(TweetDetailViewModel.class)){
            return (T) new TweetDetailViewModel(tweetActionsRepo);
        } else if (modelClass.isAssignableFrom(MainViewModel.class)){
            return (T) new MainViewModel(userCredRepo);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
package com.amallya.twittermvvm.ui.tweets;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.MockitoAnnotations;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


import com.amallya.twittermvvm.data.repo.TweetActionsRepo;
import com.amallya.twittermvvm.data.repo.TweetListRepo;

/**
 * Created by anmallya on 3/18/2018.
 */

@RunWith(JUnit4.class)
public class TweetViewModelTest {

    private TweetViewModel viewModel;
    private TweetActionsRepo tweetActionsRepo;
    private TweetListRepo tweetListRepo;
    private static final int POS =8;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        tweetActionsRepo = mock(TweetActionsRepo.class);
        tweetListRepo = mock(TweetListRepo.class);
        viewModel = new TweetViewModel(tweetListRepo, tweetActionsRepo);
    }

    @Test
    public void getRefreshingObservableTest() {
        viewModel.getRefreshingObservable();
        verify(tweetListRepo).getRefreshingObservable();
    }


    @Test
    public void getTweetsObservableTest() {
        viewModel.getTweetsObservable();
        verify(tweetListRepo).getTweetsObservable();
    }


    @Test
    public void getSelectedTweetObservableTest() {
        viewModel.getSelectedTweetObservable();
        verify(tweetListRepo).getSelectedTweetObservable();
    }

    @Test
    public void getTweetsActionsObservableTest() {
        viewModel.getTweetsActionsObservable();
        verify(tweetActionsRepo).getTweetsActionObservable();
    }

    @Test
    public void refreshTweetsTest() {
        viewModel.refreshTweets();
        verify(tweetListRepo).refreshTweets();
    }

    @Test
    public void loadMoreTweetsTest() {
        viewModel.loadMoreTweets();
        verify(tweetListRepo, atLeast(2)).loadMoreTweets();
    }

    @Test
    public void onTweetClickedTest() {
        viewModel.onTweetClicked(POS);
        verify(tweetListRepo).onTweetClicked(POS);
    }

    @Test
    public void onUserActionOnTweetTest() {
        long tweetId = 12;
        TweetUserAction tweetUserAction = TweetUserAction.FAVORITE;
        viewModel.userActionOnTweet(tweetUserAction, tweetId);
        verify(tweetActionsRepo).userActionOnTweet(tweetUserAction, tweetId);
    }


    @Test
    public void constructorTest() {
        verify(tweetListRepo).loadMoreTweets();
    }

}

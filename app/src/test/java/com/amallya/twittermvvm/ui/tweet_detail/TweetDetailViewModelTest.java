package com.amallya.twittermvvm.ui.tweet_detail;

import android.arch.core.executor.testing.InstantTaskExecutorRule;

import com.amallya.twittermvvm.data.repo.TweetActionsRepo;
import com.amallya.twittermvvm.data.repo.TweetListRepo;
import com.amallya.twittermvvm.ui.tweets.TweetUserAction;
import com.amallya.twittermvvm.ui.tweets.TweetViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by anmallya on 3/18/2018.
 */

public class TweetDetailViewModelTest {

    private TweetDetailViewModel viewModel;
    private TweetActionsRepo tweetActionsRepo;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        tweetActionsRepo = mock(TweetActionsRepo.class);
        viewModel = new TweetDetailViewModel(tweetActionsRepo);
    }

    @Test
    public void getTweetsActionsObservableTest() {
        viewModel.getTweetsActionsObservable();
        verify(tweetActionsRepo).getTweetsActionObservable();
    }

    @Test
    public void onUserActionOnTweetTest() {
        long tweetId = 12;
        TweetUserAction tweetUserAction = TweetUserAction.FAVORITE;
        viewModel.userActionOnTweet(tweetUserAction, tweetId);
        verify(tweetActionsRepo).userActionOnTweet(tweetUserAction, tweetId);
    }

    @Test
    public void onUserActionOnTweetReplyTest() {
        long tweetId = 12;
        TweetUserAction tweetUserAction = TweetUserAction.FAVORITE;
        String response = "TEST";
        viewModel.userReplyOnTweet(tweetUserAction, tweetId, response);
        verify(tweetActionsRepo).userReplyOnTweet(tweetUserAction, tweetId, response);
    }



}

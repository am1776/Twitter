package com.amallya.twittermvvm.repo;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.Observer;

import com.amallya.twittermvvm.TestUtils;
import com.amallya.twittermvvm.data.repo.TweetActionsRepo;
import com.amallya.twittermvvm.data.repo.UserCredRepo;
import com.amallya.twittermvvm.data.source.DataSource;
import com.amallya.twittermvvm.data.source.remote.TweetRemoteDataSource;
import com.amallya.twittermvvm.data.source.remote.TweetRemoteDataSourceImpl;
import com.amallya.twittermvvm.models.Request;
import com.amallya.twittermvvm.models.Response;
import com.amallya.twittermvvm.ui.main.MainViewModel;
import com.amallya.twittermvvm.ui.tweets.TweetUserAction;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by anmallya on 3/18/2018.
 */

public class TweetActionsRepoTest {

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    private DataSource dataSource;
    private TweetActionsRepo tweetActionsRepo;
    @Captor
    ArgumentCaptor<DataSource.ResultCallBack> callbackCaptor;
    ArgumentCaptor<Request> requestCaptor;
    ArgumentCaptor<Request> requestCaptor2;


    private long TWEETID = 12;
    private TweetUserAction TWEETUSERACTION = TweetUserAction.FAVORITE;
    private String RESPONSE = "test";

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        dataSource = mock(TweetRemoteDataSourceImpl.class);
        tweetActionsRepo = new TweetActionsRepo(dataSource);
    }

    @Test
    public void actionObservableisNotNull(){
        // action Observable is not null
        Assert.assertNotNull(tweetActionsRepo.getTweetsActionObservable());
    }

    @Test
    public void userActionOnTweetSuccessTest(){

        // when user takes a action on a tweet
        tweetActionsRepo.userActionOnTweet(TWEETUSERACTION, TWEETID);

        // check whether takeActionOnTweet is called
        verify((TweetRemoteDataSource)dataSource).takeActionOnTweet(any(Request.class), callbackCaptor.capture());

        // call the callback
        Response<String> actionResponse = new Response<>("MSG", Response.Status.SUCCESS);
        callbackCaptor.getValue().onResultObtained(actionResponse);

        // Make sure livedata.set is not called
        assertFalse(tweetActionsRepo.getTweetsActionObservable().getValue() == actionResponse);
    }

    @Test
    public void userActionOnTweetFailureTest(){
        requestCaptor = ArgumentCaptor.forClass(Request.class);

        // when user takes a action on a tweet
        tweetActionsRepo.userActionOnTweet(TWEETUSERACTION, TWEETID);

        // check whether takeActionOnTweet is called
        verify((TweetRemoteDataSource)dataSource).takeActionOnTweet(requestCaptor.capture(), callbackCaptor.capture());

        // verify that the Request arg is correct
        assertEquals(requestCaptor.getValue().getId(), TWEETID);
        assertEquals(requestCaptor.getValue().getTweetUserAction(), TWEETUSERACTION);
        Response<?> actionResponse = new Response<>("MSG", Response.Status.ERROR);

        // verify that the observer is notified
        Observer<Response<?>> observer = mock(Observer.class);
        tweetActionsRepo.getTweetsActionObservable().observe(TestUtils.TEST_OBSERVER, observer);
        callbackCaptor.getValue().onResultObtained(actionResponse);
        verify(observer).onChanged(actionResponse);
    }

    @Test
    public void userReplyOnTweetTest(){
        requestCaptor2 = ArgumentCaptor.forClass(Request.class);

        // when user takes a action on a tweet
        tweetActionsRepo.userReplyOnTweet(TWEETUSERACTION, TWEETID, RESPONSE);

        // check whether takeActionOnTweet is called
        verify((TweetRemoteDataSource)dataSource).takeActionOnTweet(requestCaptor2.capture(), callbackCaptor.capture());

        // verify that the Request arg is correct
        assertEquals(requestCaptor2.getValue().getId(), TWEETID);
        assertEquals(requestCaptor2.getValue().getTweetUserAction(), TWEETUSERACTION);
        assertEquals(requestCaptor2.getValue().getMessage(), RESPONSE);

        // verify that the observer is notified
        Response<String> actionResponse = new Response<>("MSG", Response.Status.ERROR);
        Observer<Response<?>> observer = mock(Observer.class);
        tweetActionsRepo.getTweetsActionObservable().observe(TestUtils.TEST_OBSERVER, observer);
        callbackCaptor.getValue().onResultObtained(actionResponse);
        verify(observer).onChanged(actionResponse);
    }
}

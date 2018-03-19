package com.amallya.twittermvvm.repo;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.Observer;

import com.amallya.twittermvvm.TestUtils;
import com.amallya.twittermvvm.data.repo.TweetActionsRepo;
import com.amallya.twittermvvm.data.repo.TweetListRepo;
import com.amallya.twittermvvm.data.source.DataSource;
import com.amallya.twittermvvm.data.source.local.TweetLocalDataSource;
import com.amallya.twittermvvm.data.source.local.TweetLocalDataSourceImpl;
import com.amallya.twittermvvm.data.source.remote.TweetRemoteDataSource;
import com.amallya.twittermvvm.data.source.remote.TweetRemoteDataSourceImpl;
import com.amallya.twittermvvm.models.Request;
import com.amallya.twittermvvm.models.Response;
import com.amallya.twittermvvm.models.Tweet;
import com.amallya.twittermvvm.ui.tweets.TweetUserAction;
import com.amallya.twittermvvm.utils.NetworkUtil;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by anmallya on 3/18/2018.
 */

public class TweetListRepoTest {
    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    private DataSource dataSourceRemote;
    private DataSource dataSourceLocal;

    private TweetListRepo tweetListRepo;
    @Captor
    ArgumentCaptor<DataSource.ResultCallBack> callbackCaptor;
    ArgumentCaptor<Request> requestCaptor;
    ArgumentCaptor<Request> requestCaptor2;


    private long TWEETID = 12;
    private TweetUserAction TWEETUSERACTION = TweetUserAction.FAVORITE;
    private String RESPONSE = "test";


    private List<Tweet> REMOTE_TWEET_LIST_1 = new ArrayList<>();
    private List<Tweet> REMOTE_TWEET_LIST_2 = new ArrayList<>();


    @Mock
    Tweet t1, t2, t3;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        dataSourceRemote = mock(TweetRemoteDataSourceImpl.class);
        dataSourceLocal = mock(TweetLocalDataSource.class);
        tweetListRepo = new TweetListRepo(dataSourceLocal, dataSourceRemote);
    }

    @Test
    public void getSelectedTweetsObservableisNotNull(){
        Assert.assertNotNull(tweetListRepo.getSelectedTweetObservable());
    }

    @Test
    public void getTweetsObservableisNotNull(){
        Assert.assertNotNull(tweetListRepo.getTweetsObservable());
    }

    @Test
    public void getRefreshingObservableisNotNull(){
        Assert.assertNotNull(tweetListRepo.getRefreshingObservable());
    }


    @Test
    public void loadMoreTweetsRemoteTest(){
        ArgumentCaptor<List<Tweet>> requestCaptor = ArgumentCaptor.forClass(List.class);

        // load more tweets
        tweetListRepo.loadMoreTweets();

        // make sure datasource gets called
        verify((TweetRemoteDataSource)dataSourceRemote).getTweets(anyLong(), callbackCaptor.capture());

        // mock callback
        Response<List<Tweet>> actionResponse = new Response<>("MSG", Response.Status.SUCCESS);
        actionResponse.setData(REMOTE_TWEET_LIST_1);
        callbackCaptor.getValue().onResultObtained(actionResponse);

         //check update database gets called
        verify((TweetLocalDataSource)dataSourceLocal).insertTweets(requestCaptor.capture(), any());
        assertEquals(requestCaptor.getValue(), REMOTE_TWEET_LIST_1);

        // Check observable gets called.
        Observer<Response<List<Tweet>>> observer = mock(Observer.class);
        tweetListRepo.getTweetsObservable().observe(TestUtils.TEST_OBSERVER, observer);
        verify(observer).onChanged(actionResponse);
    }

    @Test
    public void loadMoreTweetsRemoteListSuccessTest(){
        tweetListRepo.loadMoreTweets();
        verify((TweetRemoteDataSource)dataSourceRemote).getTweets(anyLong(), callbackCaptor.capture());
        Response<List<Tweet>> actionResponse = new Response<>("MSG", Response.Status.SUCCESS);
        REMOTE_TWEET_LIST_1.add(t1);
        actionResponse.setData(REMOTE_TWEET_LIST_1);
        callbackCaptor.getValue().onResultObtained(actionResponse);
        assertEquals(actionResponse.getData().size(), 1);

        tweetListRepo.loadMoreTweets();
        verify((TweetRemoteDataSource)dataSourceRemote, atLeast(2)).getTweets(anyLong(), callbackCaptor.capture());
        Response<List<Tweet>> actionResponse2 = new Response<>("MSG", Response.Status.SUCCESS);
        REMOTE_TWEET_LIST_2.add(t2); REMOTE_TWEET_LIST_2.add(t3);
        actionResponse2.setData(REMOTE_TWEET_LIST_2);
        callbackCaptor.getValue().onResultObtained(actionResponse2);
        assertEquals(actionResponse2.getData().size(), 3);
    }

    @Test
    public void loadMoreTweetsRemoteListErrorTest(){

        tweetListRepo.loadMoreTweets();
        verify((TweetRemoteDataSource)dataSourceRemote).getTweets(anyLong(), callbackCaptor.capture());
        Response<List<Tweet>> actionResponse = new Response<>("MSG", Response.Status.SUCCESS);
        REMOTE_TWEET_LIST_1.add(t1);
        actionResponse.setData(REMOTE_TWEET_LIST_1);
        callbackCaptor.getValue().onResultObtained(actionResponse);
        assertEquals(actionResponse.getData().size(), 1);

        tweetListRepo.loadMoreTweets();
        verify((TweetRemoteDataSource)dataSourceRemote, atLeast(2)).getTweets(anyLong(), callbackCaptor.capture());
        Response<List<Tweet>> actionResponse2 = new Response<>("MSG", Response.Status.ERROR);
        REMOTE_TWEET_LIST_2.add(t2); REMOTE_TWEET_LIST_2.add(t3);
        actionResponse2.setData(REMOTE_TWEET_LIST_2);
        callbackCaptor.getValue().onResultObtained(actionResponse2);
        assertNotEquals(actionResponse2.getData().size(), 3);
    }

    @Test
    public void refreshTweetsTest(){
        tweetListRepo.loadMoreTweets();
        verify((TweetRemoteDataSource)dataSourceRemote).getTweets(anyLong(), callbackCaptor.capture());
        Response<List<Tweet>> actionResponse = new Response<>("MSG", Response.Status.SUCCESS);
        REMOTE_TWEET_LIST_1.add(t1);
        actionResponse.setData(REMOTE_TWEET_LIST_1);
        callbackCaptor.getValue().onResultObtained(actionResponse);

        tweetListRepo.refreshTweets();

        verify((TweetRemoteDataSource)dataSourceRemote, atLeast(1)).getTweets(anyLong(), callbackCaptor.capture());
        Response<List<Tweet>> actionResponse2 = new Response<>("MSG", Response.Status.SUCCESS);
        REMOTE_TWEET_LIST_2.add(t2);
        actionResponse.setData(REMOTE_TWEET_LIST_2);
        callbackCaptor.getValue().onResultObtained(actionResponse);
        assertEquals(actionResponse.getData().size(), 1);

        Observer<Boolean> observer = mock(Observer.class);
        tweetListRepo.getRefreshingObservable().observe(TestUtils.TEST_OBSERVER, observer);
        verify(observer).onChanged(true);
    }

    @Test
    public void selectedTweetTest(){

        tweetListRepo.loadMoreTweets();
        verify((TweetRemoteDataSource)dataSourceRemote).getTweets(anyLong(), callbackCaptor.capture());
        Response<List<Tweet>> actionResponse = new Response<>("MSG", Response.Status.SUCCESS);
        REMOTE_TWEET_LIST_1.add(t1);
        REMOTE_TWEET_LIST_1.add(t2);
        REMOTE_TWEET_LIST_1.add(t3);
        actionResponse.setData(REMOTE_TWEET_LIST_1);
        callbackCaptor.getValue().onResultObtained(actionResponse);

        tweetListRepo.onTweetClicked(2);

        Observer<Tweet> observer = mock(Observer.class);
        tweetListRepo.getSelectedTweetObservable().observe(TestUtils.TEST_OBSERVER, observer);
        verify(observer).onChanged(t3);
    }
}

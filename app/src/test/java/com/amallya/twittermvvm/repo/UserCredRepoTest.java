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
import com.amallya.twittermvvm.models.User;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by anmallya on 3/18/2018.
 */

public class UserCredRepoTest {
    private DataSource dataSource;
    private UserCredRepo userCredRepo;

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    Response<User> actionResponse = new Response<>("MSG", Response.Status.ERROR);

    @Captor
    ArgumentCaptor<DataSource.ResultCallBack> callbackCaptor;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        dataSource = mock(TweetRemoteDataSourceImpl.class);
        userCredRepo = new UserCredRepo(dataSource);
    }

    @Test
    public void userCredObservableisNotNull(){
        Assert.assertNotNull(userCredRepo.getUserCredObservable());
    }

    @Test
    public void accessTokenObservableisNotNull(){
        Assert.assertNotNull(userCredRepo.getAccessTokenClearedObservable());
    }

    @Test
    public void fetchUserCredTest(){
        userCredRepo.fetchUserCred();
        verify((TweetRemoteDataSource)dataSource).getUserCred(callbackCaptor.capture());
        Observer<Response<User>> observer = mock(Observer.class);
        userCredRepo.getUserCredObservable().observe(TestUtils.TEST_OBSERVER, observer);
        callbackCaptor.getValue().onResultObtained(actionResponse);
        verify(observer).onChanged(actionResponse);
    }

    @Test
    public void clearUserAccessTokenTest(){
        userCredRepo.clearAccessTokens();
        verify((TweetRemoteDataSource)dataSource).clearAccessToken(callbackCaptor.capture());
        Observer<Boolean> observer = mock(Observer.class);
        userCredRepo.getAccessTokenClearedObservable().observe(TestUtils.TEST_OBSERVER, observer);
        callbackCaptor.getValue().onResultObtained(actionResponse);
        verify(observer).onChanged(true);
    }
}

package com.amallya.twittermvvm.ui.main;

import android.arch.core.executor.testing.InstantTaskExecutorRule;

import com.amallya.twittermvvm.data.repo.TweetActionsRepo;
import com.amallya.twittermvvm.data.repo.TweetListRepo;
import com.amallya.twittermvvm.data.repo.UserCredRepo;
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

public class MainViewModelTest {

    private MainViewModel viewModel;
    private UserCredRepo userCredRepo;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        userCredRepo = mock(UserCredRepo.class);
        viewModel = new MainViewModel(userCredRepo);
    }

    @Test
    public void getUserCredObservableTest() {
        viewModel.getUserCredentialObservable();
        verify(userCredRepo).getUserCredObservable();
    }

    @Test
    public void constructorTest() {
        verify(userCredRepo).fetchUserCred();
    }

    @Test
    public void clearAccessTokenTest() {
        viewModel.getAccessTokenClearedObservable();
        verify(userCredRepo).getAccessTokenClearedObservable();
    }
}

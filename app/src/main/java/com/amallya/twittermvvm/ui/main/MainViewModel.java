package com.amallya.twittermvvm.ui.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import com.amallya.twittermvvm.data.repo.UserCredRepo;
import com.amallya.twittermvvm.models.Response;
import com.amallya.twittermvvm.models.User;


/**
 * Created by anmallya on 3/12/2018.
 */

public class MainViewModel extends ViewModel {

    private UserCredRepo userCredRepo;

    public MainViewModel(UserCredRepo userCredRepo) {
        super();
        this.userCredRepo = userCredRepo;
    }

    public LiveData<Response<User>> getUserCredentialObservable(){
        return userCredRepo.getUserCredObservable();
    }

    public void clearAccessTokens(){
        userCredRepo.clearAccessTokens();
    }

    public LiveData<Boolean> getAccessTokenClearedObservable(){
        return userCredRepo.getAccessTokenClearedObservable();
    }
}


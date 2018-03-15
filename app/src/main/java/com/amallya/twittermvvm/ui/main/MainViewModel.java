package com.amallya.twittermvvm.ui.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import com.amallya.twittermvvm.data.repo.UserCredRepo;
import com.amallya.twittermvvm.models.User;


/**
 * Created by anmallya on 3/12/2018.
 */

public class MainViewModel extends ViewModel {

    private LiveData<User> userCred;
    UserCredRepo userCredRepo;

    public MainViewModel(UserCredRepo userCredRepo) {
        super();
        this.userCredRepo = userCredRepo;
        this.userCred = userCredRepo.getUserCredObservable();
    }

    public LiveData<User> getUserCredential(){
        return userCred;
    }
}

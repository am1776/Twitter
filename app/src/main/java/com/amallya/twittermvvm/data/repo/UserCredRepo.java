package com.amallya.twittermvvm.data.repo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;
import com.amallya.twittermvvm.RestApplication;
import com.amallya.twittermvvm.SingleLiveEvent;
import com.amallya.twittermvvm.data.source.DataSource;
import com.amallya.twittermvvm.data.source.remote.TweetRemoteDataSource;
import com.amallya.twittermvvm.data.source.remote.TweetRemoteDataSourceImpl;
import com.amallya.twittermvvm.data.source.remote.TwitterClient;
import com.amallya.twittermvvm.models.Response;
import com.amallya.twittermvvm.models.Tweet;
import com.amallya.twittermvvm.models.User;
import com.amallya.twittermvvm.utils.analytics.Events;
import com.amplitude.api.Amplitude;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONObject;

import java.util.List;

import javax.xml.transform.Result;

import cz.msebera.android.httpclient.Header;

/**
 * Created by anmallya on 3/12/2018.
 */

public class UserCredRepo extends BaseRepo {

    private final DataSource dataSource;
    private final SingleLiveEvent<Boolean> isAccessTokenClearedObservable;
    private final MutableLiveData<Response<User>> userCredObservable;

    public UserCredRepo(DataSource dataSource){
        this.dataSource = dataSource;
        isAccessTokenClearedObservable = new SingleLiveEvent<>();
        userCredObservable = new MutableLiveData<>();
    }

    public LiveData<Response<User>> getUserCredObservable(){
        return userCredObservable;
    }

    public LiveData<Boolean> getAccessTokenClearedObservable(){
        return isAccessTokenClearedObservable;
    }

    public void fetchUserCred(){
        ((TweetRemoteDataSource) dataSource).getUserCred(response -> userCredObservable.setValue(response));
    }

    public void clearAccessTokens(){
        Amplitude.getInstance().logEvent(Events.SIGN_OUT_CLICKED);

        ((TweetRemoteDataSource) dataSource).clearAccessToken(response -> {
            isAccessTokenClearedObservable.setValue(true);
        });
    }
}

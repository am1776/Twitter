package com.amallya.twittermvvm.data.repo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;
import com.amallya.twittermvvm.RestApplication;
import com.amallya.twittermvvm.data.source.DataSource;
import com.amallya.twittermvvm.data.source.remote.TweetRemoteDataSource;
import com.amallya.twittermvvm.data.source.remote.TweetRemoteDataSourceImpl;
import com.amallya.twittermvvm.data.source.remote.TwitterClient;
import com.amallya.twittermvvm.models.Response;
import com.amallya.twittermvvm.models.Tweet;
import com.amallya.twittermvvm.models.User;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by anmallya on 3/12/2018.
 */

public class UserCredRepo extends BaseRepo {

    private DataSource dataSource;

    public UserCredRepo(DataSource dataSource){
        this.dataSource = dataSource;
    }

    public LiveData<Response<User>> getUserCredObservable(){
        final MutableLiveData<Response<User>> data = new MutableLiveData<>();
        ((TweetRemoteDataSource) dataSource).getUserCred(new DataSource.ResultCallBack<User>() {
            @Override
            public void onResultObtained(Response<User> response) {
                data.setValue(response);
            }
        });
        return data;
    }

    public void clearAccessTokens(){
        ((TweetRemoteDataSource) dataSource).clearAccessToken(new DataSource.ResultCallBack(){
            @Override
            public void onResultObtained(Response response) {

            }
        });
    }
}

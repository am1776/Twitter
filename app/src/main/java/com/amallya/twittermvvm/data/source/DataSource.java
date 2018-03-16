package com.amallya.twittermvvm.data.source;

import com.amallya.twittermvvm.models.Response;
import com.amallya.twittermvvm.models.Tweet;

import java.util.List;

/**
 * Created by anmallya on 3/14/2018.
 */

public interface DataSource<T extends DataSource.ResultCallBack> {

    void getTweets(long max, T callBack);

    interface ResultCallBack<E>{
        void onResultObtained(Response<E> response);
    }
}

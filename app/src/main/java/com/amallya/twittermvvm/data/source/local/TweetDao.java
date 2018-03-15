package com.amallya.twittermvvm.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import com.amallya.twittermvvm.models.Tweet;

import java.util.List;

/**
 * Created by anmallya on 3/14/2018.
 */

@Dao
public interface TweetDao {

    @Query("SELECT * FROM Tweet")
    List<Tweet> getAllTweets();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTweets(List<Tweet> users);

    @Query("DELETE FROM Tweet")
    void deleteAllTweets();
}

package com.amallya.twittermvvm.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.amallya.twittermvvm.models.Tweet;

@Database(entities = {Tweet.class}, version = 2)
public abstract class TweetsDatabase extends RoomDatabase {

    public abstract TweetDao tweetDao();

    private static TweetsDatabase INSTANCE;

    private static final Object sLock = new Object();

    public static TweetsDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        TweetsDatabase.class, "Tweets.db")
                        .build();
            }
            return INSTANCE;
        }
    }

}
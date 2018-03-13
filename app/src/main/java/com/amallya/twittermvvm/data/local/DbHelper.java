package com.amallya.twittermvvm.data.local;

import com.amallya.twittermvvm.models.Media;

import java.util.List;

public class DbHelper {
    public static List<Media> getMediaForTweet(long id){
        //List<Media> mediaList = SQLite.select().from(Media.class).where(Media_Table.tweetId.is(id)).queryList();
        return null;
    }
}

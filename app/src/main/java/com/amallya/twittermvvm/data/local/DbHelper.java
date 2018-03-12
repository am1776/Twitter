package com.amallya.twittermvvm.data.local;

import com.amallya.twittermvvm.models.Media;
import com.amallya.twittermvvm.models.Media_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

public class DbHelper {
    public static List<Media> getMediaForTweet(long id){
        List<Media> mediaList = SQLite.select().from(Media.class).where(Media_Table.tweetId.is(id)).queryList();
        return mediaList;
    }
}

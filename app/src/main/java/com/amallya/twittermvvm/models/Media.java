package com.amallya.twittermvvm.models;

import com.google.gson.annotations.SerializedName;
import org.parceler.Parcel;

/**
 * Created by anmallya on 10/29/2016.
 */
@Parcel
public class Media{

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTweetId() {
        return tweetId;
    }

    public void setTweetId(long tweetId) {
        this.tweetId = tweetId;
    }

    private long tweetId;

    @SerializedName("type")
    private String type;

    @SerializedName("id")
    private String id;

    @SerializedName("media_url")
    private String mediaUrl;

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl){
        this.mediaUrl = mediaUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

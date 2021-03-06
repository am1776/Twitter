package com.amallya.twittermvvm.models;

import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.amallya.twittermvvm.utils.UserConverter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

@Parcel
@android.arch.persistence.room.Entity
public class Tweet  {

    @Ignore
    private DisplayType displayType = DisplayType.NORMAL;

    public DisplayType getDisplayType() {
        return displayType;
    }

    public void setDisplayType(DisplayType displayType) {
        this.displayType = displayType;
    }

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("id_str")
    private String idStr;

    @PrimaryKey
    @SerializedName("id")
    private long id;

    @SerializedName("text")
    private String text;

    @TypeConverters({UserConverter.class})
    @SerializedName("user")
    private User user;

    @SerializedName("retweet_count")
    private int retweetCount;

    public int getFavouritesCount() {
        return favouritesCount;
    }

    public void setFavouritesCount(int favouritesCount) {
        this.favouritesCount = favouritesCount;
    }

    @SerializedName("favorite_count")
    private int favouritesCount;

    @SerializedName("favorited")
    private boolean favorited;

    @SerializedName("retweeted")
    private boolean retweeted;

    @Ignore
    @SerializedName("entities")
    private Entity entities;

    public Entity getExtendedEntities() {
        return extendedEntities;
    }

    public void setExtendedEntities(Entity extendedEntities) {
        this.extendedEntities = extendedEntities;
    }

    @Ignore
    @SerializedName("extended_entities")
    private Entity extendedEntities;

    public Entity getEntities() {
        return entities;
    }

    public void setEntities(Entity entities) {
        this.entities = entities;
    }

    public String getIdStr() {
        return idStr;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public void setRetweetCount(int retweetCount) {
        this.retweetCount = retweetCount;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public boolean isRetweeted() {
        return retweeted;
    }

    public void setRetweeted(boolean retweeted) {
        this.retweeted = retweeted;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public static ArrayList<Tweet> getTweetList(String jArrayString){
        System.out.println(jArrayString);
        JsonParser parser = new JsonParser();
        JsonElement tweetElement = parser.parse(jArrayString);
        JsonArray jArray = tweetElement.getAsJsonArray();
        ArrayList<Tweet> tweetList = new ArrayList<Tweet>();
        int length = jArray.size();
        for(int i = 0; i < length; i ++){
            Gson gson = new Gson();
            Tweet tweet = gson.fromJson(jArray.get(i), Tweet.class);
            tweetList.add(tweet);
        }
        return tweetList;
    }

    public static ArrayList<Tweet> getDirectMsgList(String jArrayString){
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%% "+jArrayString);
        ArrayList<Tweet> tweetList = new ArrayList<Tweet>();
        try {
            JSONArray jArrayNew = new JSONArray(jArrayString);
            int length = jArrayNew.length();
            for(int i = 0; i < length; i++){
                JSONObject jO = jArrayNew.getJSONObject(i);
                String text = jO.getString("text");
                String createdDate = jO.getString("created_at");
                JSONObject sender =  jO.getJSONObject("sender");
                Gson gson = new Gson();
                JsonParser parser = new JsonParser();
                User user = gson.fromJson(parser.parse(sender.toString()), User.class);
                Tweet tweet = new Tweet();
                tweet.setText(text);
                tweet.setUser(user);
                tweet.setDisplayType(DisplayType.MESSAGE);
                tweet.setEntities(new Entity());
                tweet.setCreatedAt(createdDate);
                tweetList.add(tweet);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return tweetList;
    }

    @Override
    public String toString(){
        if(getEntities().getMedia() != null){
            if(getEntities().getMedia().size() > 0){
                return getId()+" "+getUser().getScreenName()+" "+getEntities().getMedia().get(0).getMediaUrl();
            }
        }
        return getId()+" "+getUser().getScreenName()+" "+getText()+" "+getFavouritesCount()+" "+getRetweetCount();
    }

}

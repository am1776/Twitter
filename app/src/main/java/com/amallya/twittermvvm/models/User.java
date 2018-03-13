package com.amallya.twittermvvm.models;

import com.amallya.twittermvvm.utils.Utils;
import com.amallya.twittermvvm.data.local.TweetsDatabase;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;
import org.parceler.Parcel;

import java.util.ArrayList;



@Parcel
public class User {

    @SerializedName("screen_name")
    private String screenName; // screen_name

    @SerializedName("name")
    private String name;

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setFollowing(boolean following) {
        isFollowing = following;
    }

    @SerializedName("following")
    private boolean isFollowing;

    @SerializedName("description")
    private String description; // description

    @SerializedName("url")
    private String url; // url

    @SerializedName("followers_count")
    private long followersCount; //followers_count

    @SerializedName("friends_count")
    private long friendsCount; // friends_count

    @SerializedName("listed_count")
    private long listedCount; //listed_count

    @SerializedName("favourites_count")
    private long favoritesCount; // favourites_count

    @SerializedName("verified")
    private boolean verified; // verified

    @SerializedName("statuses_count")
    private long statusesCount; // statuses_count

    @SerializedName("profile_background_image_url")
    private String profileBackgroundImageUrl;  //profile_background_image_url

    @SerializedName("profile_background_color")
    private String profileBackgroundColor; // profile_background_color

    @SerializedName("profile_image_url_https")
    private String profileImageUrl; // profile_image_url_https

    @SerializedName("profile_banner_url")
    private String bannerUrl; // banner_url

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(long followersCount) {
        this.followersCount = followersCount;
    }

    public long getFriendsCount() {
        return friendsCount;
    }

    public void setFriendsCount(long friendsCount) {
        this.friendsCount = friendsCount;
    }

    public long getListedCount() {
        return listedCount;
    }

    public void setListedCount(long listedCount) {
        this.listedCount = listedCount;
    }

    public long getFavoritesCount() {
        return favoritesCount;
    }

    public void setFavoritesCount(long favoritesCount) {
        this.favoritesCount = favoritesCount;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public long getStatusesCount() {
        return statusesCount;
    }

    public void setStatusesCount(long statusesCount) {
        this.statusesCount = statusesCount;
    }

    public String getProfileBackgroundImageUrl() {
        return Utils.editImage(profileBackgroundImageUrl);
    }

    public void setProfileBackgroundImageUrl(String profileBackgroundImageUrl) {
        //profileBackgroundImageUrl = Utils.editImage(profileBackgroundImageUrl);
        this.profileBackgroundImageUrl = profileBackgroundImageUrl;
    }

    public String getProfileBackgroundColor() {
        return profileBackgroundColor;
    }

    public void setProfileBackgroundColor(String profileBackgroundColor) {
        this.profileBackgroundColor = profileBackgroundColor;
    }

    public String getProfileImageUrl() {
        return Utils.editImage(profileImageUrl);
    }

    public void setProfileImageUrl(String profileImageUrl) {
        //profileImageUrl = Utils.editImage(profileImageUrl);
        this.profileImageUrl = profileImageUrl;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public static ArrayList<User> getUserList(String jArrayString){
        JsonParser parser = new JsonParser();
        JsonElement tweetElement = parser.parse(jArrayString);
        JsonArray jArray = tweetElement.getAsJsonArray();
        ArrayList<User> userList = new ArrayList<User>();
        int length = jArray.size();
        for(int i = 0; i < length; i ++){
            Gson gson = new Gson();
            User user = gson.fromJson(jArray.get(i), User.class);
            userList.add(user);
        }
        return userList;
    }

}

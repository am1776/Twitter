package com.amallya.twittermvvm.models;

import com.google.gson.annotations.SerializedName;
import org.parceler.Parcel;

import java.util.List;


@Parcel
public class Entity {
    public List<Media> getMedia() {
        return media;
    }

    public void setMedia(List<Media> media) {
        this.media = media;
    }

    private List<Media> media;

    public List<Extended> getExtendedList() {
        return extendedList;
    }

    public void setExtendedList(List<Extended> extendedList) {
        this.extendedList = extendedList;
    }

    @SerializedName("extended_entities")
    private List<Extended> extendedList;

}

package com.amallya.twittermvvm.utils;

import android.arch.persistence.room.TypeConverter;

import com.amallya.twittermvvm.models.User;
import com.google.gson.Gson;

/**
 * Created by anmallya on 3/15/2018.
 */

public class UserConverter {

    @TypeConverter
    public static User toUser(String user) {
        return (new Gson()).fromJson(user, User.class);
    }

    @TypeConverter
    public static String toString(User user) {
        return (new Gson()).toJson(user).toString();
    }
}

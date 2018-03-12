package com.amallya.twittermvvm.data.local;

import com.raizlabs.android.dbflow.annotation.Database;


@Database(name = TweetsDatabase.NAME, version = TweetsDatabase.VERSION)
public class TweetsDatabase {

    public static final String NAME = "TweetsDatabase";

    public static final int VERSION = 1;
}

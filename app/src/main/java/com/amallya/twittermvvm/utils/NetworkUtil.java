package com.amallya.twittermvvm.utils;

import android.content.Context;
import android.net.ConnectivityManager;

import com.amallya.twittermvvm.data.source.remote.InternetAvailability;

import java.net.InetAddress;

/**
 * Created by anmallya on 3/15/2018.
 */

public class NetworkUtil {
    private static boolean isInternetAvailableHelper() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

    public static void isInternetAvailable(final InternetAvailability callback){
        (new AppExecutors()).networkIO().execute(new Runnable() {
            @Override
            public void run() {
                boolean status = isInternetAvailableHelper()? true: false;
                callback.internetStatus(status);
            }
            });
    }
}


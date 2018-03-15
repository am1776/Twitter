package com.amallya.twittermvvm.utils;

import com.amallya.twittermvvm.data.remote.InternetAvailability;

import java.net.InetAddress;

/**
 * Created by anmallya on 3/15/2018.
 */

public class NetworkUtil {
    private static boolean isInternetAvailableHelper() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
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
            });}
}


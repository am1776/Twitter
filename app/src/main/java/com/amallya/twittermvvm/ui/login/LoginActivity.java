package com.amallya.twittermvvm.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.amallya.twittermvvm.R;
import com.amallya.twittermvvm.data.source.remote.TwitterClient;
import com.codepath.oauth.OAuthLoginActionBarActivity;
import com.amallya.twittermvvm.ui.main.MainActivity;

public class LoginActivity extends OAuthLoginActionBarActivity<TwitterClient> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    public void onLoginSuccess() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }


    @Override
    public void onLoginFailure(Exception e) {
        e.printStackTrace();
    }

    // When login button is clicked.
    public void loginToRest(View view) {
        getClient().connect();
    }

}
package com.amallya.twittermvvm.ui.base;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import com.amallya.twittermvvm.R;
import com.amallya.twittermvvm.models.Response;
import com.amallya.twittermvvm.models.User;

/**
 * Created by anmallya on 3/15/2018.
 */

public abstract class BaseActivity  extends AppCompatActivity{

    protected void handleResponse(Response response){
        switch(response.getErrorCode()){
            case SUCCESS:
                handleSuccess(response);
                break;
            case ERROR:
                handleError(response);
                break;
        }
    }

    protected void showSnackBar(int id, String msg){
        Snackbar mySnackbar = Snackbar.make(findViewById(id),
                msg, Snackbar.LENGTH_SHORT);
        mySnackbar.show();
    }

    public abstract void handleSuccess(Response response);

    public abstract void handleError(Response response);

}

package com.amallya.twittermvvm.ui.base;

/**
 * Created by anmallya on 3/15/2018.
 */
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;

import com.amallya.twittermvvm.models.Response;


public abstract class BaseFragment extends Fragment {
    public void handleResponse(Response response){
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
        Snackbar mySnackbar = Snackbar.make(getView().findViewById(id),
                msg, Snackbar.LENGTH_SHORT);
        mySnackbar.show();
    }

    public abstract void handleSuccess(Response response);

    public abstract void handleError(Response response);
}

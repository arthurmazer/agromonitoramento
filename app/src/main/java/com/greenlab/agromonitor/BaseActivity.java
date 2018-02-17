package com.greenlab.agromonitor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.greenlab.agromonitor.entity.User;
import com.greenlab.agromonitor.managers.SessionManager;

/**
 * Created by arthu on 12/02/2018.
 */

public abstract class BaseActivity extends AppCompatActivity {

    SessionManager sessionManager;


    @Override
    protected void onResume(){
        super.onResume();
        sessionManager = new SessionManager(this);
        Log.d("BaseActivity-Resume","user logged out");
        if (!sessionManager.isUserLoggedIn()){
            Intent it = new Intent(this, LoginActivity .class);
            startActivity(it);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sessionManager = new SessionManager(this);
        Log.d("BaseActivity-Destroyed","user logged out");
        sessionManager.logout();
    }

    public User getSessionUser(){
        sessionManager = new SessionManager(this);
        User user = new User();
        user.setLogin(sessionManager.getUserName());
        user.setId(sessionManager.getUserId());
        return user;
    }
}

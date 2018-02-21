package com.greenlab.agromonitor.managers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.greenlab.agromonitor.DbManager;
import com.greenlab.agromonitor.entity.User;

/**
 * Created by arthu on 09/02/2018.
 */

public class UserManager {

    DbManager dbManager;

    public UserManager(Context ctx){
        this.dbManager = DbManager.getInstance(ctx);
    }

    public User login(User user){
        return this.dbManager.userDAO().login(user.login,user.password);

    }

    public void insert(User user){
        this.dbManager.userDAO().insertUser(user);
    }

    @SuppressLint("StaticFieldLeak")
    public void update(final User user){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                dbManager.userDAO().updateUser(user);
                return null;
            }
        }.execute();
        //this.dbManager.userDAO().updateUser(user);
    }
}

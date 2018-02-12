package com.greenlab.agromonitor.managers;

import android.content.Context;
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

    public boolean login(User user){
        if(this.dbManager.userDAO().login(user.login,user.password) != null){
            Log.d("aqui", "what??? " + user.login + "/" + user.password);
         return true;
        }else{
            return false;
        }
    }

    public void insert(User user){
        this.dbManager.userDAO().insertUser(user);
    }
}

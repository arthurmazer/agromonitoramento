package com.greenlab.agromonitor.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.greenlab.agromonitor.entity.User;
import com.greenlab.agromonitor.utils.Constants;

/**
 * Created by arthu on 12/02/2018.
 */

public class SessionManager {

    SharedPreferences sharedPreferences;
    Editor editor;
    Context mContext;

    public SessionManager(Context context){
        this.mContext = context;
        sharedPreferences = mContext.getSharedPreferences(Constants.SHARED_PREF, 0);
        editor = sharedPreferences.edit();
    }

    public void createUserSession(User user){
        editor.putBoolean(Constants.SP_ISLOGGEDIN,true);
        editor.putString(Constants.SP_LOGIN, user.getLogin());
        editor.commit();
    }

    public boolean isUserLoggedIn(){
        if (sharedPreferences.getBoolean(Constants.SP_ISLOGGEDIN,false)){
            return true;
        }else{
            return false;
        }
    }

    public String getUserName(){
        return sharedPreferences.getString(Constants.SP_LOGIN, "");
    }

    public void logout(){
        editor.putBoolean(Constants.SP_ISLOGGEDIN,false);
        editor.putString(Constants.SP_LOGIN, "");
        editor.commit();
    }



}


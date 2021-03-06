package com.mazer.agromonitor.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.mazer.agromonitor.entity.User;
import com.mazer.agromonitor.utils.Constants;

/**
 * Created by arthu on 12/02/2018.
 */

public class SessionManager {

    SharedPreferences sharedPreferences;
    Editor editor;
    Context mContext;
    static final int NO_ID = -1;

    public SessionManager(Context context){
        this.mContext = context;
        sharedPreferences = mContext.getSharedPreferences(Constants.SHARED_PREF, 0);
        editor = sharedPreferences.edit();
    }

    public void createUserSession(User user){
        editor.putBoolean(Constants.SP_IS_LOGGED_IN,true);
        editor.putString(Constants.SP_USER_LOGIN, user.getLogin());
        editor.putInt(Constants.SP_USER_ID, user.getId());
        editor.apply();
    }

    public void setCurrentProject(int idProject, String nameProject){
        editor.putInt(Constants.SP_PROJECT_OPEN, idProject);
        editor.putString(Constants.SP_NAME_PROJECT_OPEN, nameProject);
        editor.apply();
    }

    public int getCurrentProject(){
        return sharedPreferences.getInt(Constants.SP_PROJECT_OPEN, -1);
    }

    public String getNameCurrentProject(){
        return sharedPreferences.getString(Constants.SP_NAME_PROJECT_OPEN, "");
    }

    public String getCurrentEmail(){
        return sharedPreferences.getString(Constants.SP_USER_EMAIL,"");
    }

    public void setCurrentEmail(String email){
        editor.putString(Constants.SP_USER_EMAIL, email);
        editor.apply();
    }

    public boolean isUserLoggedIn(){
        return sharedPreferences.getBoolean(Constants.SP_IS_LOGGED_IN,false);
    }

    public String getUserName(){
        return sharedPreferences.getString(Constants.SP_USER_LOGIN, "");
    }

    public int getUserId(){
        return sharedPreferences.getInt(Constants.SP_USER_ID, NO_ID);
    }

    public void logout(){
        editor.putBoolean(Constants.SP_IS_LOGGED_IN,false);
        editor.putString(Constants.SP_USER_LOGIN, "");
        editor.putInt(Constants.SP_USER_ID, NO_ID);
        editor.apply();
    }


}


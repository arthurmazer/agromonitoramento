package com.greenlab.agromonitor.entity;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.greenlab.agromonitor.interfaces.UserLoginInterface;
import com.greenlab.agromonitor.managers.UserManager;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by arthu on 09/02/2018.
 */

@Entity(indices = {@Index(value = "login",
        unique = true)})
public class User {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String login;
    public String password;
    public String listOfProjects; //ArrayList<Project> to Json


    public User(){
        this.listOfProjects = "";
    }

    public User(String login, String password){
        this.login = login;
        this.password = password;
        this.listOfProjects = "";
    }

    public User login(Context ctx){
        UserManager userManager = new UserManager(ctx);
        return userManager.login(this);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Project> getListOfProjects() {
        if (this.listOfProjects.isEmpty())
            return new ArrayList<>();
        Type listType = new TypeToken<ArrayList<Project>>(){}.getType();
        return new Gson().fromJson(this.listOfProjects, listType);
    }

    public void setListOfProjects(ArrayList<Project> listOfProjects) {
        String jsonProjects = new Gson().toJson(listOfProjects);
        this.listOfProjects = jsonProjects;
    }

}

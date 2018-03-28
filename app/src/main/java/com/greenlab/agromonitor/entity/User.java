package com.greenlab.agromonitor.entity;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.util.Log;

import com.google.firebase.database.Exclude;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.greenlab.agromonitor.HomeActivity;
import com.greenlab.agromonitor.interfaces.GetAllProjectsOfUser;
import com.greenlab.agromonitor.interfaces.UserLoginInterface;
import com.greenlab.agromonitor.managers.UserManager;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by arthu on 09/02/2018.
 *
 *
 *User
    idUser PK
    login
    password
 */

@Entity(tableName = "user",
        indices = {@Index(value = "login",
        unique = true)})
public class User {

    @PrimaryKey(autoGenerate = true)
    @Exclude
    public int id;

    public String login;
    public String phoneNumber;
    public String password;

    public User(){
    }

    public User(String login, String password){
        this.login = login;
        this.password = password;
    }

    public User login(Context ctx){
        UserManager userManager = new UserManager(ctx);
        User user = userManager.login(this);
        return userManager.login(this);
    }

    public void getListOfProjects(Context ctx, GetAllProjectsOfUser getAllProjectsOfUser){
        UserManager userManager = new UserManager(ctx);
        userManager.getListOfProjects(this.id, getAllProjectsOfUser);
    }

    public long saveProject(Context ctx, Project project, ArrayList<String> listOfProducts){
        UserManager userManager = new UserManager(ctx);
        return userManager.saveProject(project,listOfProducts);
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

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword(){
        return this.password;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}

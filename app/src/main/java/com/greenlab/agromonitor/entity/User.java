package com.greenlab.agromonitor.entity;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.util.Log;

import com.greenlab.agromonitor.interfaces.UserLoginInterface;
import com.greenlab.agromonitor.managers.UserManager;

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

    @Embedded
    ArrayList<Project> listOfProjects;


    public User(){}

    public User(String login, String password){
        this.login = login;
        this.password = password;
    }

    public User(String login, String password, ArrayList<Project> listOfProjects){
        this.login = login;
        this.password = password;
        this.listOfProjects = listOfProjects;
    }

    public User login(Context ctx){
        UserManager userManager = new UserManager(ctx);
        Log.d("aqui", "tentano logar...");
        if (userManager.login(this)){
            return this;
        }else{
            return null;
        }
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
        return listOfProjects;
    }

    public void setListOfProjects(ArrayList<Project> listOfProjects) {
        this.listOfProjects = listOfProjects;
    }

}

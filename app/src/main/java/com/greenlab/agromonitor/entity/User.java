package com.greenlab.agromonitor.entity;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import android.content.Context;

import com.google.firebase.database.Exclude;
import com.greenlab.agromonitor.interfaces.GetAllProjectsOfUser;
import com.greenlab.agromonitor.managers.UserManager;

import java.util.ArrayList;

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

    public long saveProject(Context ctx, Project project, ArrayList<Variables> listOfProducts){
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

package com.greenlab.agromonitor.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by mazer on 1/19/2018.
 */

@Entity
public class Project {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String projectName;
    private String creationDate;
    private int cultureType; //0 - Cana, 1 - Soja
    private String listOfProducts; //Json

    public Project(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public int getCultureType() {
        return cultureType;
    }

    public void setCultureType(int cultureType) {
        this.cultureType = cultureType;
    }

    public String getListOfProducts() {
        return listOfProducts;
    }

    public void setListOfProducts(String listOfProducts) {
        this.listOfProducts = listOfProducts;
    }
}

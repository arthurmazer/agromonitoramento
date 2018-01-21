package com.greenlab.agromonitor.models;

import java.util.Date;

/**
 * Created by mazer on 1/19/2018.
 */

public class Project {

    private int id;
    private String projectName;
    private Date creationDate;
    private int cultureType;

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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public int getCultureType() {
        return cultureType;
    }

    public void setCultureType(int cultureType) {
        this.cultureType = cultureType;
    }
}

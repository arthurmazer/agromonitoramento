package com.greenlab.agromonitor.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by mazer on 1/19/2018.
 *
 */


@Entity(tableName = "project",
        foreignKeys =
        @ForeignKey(entity = User.class,
                parentColumns = "id",
                childColumns = "idUser",
                onDelete = CASCADE)
)
public class Project {

    @PrimaryKey(autoGenerate = true)
    private int id;

    //Foreign Key From user
    public int idUser;

    private String projectName;
    private String creationDate;
    private int cultureType; //0 - Cana, 1 - Soja

    public Project(){}

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

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
}

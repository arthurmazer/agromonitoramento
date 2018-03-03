package com.greenlab.agromonitor.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;

import com.greenlab.agromonitor.interfaces.GetSpreadsheetValues;
import com.greenlab.agromonitor.managers.UserManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Ignore
    private List<Product> listOfProducts;
    @Ignore
    private List<SpreadsheetValues> listOfProjectProduct;

    public Project(){
        listOfProducts = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public List<SpreadsheetValues> getSpreadSheetValues(Context ctx){
        UserManager userManager = new UserManager(ctx);
        return userManager.getSpreadsheetValues(this.id);
    }


    public void insertProjectProduct(Context ctx, ProjectProduct projectProduct){
        UserManager userManager = new UserManager(ctx);
        userManager.insertProjectProduct(projectProduct);
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

    public List<Product> getListOfProducts() {
        return listOfProducts;
    }

    public void setListOfProducts(List<Product> listOfProducts) {
        this.listOfProducts = listOfProducts;
    }

    public List<SpreadsheetValues> getListOfProjectProduct() {
        return listOfProjectProduct;
    }

    public void setListOfProjectProduct(List<SpreadsheetValues> listOfProjectProduct) {
        this.listOfProjectProduct = listOfProjectProduct;
    }
}

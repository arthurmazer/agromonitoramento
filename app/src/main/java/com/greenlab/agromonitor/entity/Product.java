package com.greenlab.agromonitor.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by arthu on 2/5/2018.
 */

@Entity(tableName = "product",
        foreignKeys =
        @ForeignKey(entity = Project.class,
                parentColumns = "id",
                childColumns = "idProject",
                onDelete = CASCADE)
)
public class Product {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int idProject;
    public String product;

    public Product(){}


    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getIdProject() {
        return idProject;
    }

    public void setIdProject(int idProject) {
        this.idProject = idProject;
    }
}

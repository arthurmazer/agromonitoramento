package com.greenlab.agromonitor.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

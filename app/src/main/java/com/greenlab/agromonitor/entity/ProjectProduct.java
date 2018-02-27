package com.greenlab.agromonitor.entity;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by monitorapc on 22-Feb-18.
 */

@Entity(tableName = "project_product",
        foreignKeys = {
        @ForeignKey(entity = Project.class,
                parentColumns = "id",
                childColumns = "idProject",
                onDelete = CASCADE),
        @ForeignKey(entity = Product.class,
                parentColumns = "id",
                childColumns = "idProduct",
                onDelete = CASCADE)
        }
)
public class ProjectProduct {

    @PrimaryKey(autoGenerate = true)
    public int id;

    private int idProject;
    private int idProduct;
    private float value;

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

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}

package com.mazer.agromonitor.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

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
    private long timestamp;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timeStamp) {
        this.timestamp = timeStamp;
    }
}

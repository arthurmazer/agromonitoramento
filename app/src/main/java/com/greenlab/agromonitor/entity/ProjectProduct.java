package com.greenlab.agromonitor.entity;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by monitorapc on 22-Feb-18.
 */

@Entity(foreignKeys =
        @ForeignKey(entity = Project.class,
                parentColumns = "id",
                childColumns = "idProject",
                onDelete = CASCADE)
)
public class ProjectProduct {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int idProject;
    @Embedded
    Product product;

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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

}

package com.mazer.agromonitor.entity;

/**
 * Created by arthu on 02/03/2018.
 */

public class SpreadsheetValues {

    private float value;
    private int id; //idProduct
    private int idProject;
    private String product;
    private int idProjectProduct;
    private long timestamp;

    public SpreadsheetValues() {
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdProject() {
        return idProject;
    }

    public void setIdProject(int idProject) {
        this.idProject = idProject;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getIdProjectProduct() {
        return idProjectProduct;
    }

    public void setIdProjectProduct(int idProjectProduct) {
        this.idProjectProduct = idProjectProduct;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

package com.greenlab.agromonitor.entity;

/**
 * Created by arthu on 2/5/2018.
 */

public class Product {

    private int idCategory;
    private String title;
    private float productValue;

    public Product(int idCategory, String title, float productValue) {
        this.idCategory = idCategory;
        this.title = title;
        this.productValue = productValue;
    }

    public int getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getProductValue() {
        return productValue;
    }

    public void setProductValue(float productValue) {
        this.productValue = productValue;
    }
}

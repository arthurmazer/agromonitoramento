package com.greenlab.agromonitor.entity;

/**
 * Created by arthu on 2/5/2018.
 */

public class Product {

    public String product;
    public float value;

    public Product(String product, float value) {
        this.product = product;
        this.value = value;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}

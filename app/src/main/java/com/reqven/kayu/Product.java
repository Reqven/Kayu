package com.reqven.kayu;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class Product {

    private String product_name;
    private String ingredients_text;
    private String barcode;
    private ArrayList<Nutriment> nutriments;
    private ArrayList<Additive> additives;

    public Product() {
        nutriments = new ArrayList<>();
    }

    public Product(String barcode) {
        nutriments = new ArrayList<>();
        this.barcode = barcode;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getIngredients_text() {
        return ingredients_text;
    }

    public void setIngredients_text(String ingredients_text) {
        this.ingredients_text = ingredients_text;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public ArrayList<Nutriment> getNutriments() {
        return nutriments;
    }

    public void setNutriments(ArrayList<Nutriment> nutriments) {
        this.nutriments = nutriments;
    }

    public void addNutriment(Nutriment nutriment) {
        nutriments.add(nutriment);
    }
}

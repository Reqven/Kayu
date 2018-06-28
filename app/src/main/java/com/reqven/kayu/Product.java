package com.reqven.kayu;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Product {

    private String name;
    private String barcode;
    private Boolean found;
    private Boolean complete;
    private Nutriment salt;
    private Nutriment sugar;
    private Nutriment fat;
    private Nutriment saturated;
    private Nutriment protein;
    private Nutriment fibre;
    private boolean palm_oil;
    private ArrayList<Nutriment> nutriments;
    private ArrayList<Additive> additives;

    public Product() {
        nutriments = new ArrayList<>();
        additives  = new ArrayList<>();
        palm_oil = false;
        found = true;
    }

    public Product(String barcode) {
        super();
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Nutriment getSalt() {
        return salt;
    }

    public void setSalt(Nutriment salt) {
        this.salt = salt;
    }

    public Nutriment getSugar() {
        return sugar;
    }

    public void setSugar(Nutriment sugar) {
        this.sugar = sugar;
    }

    public Nutriment getFat() {
        return fat;
    }

    public void setFat(Nutriment fat) {
        this.fat = fat;
    }

    public Nutriment getSaturated() {
        return saturated;
    }

    public void setSaturated(Nutriment saturated) {
        this.saturated = saturated;
    }

    public Nutriment getProtein() {
        return protein;
    }

    public void setProtein(Nutriment protein) {
        this.protein = protein;
    }

    public Nutriment getFibre() {
        return fibre;
    }

    public void setFibre(Nutriment fibre) {
        this.fibre = fibre;
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

    public ArrayList<Additive> getAdditives() {
        return additives;
    }

    public void setAdditives(ArrayList<Additive> additives) {
        this.additives = additives;
    }

    public void addAdditive(Additive additive) {
        additives.add(additive);
    }

    public boolean containsPalmOil() {
        return palm_oil;
    }

    public void setPalmOil(boolean bool) {
        this.palm_oil = bool;
    }

    public Boolean isFound() {
        return found;
    }

    public void setIsFound(Boolean found) {
        this.found = found;
    }

    public Boolean isComplete() {
        return complete;
    }

    public void setIsComplete(Boolean complete) {
        this.complete = complete;
    }
}

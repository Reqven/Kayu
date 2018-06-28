package com.reqven.kayu;

import android.text.TextUtils;

public class Nutriment {
    private String label;
    private String name;
    private Float quantity;
    private String unit;
    private String level;
    private int icon;

    public Nutriment() {
    }

    public Nutriment(String name, Float quantity, String unit, String level) {
        if (unit.isEmpty()) {
            unit = "g";
        }
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.level = level;
    }

    public String getLabel() {
        if (TextUtils.isEmpty(label)) {
            return name;
        }
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        if (unit.isEmpty()) {
            unit = "g";
        }
        this.unit = unit;
    }

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public Boolean matchPreferences(String preference) {
        return true;
    }
}

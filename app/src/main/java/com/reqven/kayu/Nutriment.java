package com.reqven.kayu;

public class Nutriment {
    private String name;
    private Float quantity;
    private String unit;
    private String level;

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
}

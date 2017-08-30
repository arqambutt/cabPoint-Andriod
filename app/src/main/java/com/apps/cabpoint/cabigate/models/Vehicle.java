package com.apps.cabpoint.cabigate.models;

/**
 * Created by Abdul Ghani on 7/18/2017.
 */

public class Vehicle {
   private String Model;
   private String type;
    private String icon;

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "Model='" + Model + '\'' +
                ", type='" + type + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}

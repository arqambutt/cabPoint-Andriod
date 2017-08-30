package com.apps.cabpoint.cabigate.models;

/**
 * Created by Abdul Ghani on 6/6/2017.
 */

public class Car {
    private int id;
    private int imageResource;
    private String name;
    private int maxSize;
    private double minFare;

    public Car() {
    }

    public Car(int id, int imageResource, String name) {
        this.id = id;
        this.imageResource = imageResource;
        this.name = name;
    }

    public Car(int id, int imageResource, String name, int maxSize, double minFare) {
        this.id = id;
        this.imageResource = imageResource;
        this.name = name;
        this.maxSize = maxSize;
        this.minFare = minFare;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public double getMinFare() {
        return minFare;
    }

    public void setMinFare(double minFare) {
        this.minFare = minFare;
    }
    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", imageResource=" + imageResource +
                ", name='" + name + '\'' +
                '}';
    }
}

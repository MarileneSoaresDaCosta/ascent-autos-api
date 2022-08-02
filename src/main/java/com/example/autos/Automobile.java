package com.example.autos;

import java.util.Objects;

public class Automobile {
    private int year;
    public String make;
    public String model;
    public String color;
    public String owner;
    public String vin;

    // required
    public Automobile(int year, String make, String model, String vin){
        this.year = year;
        this.make = make;
        this.model = model;
        this.vin = vin;
    }

    public Automobile() {
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    @Override
    public String toString() {
        return "Automobile{" +
                "year=" + year +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", color='" + color + '\'' +
                ", owner='" + owner + '\'' +
                ", vin='" + vin + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Automobile that = (Automobile) o;
        return year == that.year && Objects.equals(make, that.make) && Objects.equals(model, that.model) && Objects.equals(color, that.color) && Objects.equals(owner, that.owner) && Objects.equals(vin, that.vin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, make, model, color, owner, vin);
    }
}

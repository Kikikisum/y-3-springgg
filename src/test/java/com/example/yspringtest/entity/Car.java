package com.example.yspringtest.entity;

public class Car {

    private String brand;

    private double price;

    private String owner;

    public Car(){}

    public Car(String brand, double price, String owner) {
        this.brand = brand;
        this.price = price;
        this.owner = owner;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Car{" +
                "brand='" + brand + '\'' +
                ", price=" + price +
                ", owner='" + owner + '\'' +
                '}';
    }
}

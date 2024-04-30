package com.example.socialpuig;

public class Product {
    private String name;
    private double price;

    public Product() {
        // Constructor vacío requerido por Firebase
    }

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // Agregar getters y setters según sea necesario
}

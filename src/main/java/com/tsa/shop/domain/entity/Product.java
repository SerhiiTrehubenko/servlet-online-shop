package com.tsa.shop.domain.entity;

import java.sql.Date;

public class Product {
    private Long id;
    private String name;
    private double price;
    private Date date;

    public Product() {
    }

    public Product(String name, double price) {
        this(null, name, price, null);
    }

    public Product(Long id, String name, double price) {
        this(id, name, price, null);
    }

    public Product(Long id, String name, double price, Date date) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", date=" + date +
                '}';
    }
}

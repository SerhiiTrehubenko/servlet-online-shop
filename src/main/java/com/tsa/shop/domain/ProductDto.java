package com.tsa.shop.domain;

import java.sql.Timestamp;

public class ProductDto {
    private Long id;
    private String name;
    private double price;
    private Timestamp date;
    private String description;

    public ProductDto() {
    }

    public ProductDto(Long id,
                      String name,
                      double price,
                      Timestamp date,
                      String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.date = date;
        this.description = description;
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

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ProductDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", date=" + date +
                '}';
    }
}

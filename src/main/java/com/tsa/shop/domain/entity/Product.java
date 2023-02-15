package com.tsa.shop.domain.entity;

import com.tsa.shop.orm.annotation.*;

import java.sql.Date;

@Entity
@Table(name = "products")
public class Product {
    @Id(name = "product_id")
    private Long id;

    @Column(name = "product_name")
    private String name;
    @Column(name = "product_price")
    private double price;
    @Column(name = "creationdate")
    @Immutable
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
}

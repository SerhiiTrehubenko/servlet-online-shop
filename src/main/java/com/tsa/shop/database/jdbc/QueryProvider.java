package com.tsa.shop.database.jdbc;

public enum QueryProvider {
    PRODUCT_FIND_All("SELECT * FROM products;"),
    PRODUCT_FIND_BY_ID("SELECT * FROM products WHERE product_id=?;"),
    PRODUCT_UPDATE("UPDATE products SET product_name=?, product_price=?, description=? WHERE product_id=?;"),
    PRODUCT_DELETE("DELETE FROM products WHERE product_id=?;"),
    PRODUCT_FIND_BY_CRITERIA("SELECT * FROM products WHERE product_name LIKE ? OR description LIKE ?;"),
    PRODUCT_INSERT("INSERT INTO products (product_name, product_price, creationdate, description) VALUES (?,?,?,?);");


    private final String query;
    QueryProvider(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}

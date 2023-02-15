package com.tsa.shop.servlets.enums;

public enum UriPageConnector {
    HOME("/home", "home.html"),
    HOME_SLASH("/", "home.html"),
    PRODUCT_ADD("/products/add", "add-product.html"),
    PRODUCTS_EDIT("/products/edit", "edit-product.html"),
    PRODUCTS_EDIT_SEND("/products/edit/send", "products.html"),
    PRODUCT_GET_ALL("/products", "products.html"),
    PRODUCT_DELETE("/products/delete", "products.html");

    private final String uri;
    private final String htmlPage;

    public String getUri() {
        return uri;
    }
    public String getHtmlPage() {
        return htmlPage;
    }
    UriPageConnector(String uri, String htmlPage) {
        this.uri = uri;
        this.htmlPage = htmlPage;
    }
}

package com.tsa.shop.domain;

public enum UriPageConnector {
    HOME("/home", UriPageConnector.HOME_PAGE),
    SLASH("/", UriPageConnector.HOME_PAGE),
    PRODUCTS_ADD("/products/add", UriPageConnector.ADD_PRODUCT_PAGE),
    PRODUCTS_UPDATE("/products/edit", UriPageConnector.EDIT_PRODUCT_PAGE),
    PRODUCTS_POST_UPDATE("/products/edit/send", UriPageConnector.PRODUCTS_PAGE),
    PRODUCTS("/products", UriPageConnector.PRODUCTS_PAGE),
    PRODUCTS_DELETE("/products/delete", UriPageConnector.PRODUCTS_PAGE),
    ERROR_PAGE("stub-url", "error-page.html"),
    LOG_IN_PAGE("/login", "log-in-page.html");

    final static String PRODUCTS_PAGE = "products.html";
    final static String HOME_PAGE = "home.html";
    final static String EDIT_PRODUCT_PAGE = "edit-product.html";
    final static String ADD_PRODUCT_PAGE = "add-product.html";
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


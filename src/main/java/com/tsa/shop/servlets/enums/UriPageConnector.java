package com.tsa.shop.servlets.enums;

public enum UriPageConnector {
    HOME("/home", Pages.HOME_PAGE),
    SLASH("/", Pages.HOME_PAGE),
    PRODUCTS_ADD("/products/add", Pages.ADD_PRODUCT_PAGE),
    PRODUCTS_EDIT("/products/edit", Pages.EDIT_PRODUCT_PAGE),
    PRODUCTS_EDIT_SEND("/products/edit/send", Pages.PRODUCTS_PAGE),
    PRODUCTS("/products", Pages.PRODUCTS_PAGE),
    PRODUCTS_DELETE("/products/delete", Pages.PRODUCTS_PAGE);

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

    private static final class Pages{
        public final static String PRODUCTS_PAGE = "products.html";
        public final static String HOME_PAGE = "home.html";
        public final static String EDIT_PRODUCT_PAGE = "edit-product.html";
        public final static String ADD_PRODUCT_PAGE = "add-product.html";
    }
}


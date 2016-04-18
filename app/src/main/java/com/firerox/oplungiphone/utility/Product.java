package com.firerox.oplungiphone.utility;

public class Product {

    private int id;
    private String categoryId;
    private String productId;
    private String categoryName;
    private String productTitle;
    private String productImage;
    private String productDetail;
    private String productState;
    private String productPrice;
    private String productCode;

    public Product() {

    }

    public Product(String categoryId) {
        this.categoryId = categoryId;
    }

    public Product(String categoryId, String productId, String categoryName, String productTitle, String productImage,
                   String productDetail,  String productState, String productPrice, String productCode) {
        this.categoryId = categoryId;
        this.productId = productId;
        this.categoryName = categoryName;
        this.productTitle = productTitle;
        this.productImage = productImage;
        this.productDetail = productDetail;
        this.productState = productState;
        this.productPrice = productPrice;
        this.productCode = productCode;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductDetail() {
        return productDetail;
    }

    public void setProductDetail(String productDetail) {
        this.productDetail = productDetail;
    }

    public String getProductState() {
        return productState;
    }

    public void setProductState(String productState) {
        this.productState = productState;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
}

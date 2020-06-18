package com.netmart.netmart_main.Model;

public class Products {

    private String name, qty, description, price, productimage, category, Pid, date, time, nameLower, sellerid, sellerName;
    private Boolean status;

    public Products(){

    }

    public Products(String name, String qty, String description, String price, String productimage, String category, String pid, String date, String time, String nameLower, String sellerid, String sellerName, Boolean status) {
        this.name = name;
        this.qty = qty;
        this.description = description;
        this.price = price;
        this.productimage = productimage;
        this.category = category;
        Pid = pid;
        this.date = date;
        this.time = time;
        this.nameLower = nameLower;
        this.sellerid = sellerid;
        this.sellerName = sellerName;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProductimage() {
        return productimage;
    }

    public void setProductimage(String productimage) {
        this.productimage = productimage;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPid() {
        return Pid;
    }

    public void setPid(String pid) {
        Pid = pid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNameLower() {
        return nameLower;
    }

    public void setNameLower(String nameLower) {
        this.nameLower = nameLower;
    }

    public String getSellerid() {
        return sellerid;
    }

    public void setSellerid(String sellerid) {
        this.sellerid = sellerid;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}

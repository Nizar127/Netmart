package com.netmart.netmart_main.Model;

public class Products {

    private String name, description, price, image, category, pid, date, time, nameLower;

    public Products(){

    }

    public Products(String name, String nameLower, String description, String price, String image, String category, String pid, String date, String time) {
        this.name = name;
        this.nameLower = nameLower;
        this.description = description;
        this.price = price;
        this.image = image;
        this.category = category;
        this.pid = pid;
        this.date = date;
        this.time = time;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getNameLower() { return nameLower; }

    public void setNameLower(String nameLower) { this.nameLower = nameLower; }

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
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
}

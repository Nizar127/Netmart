package com.netmart.netmart_main.Model;

public class Users {

     String username, phone, password, image, email, fullname;
    Boolean status;

    public Users() {

    }

    public Users(String username, String phone, String password, String image, String fullname, String email, Boolean status) {
        this.username = username;
        this.phone = phone;
        this.password = password;
        this.image = image;
        this.fullname = fullname;
        this.email = email;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}

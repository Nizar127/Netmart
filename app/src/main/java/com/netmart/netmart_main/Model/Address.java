package com.netmart.netmart_main.Model;

public class Address {

    private String houseNo, street, houseArea, city, postcode, state;

    public Address(){
    }

    public Address(String houseNo, String street, String houseArea, String city, String postcode, String state) {
        this.houseNo = houseNo;
        this.street = street;
        this.houseArea = houseArea;
        this.city = city;
        this.postcode = postcode;
        this.state = state;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseArea() {
        return houseArea;
    }

    public void setHouseArea(String houseArea) {
        this.houseArea = houseArea;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}

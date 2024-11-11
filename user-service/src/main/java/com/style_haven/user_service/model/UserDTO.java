package com.style_haven.user_service.model;

import jakarta.validation.constraints.Size;


public class UserDTO {

    private Integer userid;

    @Size(max = 255)
    private String fname;

    @Size(max = 255)
    private String lname;

    @Size(max = 255)
    private String state;

    @Size(max = 255)
    private String city;

    @Size(max = 255)
    private String street;

    @Size(max = 255)
    private String postalCode;

    @Size(max = 255)
    private String email;

    @Size(max = 255)
    private String username;

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(final Integer userid) {
        this.userid = userid;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(final String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(final String lname) {
        this.lname = lname;
    }

    public String getState() {
        return state;
    }

    public void setState(final String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(final String street) {
        this.street = street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(final String postalCode) {
        this.postalCode = postalCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

}

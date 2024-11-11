package com.style_haven.admin_service.model;

import jakarta.validation.constraints.Size;


public class AdminDTO {

    private Long adminid;

    @Size(max = 255)
    private String username;

    @Size(max = 255)
    private String password;

    @Size(max = 255)
    private String email;

    public Long getAdminid() {
        return adminid;
    }

    public void setAdminid(final Long adminid) {
        this.adminid = adminid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

}

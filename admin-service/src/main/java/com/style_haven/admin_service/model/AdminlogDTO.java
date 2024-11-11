package com.style_haven.admin_service.model;

import jakarta.validation.constraints.Size;


public class AdminlogDTO {

    private Integer adminlogId;

    @Size(max = 255)
    private String timestamp;

    @Size(max = 255)
    private String action;

    @AdminlogAdminUnique
    private Long admin;

    public Integer getAdminlogId() {
        return adminlogId;
    }

    public void setAdminlogId(final Integer adminlogId) {
        this.adminlogId = adminlogId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final String timestamp) {
        this.timestamp = timestamp;
    }

    public String getAction() {
        return action;
    }

    public void setAction(final String action) {
        this.action = action;
    }

    public Long getAdmin() {
        return admin;
    }

    public void setAdmin(final Long admin) {
        this.admin = admin;
    }

}

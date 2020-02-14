package com.mUI.microserviceUI.beans;

import javax.validation.constraints.NotEmpty;

public class AddShopDTO {

    @NotEmpty
    private String merchantName;
    @NotEmpty
    private String category;
    @NotEmpty
    private String email;
    @NotEmpty
    private String address;
    private Integer userId;
    @NotEmpty
    private String maxPoints;
    private String longitude;
    private String latitude;

    public AddShopDTO() {
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(String maxPoints) {
        this.maxPoints = maxPoints;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "AddShopDTO{" +
                "merchantName='" + merchantName + '\'' +
                ", category='" + category + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", userId=" + userId +
                ", maxPoints='" + maxPoints + '\'' +
                '}';
    }
}

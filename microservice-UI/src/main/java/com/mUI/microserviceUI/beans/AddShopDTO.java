package com.mUI.microserviceUI.beans;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class AddShopDTO {

    @NotEmpty
    private String merchantName;
    @NotNull
    private Integer categoryId;
    @NotEmpty
    private String email;
    @NotEmpty
    private String address;
    private Integer userId;
    private Integer maxPoints;
    private String longitude;
    private String latitude;
    private String points;

    public AddShopDTO() {
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
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

    public Integer getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(Integer maxPoints) {
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

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
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
                ", categoryId='" + categoryId + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", userId=" + userId +
                ", maxPoints='" + maxPoints + '\'' +
                '}';
    }
}

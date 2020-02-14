package com.mUI.microserviceUI.beans;

public class UserBean {

    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private RoleBean userRole;
    private String resetToken;
    private boolean merchantOrNot;
    private boolean newsletterSuscriber;
    private String mapsAddress;
    private String address;
    private String longitude;
    private String latitude;

    public UserBean() {
    }

    public boolean isNewsletterSuscriber() {
        return newsletterSuscriber;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMapsAddress() {
        return mapsAddress;
    }

    public void setMapsAddress(String mapsAddress) {
        this.mapsAddress = mapsAddress;
    }

    public void setNewsletterSuscriber(boolean newsletterSuscriber) {
        this.newsletterSuscriber = newsletterSuscriber;
    }

    public boolean isMerchantOrNot() {
        return merchantOrNot;
    }

    public void setMerchantOrNot(boolean merchantOrNot) {
        this.merchantOrNot = merchantOrNot;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public RoleBean getUserRole() {
        return userRole;
    }

    public void setUserRole(RoleBean userRole) {
        this.userRole = userRole;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

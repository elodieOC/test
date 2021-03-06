package com.mmailing.microservicemailing.beans;

public class UserBean {

    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private RoleBean userRole;
    private String resetToken;
    private String tokenDate;
    private boolean newsletterSuscriber;

    public UserBean() {
    }

    public boolean isNewsletterSuscriber() {
        return newsletterSuscriber;
    }

    public void setNewsletterSuscriber(boolean newsletterSuscriber) {
        this.newsletterSuscriber = newsletterSuscriber;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public String getTokenDate() {
        return tokenDate;
    }

    public void setTokenDate(String tokenDate) {
        this.tokenDate = tokenDate;
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

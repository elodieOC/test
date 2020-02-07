package com.mUI.microserviceUI.beans;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class EditUserDTO {

    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private boolean newsletterSuscriber;

    public EditUserDTO() {
    }

    public boolean isNewsletterSuscriber() {
        return newsletterSuscriber;
    }

    public void setNewsletterSuscriber(boolean newsletterSuscriber) {
        this.newsletterSuscriber = newsletterSuscriber;
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

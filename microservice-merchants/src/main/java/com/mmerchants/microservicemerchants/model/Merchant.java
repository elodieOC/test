package com.mmerchants.microservicemerchants.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.sql.Timestamp;


@Entity
@Table(name = "merchants")
public class Merchant {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType. IDENTITY )
    private Integer id;

    @NotEmpty
    @Column(name="merchant_name", unique = true)
    private String merchantName;

    @NotEmpty
    @Column(name="email", unique = true)
    private String email;

    @NotEmpty
    @Column(name="category")
    private String category;

    @NotEmpty
    @Column(name="address")
    private String address;

    @NotEmpty
    @Size(min=8, message="Veuillez saisir un mot de passe d'au moins 8 caractères")
    @Column(name="password")
    private String password;

    @Column(name = "reset_token")
    private String resetToken;

    @Column(name = "token_date")
    private Timestamp tokenDate;

    public Merchant() {
    }

    public Timestamp getTokenDate() {
        return tokenDate;
    }

    public void setTokenDate(Timestamp tokenDate) {
        this.tokenDate = tokenDate;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public String getUserName() {
        return merchantName;
    }

    public void setUserName(String merchantName) {
        this.merchantName = merchantName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Merchant{" +
                "id=" + id +
                ", merchantName='" + merchantName + '\'' +
                ", category='" + category + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}

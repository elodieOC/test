package com.mmerchants.microservicemerchants.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


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
    @Column(name="address")
    private String address;

    @NotEmpty
    @Column(name="longitude")
    private String longitude;
    @NotEmpty
    @Column(name="latitude")
    private String latitude;


    @Column(name = "userId")
    private Integer userId;

    @NotNull
    @Column(name="max_points")
    private Integer maxPoints;


    @ManyToOne
    @JoinColumn(name="id_category")
    private Category category;

    public Merchant() {
    }

    public Merchant(MerchantDTO dto) {
        this.setUserId(dto.getUserId());
        this.setEmail(dto.getEmail());
        this.setAddress(dto.getAddress());
        this.setMerchantName(dto.getMerchantName());
        this.setMaxPoints(dto.getMaxPoints());
        this.setLatitude(dto.getLatitude());
        this.setLongitude(dto.getLongitude());
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(Integer maxPoints) {
        this.maxPoints = maxPoints;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Merchant{" +
                "id=" + id +
                ", merchantName='" + merchantName + '\'' +
                ", category='" + category + '\'' +
                ", address='" + address + '\'' +
                ", maxPoints='" + maxPoints + '\'' +
                '}';
    }
}

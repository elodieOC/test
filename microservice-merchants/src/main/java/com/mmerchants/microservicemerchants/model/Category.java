package com.mmerchants.microservicemerchants.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Base64;
import java.util.List;

@Entity
@Table(name = "category")
@JsonSerialize(using= CategorySerializer.class)
public class Category {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType. IDENTITY )
    private Integer id;

    @NotEmpty
    @Column(name="category_name", unique = true)
    private String categoryName;

    @Lob
    @Column(name = "icon")
    private byte[] icon;

    @OneToMany(mappedBy="category")
    private List<Merchant> merchants;

    public Category(CategoryDTO categoryDTO) {
        this.id = categoryDTO.getId();
        this.categoryName = categoryDTO.getCategoryName();
        this.icon = Base64.getDecoder().decode(categoryDTO.getIcon());
    }

    public Category() {
    }

    public byte[] getIcon() {
        return icon;
    }

    public void setIcon(byte[] icon) {
        this.icon = icon;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<Merchant> getMerchants() {
        return merchants;
    }

    public void setMerchants(List<Merchant> merchants) {
        this.merchants = merchants;
    }
}

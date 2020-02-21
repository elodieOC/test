package com.mmerchants.microservicemerchants.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "icon_id", referencedColumnName = "id")
    private CategoryIcon categoryIcon;

    @OneToMany(mappedBy="category")
    @JsonIgnore
    private List<Merchant> merchants;

    public Category() {
    }

    public CategoryIcon getCategoryIcon() {
        return categoryIcon;
    }

    public void setCategoryIcon(CategoryIcon categoryIcon) {
        this.categoryIcon = categoryIcon;
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

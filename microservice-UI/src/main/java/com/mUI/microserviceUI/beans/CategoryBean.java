package com.mUI.microserviceUI.beans;

import java.util.List;

public class CategoryBean {
    private Integer id;
    private String categoryName;
    private Integer categoryIcon;
    private CategoryIconBean icon;
    private List<MerchantBean> merchants;

    public Integer getCategoryIcon() {
        return categoryIcon;
    }

    public void setCategoryIcon(Integer categoryIcon) {
        this.categoryIcon = categoryIcon;
    }

    public CategoryIconBean getIcon() {
        return icon;
    }

    public void setIcon(CategoryIconBean icon) {
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

    public List<MerchantBean> getMerchants() {
        return merchants;
    }

    public void setMerchants(List<MerchantBean> merchants) {
        this.merchants = merchants;
    }
}

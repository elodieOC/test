package com.mUI.microserviceUI.beans;

import org.springframework.web.multipart.MultipartFile;


public class EditCategoryDTO {
    private Integer id;
    private String categoryName;
    private String icon;

    public Integer getId() {
        return id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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

}

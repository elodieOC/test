package com.mUI.microserviceUI.beans;

import java.util.ArrayList;

public class Place {

    private String place_id;
    private String formatted_address;
    private String name;
    private String opening_hours;
    private String icon;

    public Place() {
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpening_hours() {
        return opening_hours;
    }

    public void setOpening_hours(String opening_hours) {
        this.opening_hours = opening_hours;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "Place{" +
                "place_id='" + place_id + '\'' +
                ", formatted_address='" + formatted_address + '\'' +
                ", name='" + name + '\'' +
                ", opening_hours='" + opening_hours + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}

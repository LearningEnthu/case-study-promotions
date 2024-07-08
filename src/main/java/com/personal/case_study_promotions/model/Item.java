package com.personal.case_study_promotions.model;

import lombok.Data;

@Data
public class Item {
    private String id;
    private double price;
    private String expiryDate;

    public Item(String id, double price, String expiryDate) {
        this.id = id;
        this.price = price;
        this.expiryDate = expiryDate;
    }
}
package com.foodio.catering.Domain;

import java.io.Serializable;

public class Foods implements Serializable {
    private int CategoryId;
    private String Description;
    private boolean Recommended;
    private int Id;
    private double Price;
    private String ImagePath;
    private int PriceId;
    private double Star;
    private int TimeId;
    private String TimeValue;
    private String Title;
    private int numberInCart;

    // CartItem properties

    private boolean selected;   // Status checkbox untuk item di keranjang

    public Foods() {
        this.Title = Title;
        this.Price = Price;
        this.numberInCart = numberInCart;
        this.selected = false; // Default unchecked
    }

    @Override
    public String toString() {
        return Title;
    }

    // Getter and Setter methods

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int categoryId) {
        CategoryId = categoryId;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public boolean isRecommended() {
        return Recommended;
    }

    public void setRecommended(boolean recommended) {
        Recommended = recommended;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public int getPriceId() {
        return PriceId;
    }

    public void setPriceId(int priceId) {
        PriceId = priceId;
    }

    public double getStar() {
        return Star;
    }

    public void setStar(double star) {
        Star = star;
    }

    public int getTimeId() {
        return TimeId;
    }

    public void setTimeId(int timeId) {
        TimeId = timeId;
    }

    public String getTimeValue() {
        return TimeValue;
    }

    public void setTimeValue(String timeValue) {
        TimeValue = timeValue;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getNumberInCart() {
        return numberInCart;
    }

    public void setNumberInCart(int numberInCart) {
        this.numberInCart = numberInCart;
    }

    // CartItem methods


    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean checked) {
        selected = checked;
    }

}

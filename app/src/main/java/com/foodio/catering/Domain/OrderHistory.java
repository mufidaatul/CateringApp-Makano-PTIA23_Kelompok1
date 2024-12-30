package com.foodio.catering.Domain;

public class OrderHistory {
    private String foodName;
    private double price;
    private int quantity;
    private String status;
    private String date;
    private String imagePath;  // Tambahkan imagePath untuk menyimpan URL gambar

    public OrderHistory() {
        // Required empty constructor for Firebase
    }

    public OrderHistory(String foodName, double price, int quantity, String status, String date, String imagePath) {
        this.foodName = foodName;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
        this.date = date;
        this.imagePath = imagePath;  // Inisialisasi imagePath
    }

    // Getter dan Setter untuk imagePath
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    // Getter dan Setter untuk properti lainnya...
    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

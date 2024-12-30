package com.foodio.catering.Domain;

import java.util.ArrayList;

public class Order {
    private String orderId;
    private String address;
    private String notes;
    private String paymentMethod;
    private ArrayList<Foods> cartList;

    // Constructor
    public Order(String orderId, String address, String notes, String paymentMethod, ArrayList<Foods> cartList) {
        this.orderId = orderId;
        this.address = address;
        this.notes = notes;
        this.paymentMethod = paymentMethod;
        this.cartList = cartList;
    }

    // Getters and Setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public ArrayList<Foods> getCartList() {
        return cartList;
    }

    public void setCartList(ArrayList<Foods> cartList) {
        this.cartList = cartList;
    }
}

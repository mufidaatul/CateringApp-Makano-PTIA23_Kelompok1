package com.foodio.catering.Domain;

import android.util.Log;

public class Price {
    private int Id;
    private double Min;
    private double Max;

    // Constructor kosong untuk Firebase
    public Price() {}

    // Getter dan Setter untuk Id
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    // Getter untuk Min
    public double getMin() {
        return Min;
    }

    // Setter untuk Min dengan validasi
    public void setMin(double min) {
        if (min <= this.Max || this.Max == 0.0) { // Validasi jika Max belum diatur
            this.Min = min;
        } else {
            Log.e("PriceError", "Min value cannot be greater than Max value");
        }
    }

    // Getter untuk Max
    public double getMax() {
        return Max;
    }

    // Setter untuk Max dengan validasi
    public void setMax(double max) {
        if (max >= this.Min || this.Min == 0.0) { // Validasi jika Min belum diatur
            this.Max = max;
        } else {
            Log.e("PriceError", "Max value cannot be less than Min value");
        }
    }

    // toString untuk menampilkan rentang harga
    @Override
    public String toString() {
        return Min + " - " + Max; // Format rentang harga
    }
}

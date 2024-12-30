package com.foodio.catering.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.foodio.catering.Domain.Foods;
import com.foodio.catering.R;
import com.foodio.catering.databinding.ActivityDetailBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DetailActivity extends AppCompatActivity {
    private ActivityDetailBinding binding;
    private Foods object;
    private int num = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(getResources().getColor(R.color.black));

        getIntentExtra();
        setVariable();
    }

    private void setVariable() {
        binding.BackButton.setOnClickListener(v -> finish());

        Glide.with(DetailActivity.this)
                .load(object.getImagePath())
                .into(binding.pic);

        binding.priceTxt.setText("Rp" + object.getPrice());
        binding.titleTxt.setText(object.getTitle());
        binding.descriptionTxt.setText(object.getDescription());
        binding.rateTxt.setText(object.getStar() + " Rating");
        binding.ratingBar.setRating((float) object.getStar());
        binding.totalTxt.setText("Rp" + (num * object.getPrice()));

        binding.plusButton.setOnClickListener(v -> {
            num = num + 1;
            binding.numTxt.setText(num + " ");
            binding.totalTxt.setText("Rp" + (num * object.getPrice()));
        });

        binding.minusButton.setOnClickListener(v -> {
            if (num > 1) {
                num = num - 1;
                binding.numTxt.setText(num + "");
                binding.totalTxt.setText("Rp" + (num * object.getPrice()));
            }
        });

        binding.AddButton.setOnClickListener(v -> {
            object.setNumberInCart(num);
            addToCartFirebase(object);

            // Pindah ke halaman keranjang
            Intent intent = new Intent(DetailActivity.this, CartActivity.class);
            startActivity(intent);
        });
    }

    private void getIntentExtra() {
        object = (Foods) getIntent().getSerializableExtra("object");
    }

    /**
     * Menambahkan produk ke Firebase
     */
    private void addToCartFirebase(Foods food) {
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("Cart");
        String itemId = cartRef.push().getKey(); // Buat ID unik untuk produk

        if (itemId != null) {
            cartRef.child(itemId).setValue(food)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(DetailActivity.this, "Added to Cart", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        // Log kesalahan untuk debugging
                        Log.e("FirebaseError", "Failed to Add: " + e.getMessage());
                        Toast.makeText(DetailActivity.this, "Failed to Add: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }
}

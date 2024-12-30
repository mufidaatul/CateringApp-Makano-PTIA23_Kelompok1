package com.foodio.catering.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.foodio.catering.Adapter.CartAdapter;
import com.foodio.catering.Domain.Foods;
import com.foodio.catering.R;
import com.foodio.catering.databinding.ActivityCartBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartItemInteractionListener {

    private ActivityCartBinding binding;
    private ArrayList<Foods> cartItems = new ArrayList<>();
    private CartAdapter cartAdapter;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(getResources().getColor(R.color.black));

        databaseReference = FirebaseDatabase.getInstance().getReference("Cart");

        setUpRecyclerView();
        loadFoodsFromFirebase();

        binding.btnCheckout.setOnClickListener(view -> {
            if (cartItems.size() > 0) {
                Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
                intent.putExtra("totalPrice", calculateTotalPrice());
                startActivity(intent);
            } else {
                Toast.makeText(CartActivity.this, "Keranjang belanjamu masih kosong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpRecyclerView() {
        cartAdapter = new CartAdapter(cartItems, this, this, this::showDeleteConfirmationDialog);
        binding.CardView.setLayoutManager(new LinearLayoutManager(this));
        binding.CardView.setAdapter(cartAdapter);
    }

    private void loadFoodsFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartItems.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Foods food = dataSnapshot.getValue(Foods.class);
                    if (food != null) {
                        // Konversi ID dari String (Firebase Key) ke int
                        try {
                            food.setId(Integer.parseInt(dataSnapshot.getKey())); // key adalah String di Firebase
                        } catch (NumberFormatException e) {
                            e.printStackTrace();  // Tangani jika ID tidak valid
                        }
                        cartItems.add(food);
                    }
                }
                cartAdapter.notifyDataSetChanged();
                updateTotalPrice();
                checkIfCartIsEmpty();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CartActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private double calculateTotalPrice() {
        double totalPrice = 0;
        for (Foods food : cartItems) {
            if (food.isSelected()) {
                totalPrice += food.getPrice() * food.getNumberInCart();
            }
        }
        return totalPrice;
    }

    private void updateTotalPrice() {
        double totalPrice = calculateTotalPrice();
        binding.tvTotalPrice.setText("Total: Rp " + totalPrice);
    }

    private void checkIfCartIsEmpty() {
        if (cartItems.isEmpty()) {
            binding.emptyTxt.setVisibility(View.VISIBLE);
        } else {
            binding.emptyTxt.setVisibility(View.GONE);
        }
    }

    private void deleteItemFromDatabase(Foods food) {
        // Menghapus item dari Firebase
        String idAsString = String.valueOf(food.getId()); // Mengonversi ID int ke String
        databaseReference.child(idAsString).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                cartItems.remove(food); // Menghapus item dari ArrayList
                cartAdapter.notifyDataSetChanged(); // Memberi tahu adapter untuk menyegarkan data
                updateTotalPrice();
                checkIfCartIsEmpty();
                Toast.makeText(CartActivity.this, "Item berhasil dihapus", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CartActivity.this, "Gagal menghapus item", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDeleteConfirmationDialog(Foods food) {
        new AlertDialog.Builder(this)
                .setTitle("Hapus Item")
                .setMessage("Apakah Anda yakin ingin menghapus item ini dari keranjang?")
                .setPositiveButton("Ya", (dialog, which) -> deleteItemFromDatabase(food))
                .setNegativeButton("Tidak", null)
                .show();
    }

    @Override
    public void onCartUpdated() {
        updateTotalPrice();
        checkIfCartIsEmpty();
    }
}

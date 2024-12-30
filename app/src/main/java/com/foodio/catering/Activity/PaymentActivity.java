package com.foodio.catering.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.foodio.catering.Adapter.PaymentAdapter;
import com.foodio.catering.Domain.Foods;
import com.foodio.catering.Domain.Order;
import com.foodio.catering.Domain.OrderHistory;
import com.foodio.catering.R;
import com.foodio.catering.databinding.ActivityPaymentBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PaymentActivity extends AppCompatActivity {
    private ActivityPaymentBinding binding;
    private PaymentAdapter adapter;
    private ArrayList<Foods> cartList = new ArrayList<>();
    private DatabaseReference cartRef;

    private double biayaAdmin;
    private final double biayaPengiriman = 12000; // Biaya pengiriman tetap

    // Views
    private EditText etAddress, etNotes;
    private RadioGroup radioGroupPaymentMethod;
    private RadioButton rbCod, rbBankTransfer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Binding layout
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase
        cartRef = FirebaseDatabase.getInstance().getReference("Cart");

        // Initialize views from layout
        etAddress = findViewById(R.id.etAddress);
        etNotes = findViewById(R.id.etNotes);
        radioGroupPaymentMethod = findViewById(R.id.radioGroupPayment);
        rbCod = findViewById(R.id.rbCod);
        rbBankTransfer = findViewById(R.id.rbBankTransfer);

        setupRecyclerView();
        loadCartData();
        setListeners();
    }

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.CardView.setLayoutManager(linearLayoutManager);

        adapter = new PaymentAdapter(cartList, this, this::calculateCart);
        binding.CardView.setAdapter(adapter);
    }

    private void loadCartData() {
        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    Foods food = itemSnapshot.getValue(Foods.class);
                    if (food != null) {
                        cartList.add(food);
                    }
                }
                adapter.notifyDataSetChanged();
                calculateCart();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PaymentActivity.this, "Failed to load cart data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void calculateCart() {
        double totalFee = 0;

        for (Foods food : cartList) {
            totalFee += food.getPrice() * food.getNumberInCart();
        }

        biayaAdmin = totalFee * 0.02; // Biaya admin 2%
        double total = totalFee + biayaAdmin + biayaPengiriman;

        binding.totalFeeTxt.setText("Rp " + Math.round(totalFee));
        binding.BiayaAdminTxt.setText("Rp " + Math.round(biayaAdmin));
        binding.BiayaPengirimanTxt.setText("Rp " + Math.round(biayaPengiriman));
        binding.totalTxt.setText("Rp " + Math.round(total));
    }

    private void setListeners() {
        binding.BackButton.setOnClickListener(v -> finish());

        binding.buttonPesansekarang.setOnClickListener(v -> {
            String address = etAddress.getText().toString();
            String notes = etNotes.getText().toString();
            int selectedPaymentMethodId = radioGroupPaymentMethod.getCheckedRadioButtonId();

            if (address.isEmpty()) {
                Toast.makeText(this, "Alamat harus diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            String paymentMethod = "";
            if (selectedPaymentMethodId == rbCod.getId()) {
                paymentMethod = "COD";
            } else if (selectedPaymentMethodId == rbBankTransfer.getId()) {
                paymentMethod = "Transfer Bank";
            } else {
                Toast.makeText(this, "Pilih metode pembayaran", Toast.LENGTH_SHORT).show();
                return;
            }

            // Simpan data ke Firebase
            saveOrder(address, notes, paymentMethod);
        });
    }

    private void saveOrder(String address, String notes, String paymentMethod) {
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Orders");
        DatabaseReference orderHistoryRef = FirebaseDatabase.getInstance().getReference("OrderHistory");

        // Buat objek pesanan
        String orderId = orderRef.push().getKey();
        if (orderId != null) {
            // Ambil informasi makanan dari cart
            ArrayList<OrderHistory> orderHistoryList = new ArrayList<>();
            for (Foods food : cartList) {
                String foodName = food.getTitle();
                double price = food.getPrice();
                int quantity = food.getNumberInCart();
                String status = "Makanan Sudah diantar";  // Status bisa "Pending", "Completed", dll.

                // Menggunakan SimpleDateFormat untuk format tanggal
                long timestamp = System.currentTimeMillis();  // Mendapatkan waktu dalam milidetik
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());  // Format: 12 Desember 2024
                String date = sdf.format(new Date(timestamp));  // Mengonversi timestamp ke format tanggal

                // Menambahkan imagePath
                String imagePath = food.getImagePath();  // Menambahkan imagePath dari cart

                // Buat OrderHistory untuk menyimpan ke OrderHistory node
                OrderHistory orderHistory = new OrderHistory(foodName, price, quantity, status, date, imagePath);
                orderHistoryList.add(orderHistory);
            }

            // Simpan data ke node OrderHistory
            for (OrderHistory orderHistory : orderHistoryList) {
                orderHistoryRef.child(orderId).setValue(orderHistory);
            }

            // Simpan data pesanan ke node Orders
            Order order = new Order(orderId, address, notes, paymentMethod, cartList);
            orderRef.child(orderId).setValue(order)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Pesanan berhasil dibuat", Toast.LENGTH_SHORT).show();
                        // Pindah ke HistoryOrderActivity setelah berhasil
                        Intent intent = new Intent(PaymentActivity.this, HistoryOrderActivity.class);
                        startActivity(intent);
                        finish();  // Menutup PaymentActivity
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Gagal membuat pesanan", Toast.LENGTH_SHORT).show());
        }
    }
}

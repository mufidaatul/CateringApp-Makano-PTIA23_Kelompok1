package com.foodio.catering.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foodio.catering.Adapter.CategoryAdapter;
import com.foodio.catering.Adapter.RecommendedAdapter;
import com.foodio.catering.Domain.Category;
import com.foodio.catering.Domain.Location;
import com.foodio.catering.Domain.Foods;
import com.foodio.catering.Domain.Price;
import com.foodio.catering.Domain.Time;
import com.foodio.catering.R;
import com.foodio.catering.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        displayUsername();

        initLocation();
        initTime();
        initPrice();
        initRecommended();
        initCategory();
        setVariable();

        setupPriceFilter(); // logika filter harga
    }

    private void displayUsername() {
        // Ambil user ID dari pengguna yang sedang login
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = database.getReference("Users").child(userId);

        // Ambil data username dari Firebase
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Ambil nilai username dari snapshot
                    String username = snapshot.child("username").getValue(String.class);
                    if (username != null) {
                        // Set nilai username ke TextView textview9
                        binding.textView9.setText(username);
                    }
                } else {
                    Log.e("FirebaseError", "User data not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error: " + error.getMessage());
            }
        });
    }


    private void setVariable() {
        binding.logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        });

        binding.searchButton.setOnClickListener(v -> {
            String text = binding.searchEditText.getText().toString();
            if (!text.isEmpty()) {
                Intent intent = new Intent(MainActivity.this, ListFoodsActivity.class);
                intent.putExtra("text", text);
                intent.putExtra("isSearch", true);
                startActivity(intent);
            }
        });

        binding.cartButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CartActivity.class)));
        binding.HistoryButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, HistoryOrderActivity.class)));
    }

    private void initLocation() {
        DatabaseReference myRef = database.getReference("Location");
        ArrayList<Location> list = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        list.add(issue.getValue(Location.class));
                    }
                    ArrayAdapter<Location> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.sp_item,list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.locationSp.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error: " + error.getMessage());
            }
        });
    }

    private void initRecommended() {
        DatabaseReference myRef = database.getReference("Foods");
        binding.progressBarRecommended.setVisibility(View.VISIBLE);
        ArrayList<Foods> list = new ArrayList<>();
        Query query = myRef.orderByChild("Recommended").equalTo(true);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        list.add(issue.getValue(Foods.class));
                    }
                    if (!list.isEmpty()) {
                        binding.RecommendedView.setLayoutManager(
                                new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        RecyclerView.Adapter<RecommendedAdapter.viewHolder> adapter = new RecommendedAdapter(list);
                        binding.RecommendedView.setAdapter(adapter);
                    }
                }
                binding.progressBarRecommended.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error: " + error.getMessage());
            }
        });
    }

    private void initTime() {
        DatabaseReference myRef = database.getReference("Time");
        ArrayList<Time> list = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        list.add(issue.getValue(Time.class));
                    }
                    ArrayAdapter<Time> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.sp_item, list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.timeSp.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error: " + error.getMessage());
            }
        });
    }

    private void initPrice() {
        DatabaseReference myRef = database.getReference("Price");
        ArrayList<Price> list = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        Price price = issue.getValue(Price.class);
                        if (price != null) {
                            // Log nilai yang diterima untuk memastikan Min dan Max adalah double
                            Log.d("PriceData", "Min: " + price.getMin() + ", Max: " + price.getMax());
                            list.add(price);
                        } else {
                            Log.e("PriceData", "Received null Price object from Firebase");
                        }
                    }

                    // Periksa apakah list sudah terisi dengan benar
                    if (!list.isEmpty()) {
                        ArrayAdapter<Price> adapter = new ArrayAdapter<>(
                                MainActivity.this,
                                android.R.layout.simple_spinner_item,
                                list
                        );
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        binding.priceSp.setAdapter(adapter);
                    } else {
                        Log.e("PriceData", "Price list is empty");
                    }
                } else {
                    Log.e("Firebase", "No data in 'Price' node");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error: " + error.getMessage());
            }
        });
    }



    private void initCategory() {
        DatabaseReference myRef = database.getReference("Category");
        binding.progressBarCategory.setVisibility(View.VISIBLE);
        ArrayList<Category> list = new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        list.add(issue.getValue(Category.class));
                    }
                    if (!list.isEmpty()) {
                        binding.CategoryView.setLayoutManager(new GridLayoutManager(MainActivity.this, 4));
                        RecyclerView.Adapter<CategoryAdapter.ViewHolder> adapter = new CategoryAdapter(list);
                        binding.CategoryView.setAdapter(adapter);
                    }
                }
                binding.progressBarCategory.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error: " + error.getMessage());
            }
        });
    }

    private void filterFoodsByPrice(double minPrice, double maxPrice) {
        DatabaseReference foodsRef = database.getReference("Foods");
        foodsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Foods> filteredFoods = new ArrayList<>();
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        Foods food = issue.getValue(Foods.class);
                        if (food != null && food.getPrice() >= minPrice && food.getPrice() <= maxPrice) {
                            filteredFoods.add(food);
                        }
                    }
                    // Tampilkan makanan yang sesuai
                    binding.RecommendedView.setLayoutManager(
                            new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                    RecommendedAdapter adapter = new RecommendedAdapter(filteredFoods);
                    binding.RecommendedView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error filtering foods: " + error.getMessage());
            }
        });
    }


    private void setupPriceFilter() {
        binding.priceSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Mendapatkan objek Price yang dipilih
                Price selectedPrice = (Price) parent.getItemAtPosition(position);
                Log.d("PriceSelection", "Selected: Min=" + selectedPrice.getMin() + ", Max=" + selectedPrice.getMax());

                // Pastikan harga valid
                if (selectedPrice != null) {
                    double minPrice = selectedPrice.getMin();
                    double maxPrice = selectedPrice.getMax();

                    // Panggil metode untuk memfilter makanan berdasarkan harga
                    filterFoodsByPrice(minPrice, maxPrice);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Tidak ada aksi jika tidak ada item yang dipilih
            }
        });
    }
}

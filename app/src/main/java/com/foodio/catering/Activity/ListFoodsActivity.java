package com.foodio.catering.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foodio.catering.Adapter.FoodListAdapter;
import com.foodio.catering.Domain.Foods;
import com.foodio.catering.databinding.ActivityListFoodsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListFoodsActivity extends BaseActivity {

    private ActivityListFoodsBinding binding;
    private RecyclerView.Adapter adapterListFood;
    private String categoryId; // Pastikan sesuai dengan "Id" di database
    private String categoryName;
    private String searchText;
    private boolean isSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListFoodsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentExtra();
        initList();
    }

    private void initList() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Foods");
        binding.progressBar.setVisibility(View.VISIBLE);
        ArrayList<Foods> list = new ArrayList<>();

        Query query = null;

        // Periksa apakah pencarian atau kategori
        if (isSearch) {
            if (searchText != null && !searchText.trim().isEmpty()) {
                query = myRef.orderByChild("Title").startAt(searchText).endAt(searchText + '\uf8ff');
            } else {
                Log.e("ListFoodsActivity", "Search text is empty or null.");
                binding.progressBar.setVisibility(View.GONE);
                return; // Keluar jika searchText tidak valid
            }
        } else {
            // Pastikan categoryId tidak null
            if (categoryId != null && !categoryId.trim().isEmpty()) {
                try {
                    // Mengubah categoryId menjadi integer jika diperlukan
                    int categoryIdInt = Integer.parseInt(categoryId);
                    query = myRef.orderByChild("CategoryId").equalTo(categoryIdInt);
                } catch (NumberFormatException e) {
                    Log.w("ListFoodsActivity", "CategoryId is not a valid integer, using string fallback.");
                    query = myRef.orderByChild("CategoryId").equalTo(categoryId); // Jika kategori adalah string, gunakan query yang sesuai
                }
            } else {
                Log.e("ListFoodsActivity", "categoryId is null or empty, cannot perform query.");
                binding.progressBar.setVisibility(View.GONE);
                return; // Keluar jika categoryId tidak valid
            }
        }

        // Menambahkan listener untuk query Firebase
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                binding.progressBar.setVisibility(View.GONE);

                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        Foods food = issue.getValue(Foods.class);
                        if (food != null) {
                            list.add(food);
                            Log.d("ListFoodsActivity", "Food added: " + food.getTitle());
                        } else {
                            Log.w("ListFoodsActivity", "Food data is null for a snapshot.");
                        }
                    }

                    if (!list.isEmpty()) {
                        binding.foodlistView.setLayoutManager(new GridLayoutManager(ListFoodsActivity.this, 2));
                        adapterListFood = new FoodListAdapter(list);
                        binding.foodlistView.setAdapter(adapterListFood);
                    } else {
                        Log.d("ListFoodsActivity", "No data found in this category.");
                    }
                } else {
                    Log.d("ListFoodsActivity", "No data found in Firebase.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBar.setVisibility(View.GONE);
                Log.e("ListFoodsActivity", "Database error: " + error.getMessage());
            }
        });
    }

    private void getIntentExtra() {
        // Ambil data dari Intent
        categoryId = getIntent().getStringExtra("CategoryId"); // Ambil "Id" sebagai string
        categoryName = getIntent().getStringExtra("CategoryName"); // Ambil sesuai dengan input Name
        searchText = getIntent().getStringExtra("text");
        isSearch = getIntent().getBooleanExtra("isSearch", false);

        // Menampilkan nama kategori di tampilan
        binding.titleTxt.setText(categoryName);
        binding.BackButton.setOnClickListener(v -> finish());

        // Debugging log untuk memastikan data dari Intent
        Log.d("ListFoodsActivity", "categoryId: " + categoryId);
        Log.d("ListFoodsActivity", "categoryName: " + categoryName);
        Log.d("ListFoodsActivity", "isSearch: " + isSearch);
        Log.d("ListFoodsActivity", "searchText: " + searchText);

        // Jika categoryId kosong atau null, berikan log dan keluar
        if (categoryId == null || categoryId.trim().isEmpty()) {
            Log.e("ListFoodsActivity", "categoryId is null or empty, please check Intent.");
        }
    }
}
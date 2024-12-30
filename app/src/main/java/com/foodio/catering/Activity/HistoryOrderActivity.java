package com.foodio.catering.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.foodio.catering.Adapter.HistoryOrderAdapter;
import com.foodio.catering.Domain.OrderHistory;
import com.foodio.catering.databinding.ActivityHistoryOrderBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

public class HistoryOrderActivity extends AppCompatActivity {
    private ActivityHistoryOrderBinding binding;
    private ArrayList<OrderHistory> orderHistoryList;
    private HistoryOrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initRecyclerView();
        fetchOrderHistory();

        binding.BackButton.setOnClickListener(v -> finish());
    }

    private void initRecyclerView() {
        orderHistoryList = new ArrayList<>();
        binding.rvHistory.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HistoryOrderAdapter(orderHistoryList);
        binding.rvHistory.setAdapter(adapter);
    }

    private void fetchOrderHistory() {
        binding.tvNotFound.setVisibility(View.GONE);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("OrderHistory");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderHistoryList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        OrderHistory order = dataSnapshot.getValue(OrderHistory.class);
                        orderHistoryList.add(order);
                    }
                    adapter.notifyDataSetChanged();
                }

                if (orderHistoryList.isEmpty()) {
                    binding.tvNotFound.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }
}

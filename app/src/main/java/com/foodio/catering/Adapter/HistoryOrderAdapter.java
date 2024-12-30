package com.foodio.catering.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.foodio.catering.Domain.OrderHistory;
import com.foodio.catering.R;
import com.foodio.catering.databinding.ListItemRiwayatBinding;

import java.util.ArrayList;

public class HistoryOrderAdapter extends RecyclerView.Adapter<HistoryOrderAdapter.ViewHolder> {
    private final ArrayList<OrderHistory> orderHistoryList;

    public HistoryOrderAdapter(ArrayList<OrderHistory> orderHistoryList) {
        this.orderHistoryList = orderHistoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemRiwayatBinding binding = ListItemRiwayatBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderHistory order = orderHistoryList.get(position);

        holder.binding.tvNama.setText(order.getFoodName());
        holder.binding.tvPrice.setText("Rp" + order.getPrice());
        holder.binding.tvJml.setText("Jumlah: " + order.getQuantity());
        holder.binding.tvStatus.setText(order.getStatus());
        holder.binding.tvDate.setText(order.getDate());

        // Menampilkan gambar makanan menggunakan Glide
        String imageUrl = order.getImagePath(); // Mengambil URL gambar dari OrderHistory
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.ic_history) // Gambar placeholder
                .into(holder.binding.imgLogo); // Pastikan ListItemRiwayatBinding punya ImageView dengan id ivFoodImage

        holder.binding.btnLogin.setOnClickListener(v ->
                Toast.makeText(holder.itemView.getContext(), "Pesan lagi: " + order.getFoodName(), Toast.LENGTH_SHORT).show()
        );
    }

    @Override
    public int getItemCount() {
        return orderHistoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ListItemRiwayatBinding binding;

        public ViewHolder(@NonNull ListItemRiwayatBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

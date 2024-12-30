package com.foodio.catering.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.foodio.catering.Domain.Category;
import com.foodio.catering.R;
import com.foodio.catering.Activity.ListFoodsActivity;

import java.util.ArrayList;
import java.util.Locale;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private ArrayList<Category> items;
    private Context context;

    public CategoryAdapter(ArrayList<Category> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.viewholder_category, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        // Set the category name
        holder.titleTxt.setText(items.get(position).getName());

        // Get the image path dynamically
        String imagePath = items.get(position).getImagePath().toLowerCase(Locale.ROOT);
        int drawableResourceId = context.getResources().getIdentifier(imagePath, "drawable", context.getPackageName());

        // Use Glide for image loading
        Glide.with(context)
                .load(drawableResourceId)
                .placeholder(R.drawable.btn_2) // Placeholder image
                .into(holder.pic);

        // Set item click listener to navigate to ListFoodsActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ListFoodsActivity.class);
            intent.putExtra("CategoryId", items.get(position).getId());
            intent.putExtra("CategoryName", items.get(position).getName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTxt;
        ImageView pic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.tvName);
            pic = itemView.findViewById(R.id.imageIcon);
        }
    }
}
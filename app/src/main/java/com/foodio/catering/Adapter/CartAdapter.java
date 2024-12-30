package com.foodio.catering.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.foodio.catering.Domain.Foods;
import com.foodio.catering.R;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private ArrayList<Foods> cartItems;
    private Context context;
    private OnCartItemInteractionListener listener;
    private OnItemLongClickListener longClickListener;

    public CartAdapter(ArrayList<Foods> cartItems, Context context, OnCartItemInteractionListener listener, OnItemLongClickListener longClickListener) {
        this.cartItems = cartItems;
        this.context = context;
        this.listener = listener;
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Foods food = cartItems.get(position);

        Glide.with(context)
                .load(food.getImagePath())
                .into(holder.ivItemImage);

        holder.tvItemName.setText(food.getTitle());
        holder.tvItemPrice.setText("Rp " + (food.getPrice() * food.getNumberInCart()));
        holder.tvItemQuantity.setText(String.valueOf(food.getNumberInCart()));

        holder.checkboxItem.setChecked(food.isSelected());
        holder.checkboxItem.setOnCheckedChangeListener((buttonView, isChecked) -> {
            food.setSelected(isChecked);
            listener.onCartUpdated();
        });

        holder.itemView.setOnLongClickListener(v -> {
            longClickListener.onItemLongClicked(food);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemName, tvItemPrice, tvItemQuantity;
        CheckBox checkboxItem;
        ImageView ivItemImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivItemImage = itemView.findViewById(R.id.iv_item_image);
            tvItemName = itemView.findViewById(R.id.tv_item_name);
            tvItemPrice = itemView.findViewById(R.id.tv_item_price);
            tvItemQuantity = itemView.findViewById(R.id.tv_item_quantity);
            checkboxItem = itemView.findViewById(R.id.checkbox_item);
        }
    }

    public interface OnCartItemInteractionListener {
        void onCartUpdated();
    }

    public interface OnItemLongClickListener {
        void onItemLongClicked(Foods food);
    }
}

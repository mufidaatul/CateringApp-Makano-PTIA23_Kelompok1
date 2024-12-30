package com.foodio.catering.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.foodio.catering.Domain.Foods;
import com.foodio.catering.R;
import com.foodio.catering.Helper.ChangeNumberItemsListener;

import java.util.ArrayList;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {
    private ArrayList<Foods> list;
    private Context context;
    private ChangeNumberItemsListener changeNumberItemsListener;

    public PaymentAdapter(ArrayList<Foods> list, Context context, ChangeNumberItemsListener changeNumberItemsListener) {
        this.list = list;
        this.context = context;
        this.changeNumberItemsListener = changeNumberItemsListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_payment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Foods food = list.get(position);

        holder.title.setText(food.getTitle());
        holder.feeEachItem.setText("Rp" + food.getPrice());
        holder.totalEachItem.setText(food.getNumberInCart() + " x Rp" + food.getPrice());
        holder.num.setText(String.valueOf(food.getNumberInCart()));

        Glide.with(context)
                .load(food.getImagePath())
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.pic);

        // Disable add/remove buttons for PaymentActivity
        holder.plusItem.setVisibility(View.GONE);
        holder.minusItem.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, feeEachItem, totalEachItem, num;
        ImageView pic, plusItem, minusItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.titleTxt);
            feeEachItem = itemView.findViewById(R.id.feeEachItem);
            totalEachItem = itemView.findViewById(R.id.totalEachItem);
            num = itemView.findViewById(R.id.numberItemTxt);
            pic = itemView.findViewById(R.id.pic);
            plusItem = itemView.findViewById(R.id.plusCartButton); // ImageView
            minusItem = itemView.findViewById(R.id.minusCartButton); // ImageView
        }
    }
}

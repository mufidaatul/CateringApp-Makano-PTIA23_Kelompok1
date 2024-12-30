package com.foodio.catering.Adapter;

import android.annotation.SuppressLint;
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
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.foodio.catering.Activity.DetailActivity;
import com.foodio.catering.Domain.Foods;
import com.foodio.catering.R;

import java.util.ArrayList;

public class RecommendedAdapter extends RecyclerView.Adapter<RecommendedAdapter.viewHolder> {
    ArrayList<Foods> items;
    Context context;

    public RecommendedAdapter(ArrayList<Foods> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public RecommendedAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_recommended, parent, false);
        return new viewHolder(inflate);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecommendedAdapter.viewHolder holder, int position) {
        holder.titleTxt.setText(items.get(position).getTitle());
        holder.priceTxt.setText("Rp" + items.get(position).getPrice());
        holder.timeTxt.setText(items.get(position).getTimeValue());
        holder.starTxt.setText("" + items.get(position).getStar());

        Glide.with(context)
                .load(items.get(position).getImagePath())
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.pic);

        holder.itemView.setOnClickListener(v -> {
            Intent intent=new Intent(context, DetailActivity.class);
            intent.putExtra("object",items.get(position));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        TextView titleTxt, priceTxt, starTxt, timeTxt;
        ImageView pic;

        @SuppressLint("CutPasteId")
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.titleTxt);
            priceTxt = itemView.findViewById(R.id.priceTxt);
            starTxt = itemView.findViewById(R.id.starTxt);
            timeTxt = itemView.findViewById(R.id.timeTxt);
            pic = itemView.findViewById(R.id.pic);
        }
    }
}
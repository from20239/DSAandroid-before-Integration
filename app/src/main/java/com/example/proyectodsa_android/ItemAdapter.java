package com.example.proyectodsa_android;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.proyectodsa_android.models.InventoryObject;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.InventoryViewHolder> {
    private List<InventoryObject> items = new ArrayList<>();

    public void setItems(List<InventoryObject> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public InventoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_inventory, parent, false);
        return new InventoryViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryViewHolder holder, int position) {
        InventoryObject item = items.get(position);
        holder.bind(item);
    }

    static class InventoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvQuantity;
        ImageView ivImage;

        InventoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            ivImage = itemView.findViewById(R.id.ivImage);
        }

        void bind(InventoryObject item) {
            tvName.setText(item.getName());
            tvQuantity.setText(String.valueOf(item.getQuantity()));

            // 拼接完整的 URL
            String baseUrl = "http://localhost:8080/"; // 替换为你的服务器地址
            String imageUrl = item.getUrl();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                if (!imageUrl.startsWith("http")) {
                    imageUrl = baseUrl + imageUrl;
                }
            } else {
                imageUrl = ""; // 防止 null，提供默认值
            }

            Log.d("StoreAdapter", "Final Image URL: " + imageUrl); // 添加日志以输出拼接后的 URL

            // Carga de imágenes con GlideApp
            GlideApp.with(itemView.getContext())
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivImage);
        }
    }




}
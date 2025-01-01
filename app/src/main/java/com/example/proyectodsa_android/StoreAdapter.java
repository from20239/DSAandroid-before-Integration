package com.example.proyectodsa_android;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectodsa_android.models.StoreObject;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreViewHolder> {
    private List<StoreObject> items = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public void setItems(List<StoreObject> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_store, parent, false);
        return new StoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreViewHolder holder, int position) {
        StoreObject item = items.get(position);
        holder.bind(item);

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    static class StoreViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvPrice;
        ImageView ivImage;


        StoreViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            ivImage = itemView.findViewById(R.id.ivImage);
        }

        void bind(StoreObject item) {
            tvName.setText(item.getName());
            tvPrice.setText(String.format("%.2f €", item.getPrice()));

            // 拼接完整的 URL
            String baseUrl = "http://localhost:8080/"; // 替换为你的服务器地址
            String imageUrl = item.getImageUrl();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                if (!imageUrl.startsWith("http")) {
                    imageUrl = baseUrl + imageUrl;
                }
            } else {
                imageUrl = ""; // 防止 null，提供默认值
            }

            Log.d("StoreAdapter", "Final Image URL: " + imageUrl); // 添加日志以输出拼接后的 URL

            // 使用 GlideApp 加载图片
            GlideApp.with(itemView.getContext())
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivImage);
        }
    }



    public interface OnItemClickListener {
        void onItemClick(StoreObject item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }


}
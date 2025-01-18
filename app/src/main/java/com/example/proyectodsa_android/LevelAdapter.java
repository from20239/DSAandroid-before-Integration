package com.example.proyectodsa_android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyectodsa_android.R;
import com.example.proyectodsa_android.models.Level;
import java.util.List;

public class LevelAdapter extends RecyclerView.Adapter<LevelAdapter.LevelViewHolder> {

    private final Context context;
    private final List<Level> levelList;

    public LevelAdapter(Context context, List<Level> levelList) {
        this.context = context;
        this.levelList = levelList;
    }

    @NonNull
    @Override
    public LevelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_level, parent, false);
        return new LevelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LevelViewHolder holder, int position) {
        Level level = levelList.get(position);
        holder.levelName.setText(level.getLevelName());
        holder.userId.setText("User ID: " + level.getUserId());
        holder.levelId.setText("Level ID: " + level.getId());
    }

    @Override
    public int getItemCount() {
        return levelList.size();
    }

    static class LevelViewHolder extends RecyclerView.ViewHolder {
        TextView levelName, userId, levelId;

        public LevelViewHolder(@NonNull View itemView) {
            super(itemView);
            levelName = itemView.findViewById(R.id.levelName);
            userId = itemView.findViewById(R.id.userId);
            levelId = itemView.findViewById(R.id.levelId);
        }
    }
}


package com.example.aioldphotorestorer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aioldphotorestorer.R;

import java.util.List;

public class FeatureAdapter extends RecyclerView.Adapter<FeatureAdapter.FeatureViewHolder> {

    private final List<String> items;

    public FeatureAdapter(List<String> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public FeatureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feature, parent, false);
        return new FeatureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeatureViewHolder holder, int position) {
        holder.label.setText(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class FeatureViewHolder extends RecyclerView.ViewHolder {
        private final TextView label;

        FeatureViewHolder(@NonNull View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.textFeature);
        }
    }
}

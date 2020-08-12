package com.mykvantorium.ui.progress;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mykvantorium.R;

public class ProgressViewHolder extends RecyclerView.ViewHolder {

    View progressView;

    public ProgressViewHolder(@NonNull View itemView) {
        super(itemView);
        progressView = itemView;
    }

    public void setDetails(Context context, String date, String score) {
        TextView progressDate = progressView.findViewById(R.id.progressDate);
        TextView progressScore = progressView.findViewById(R.id.progressScore);
        progressDate.setText(date);
        progressScore.setText(score);
    }
}

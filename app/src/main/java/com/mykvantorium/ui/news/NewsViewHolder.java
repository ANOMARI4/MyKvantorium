package com.mykvantorium.ui.news;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mykvantorium.R;
import com.squareup.picasso.Picasso;

public class NewsViewHolder extends RecyclerView.ViewHolder {

    View newsView;

    public NewsViewHolder(@NonNull View itemView) {
        super(itemView);
        newsView = itemView;
    }

    public void setDetails(Context context, String title, String description, String image, String link) {
        TextView newsTitle = newsView.findViewById(R.id.newsTitle);
        TextView newsDescription = newsView.findViewById(R.id.newsDescription);
        ImageView newsImage = newsView.findViewById(R.id.newsImage);
        TextView newsLink = newsView.findViewById(R.id.newsLink);
        newsTitle.setText(title);
        newsDescription.setText(description);
        Picasso.get().load(image).into(newsImage);
        newsLink.setText(link);
    }
}

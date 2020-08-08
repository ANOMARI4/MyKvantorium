package com.mykvantorium.ui.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mykvantorium.R;

public class NewsFragment extends Fragment {

    private RecyclerView newsRecyclerView;
    private DatabaseReference newsRef;
    private FirebaseRecyclerAdapter<NewsModel, NewsViewHolder> adapter;
    private FirebaseRecyclerOptions<NewsModel> options;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_news, container, false);

        newsRecyclerView = root.findViewById(R.id.newsRecyclerView);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseDatabase newsFirebaseDatabase = FirebaseDatabase.getInstance();
        newsRef = newsFirebaseDatabase.getReference("News");

        showNews();

        return root;
    }

    private void showNews() {
        options = new FirebaseRecyclerOptions.Builder<NewsModel>().setQuery(newsRef, NewsModel.class).build();
        adapter = new FirebaseRecyclerAdapter<NewsModel, NewsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull NewsViewHolder viewHolder, int i, @NonNull NewsModel model) {
                viewHolder.setDetails(getContext(), model.getTitle(), model.getDescription(), model.getImage(), model.getLink());
            }

            @NonNull
            @Override
            public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_row, parent, false);
                return new NewsViewHolder(itemView);
            }
        };
        newsRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
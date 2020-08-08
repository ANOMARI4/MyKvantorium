package com.mykvantorium.ui.progress;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mykvantorium.R;

public class ProgressFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private FirebaseUser currentUser;
    private DatabaseReference progressDb;

    private TextView textViewName, textViewRating;

    private RecyclerView progressRecyclerView;
    private FirebaseRecyclerAdapter<ProgressModel, ProgressViewHolder> adapter;
    private FirebaseRecyclerOptions<ProgressModel> options;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_progress, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

        textViewName = root.findViewById(R.id.textViewName);
        textViewRating = root.findViewById(R.id.textViewRating);

        progressRecyclerView = root.findViewById(R.id.progressRecyclerView);
        progressRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        showName();

        showProgress();

        showRating();

        return root;
    }

    public void showName() {
        DatabaseReference nameRef = database.getReference("Users" + "/" + currentUser.getUid()).child("name");
        nameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                textViewName.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    public void showProgress() {
        progressDb = database.getReference("Users" + "/" + currentUser.getUid() + "/" + "progress" + "/" + "0");
        options = new FirebaseRecyclerOptions.Builder<ProgressModel>().setQuery(progressDb, ProgressModel.class).build();
        adapter = new FirebaseRecyclerAdapter<ProgressModel, ProgressViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProgressViewHolder viewHolder, int i, @NonNull ProgressModel model) {
                viewHolder.setDetails(getContext(), model.getDate(), model.getScore());
            }

            @NonNull
            @Override
            public ProgressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_row, parent, false);
                return new ProgressViewHolder(itemView);
            }
        };
        progressRecyclerView.setAdapter(adapter);
    }

    public void showRating() {
        DatabaseReference sumRef = database.getReference("Users" + "/" + currentUser.getUid() + "/" + "progress" + "/" + "rating" + "/" + "0").child("scoreSum");
        sumRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer value = dataSnapshot.getValue(Integer.class);
                textViewRating.setText(String.valueOf(value));
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        DatabaseReference totalRef = database.getReference("Users" + "/" + currentUser.getUid() + "/" + "progress" + "/" + "rating" + "/" + "0").child("total");
        totalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer value = dataSnapshot.getValue(Integer.class);
                textViewRating.setText(String.valueOf(Integer.parseInt(textViewRating.getText().toString()) / value));
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
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
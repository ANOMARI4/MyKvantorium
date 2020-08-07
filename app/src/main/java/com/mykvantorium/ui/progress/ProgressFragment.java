package com.mykvantorium.ui.progress;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
    private TextView textViewName;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_progress, container, false);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        progressDb = database.getReference("Users" + "/" + currentUser.getUid() + "/" + "name");
        textViewName = root.findViewById(R.id.textViewName);
        progressDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                textViewName.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
        return root;
    }
}
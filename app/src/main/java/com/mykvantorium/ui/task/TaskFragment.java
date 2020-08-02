package com.mykvantorium.ui.task;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mykvantorium.R;

public class TaskFragment extends Fragment implements View.OnClickListener {

    private int t = 0;
    private String time[] = {"Сегодня", "Завтра", "Предстоящие", "Без срока"};
    private FirebaseRecyclerAdapter<TaskModel, TaskViewHolder> adapter;
    private FirebaseRecyclerOptions<TaskModel> options;
    private FloatingActionButton buttonAddTask;
    private FirebaseDatabase database;
    private DatabaseReference taskDb;
    private FirebaseUser currentUser;
    private RecyclerView todayTaskView, tomorrowTaskView, comingTaskView, unlimitedTaskView;
    private FirebaseAuth mAuth;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_task, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

        buttonAddTask = root.findViewById(R.id.buttonAddTask);
        buttonAddTask.setOnClickListener(this);

        todayTaskView = root.findViewById(R.id.todayTaskView);
        tomorrowTaskView = root.findViewById(R.id.tomorrowTaskView);
        comingTaskView = root.findViewById(R.id.comingTaskView);
        unlimitedTaskView = root.findViewById(R.id.unlimitedTaskView);

        todayTaskView.setLayoutManager(new LinearLayoutManager(getContext()));
        tomorrowTaskView.setLayoutManager(new LinearLayoutManager(getContext()));
        comingTaskView.setLayoutManager(new LinearLayoutManager(getContext()));
        unlimitedTaskView.setLayoutManager(new LinearLayoutManager(getContext()));

        showTask();

        return root;
    }

    private void showTask() {
        for (t = 0; t < 4; t++) {
            taskDb = database.getReference(currentUser.getUid() + "/" + time[0]);
            options = new FirebaseRecyclerOptions.Builder<TaskModel>().setQuery(taskDb, TaskModel.class).build();
            adapter = new FirebaseRecyclerAdapter<TaskModel, TaskViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull TaskViewHolder taskViewHolder, int i, @NonNull TaskModel taskModel) {
                    taskViewHolder.taskName.setText(taskModel.getTaskName());
                    taskViewHolder.taskCheckBox.setChecked(taskModel.isCheckbox());
                }

                @NonNull
                @Override
                public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_row, parent, false);
                    return new TaskViewHolder(itemView);
                }
            };
            todayTaskView.setAdapter(adapter);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonAddTask:
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View mView = getLayoutInflater().inflate(R.layout.dialog_task, null);
                final EditText editTextTaskName = mView.findViewById(R.id.editTextTaskName);
                final Spinner spinnerTime = mView.findViewById(R.id.spinnerTime);
                Button save = mView.findViewById(R.id.buttonSave);
                Button cancel = mView.findViewById(R.id.buttonCancel);
                builder.setView(mView);
                final AlertDialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.buttonCancel:
                                dialog.dismiss();
                                break;
                        }
                    }
                });
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.buttonSave:
                                String taskName = editTextTaskName.getText().toString();
                                String taskTime = spinnerTime.getSelectedItem().toString();
                                TaskModel taskModel = new TaskModel(taskName, taskTime, false);
                                taskDb = database.getReference(currentUser.getUid() + "/" + taskTime);
                                taskDb.push().setValue(taskModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getContext(), "Задача добавлена", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                                dialog.dismiss();
                                break;
                        }
                    }
                });
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getTitle().equals("Редактировать")) {
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(), adapter.getItem(item.getOrder()));
        } else if (item.getTitle().equals("Удалить")) {
            deleteTask(adapter.getRef(item.getOrder()).getKey());
            Toast.makeText(getContext(), "Задача удалена", Toast.LENGTH_SHORT).show();
        }
        return super.onContextItemSelected(item);
    }

    private void deleteTask(String key) {
        taskDb = database.getReference(currentUser.getUid());
        taskDb.child(key).removeValue();
    }

    private void showUpdateDialog(String key, TaskModel item) {
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
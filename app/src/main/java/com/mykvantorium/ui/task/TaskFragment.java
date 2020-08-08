package com.mykvantorium.ui.task;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.webianks.library.scroll_choice.ScrollChoice;

import java.util.ArrayList;
import java.util.List;

public class TaskFragment extends Fragment implements View.OnClickListener {

    private FirebaseRecyclerAdapter<TaskModel, TaskViewHolder> todayAdapter;
    private FirebaseRecyclerOptions<TaskModel> todayOptions;
    private FirebaseRecyclerAdapter<TaskModel, TaskViewHolder> tomorrowAdapter;
    private FirebaseRecyclerOptions<TaskModel> tomorrowOptions;
    private FirebaseRecyclerAdapter<TaskModel, TaskViewHolder> comingAdapter;
    private FirebaseRecyclerOptions<TaskModel> comingOptions;
    private FirebaseRecyclerAdapter<TaskModel, TaskViewHolder> unlimitedAdapter;
    private FirebaseRecyclerOptions<TaskModel> unlimitedOptions;
    private FloatingActionButton buttonAddTask;
    private FirebaseDatabase database;
    private DatabaseReference taskDb, todayTaskDb, tomorrowTaskDb, comingTaskDb, unlimitedTaskDb;
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

        showTodayTask();
        showTomorrowTask();
        showComingTask();
        showUnlimitedTask();

        return root;
    }

    private void showTodayTask() {
        todayTaskDb = database.getReference("Users" + "/" + currentUser.getUid() + "/" + "Сегодня");
        todayOptions = new FirebaseRecyclerOptions.Builder<TaskModel>().setQuery(todayTaskDb, TaskModel.class).build();
        todayAdapter = new FirebaseRecyclerAdapter<TaskModel, TaskViewHolder>(todayOptions) {
            protected void onBindViewHolder(@NonNull TaskViewHolder taskViewHolder, final int i, @NonNull final TaskModel taskModel) {
                taskViewHolder.taskName.setText(taskModel.getTaskName());
                taskViewHolder.taskCheckBox.setChecked(taskModel.isCheckbox());
                if (taskModel.isCheckbox()) {
                    taskViewHolder.taskName.setPaintFlags(taskViewHolder.taskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
                taskViewHolder.taskCheckBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference itemRef = getRef(i);
                        itemRef.child("checkbox").setValue(!taskModel.isCheckbox());
                    }
                });
                taskViewHolder.taskDeleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference itemRef = getRef(i);
                        itemRef.removeValue();
                        Toast.makeText(getContext(), "Задача удалена", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @NonNull
            @Override
            public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_row, parent, false);
                return new TaskViewHolder(itemView);
            }
        };
        todayTaskView.setAdapter(todayAdapter);
    }

    private void showTomorrowTask() {
        tomorrowTaskDb = database.getReference("Users" + "/" + currentUser.getUid() + "/" + "Завтра");
        tomorrowOptions = new FirebaseRecyclerOptions.Builder<TaskModel>().setQuery(tomorrowTaskDb, TaskModel.class).build();
        tomorrowAdapter = new FirebaseRecyclerAdapter<TaskModel, TaskViewHolder>(tomorrowOptions) {
            @Override
            protected void onBindViewHolder(@NonNull TaskViewHolder taskViewHolder, final int i, @NonNull final TaskModel taskModel) {
                taskViewHolder.taskName.setText(taskModel.getTaskName());
                taskViewHolder.taskCheckBox.setChecked(taskModel.isCheckbox());
                if (taskModel.isCheckbox()) {
                    taskViewHolder.taskName.setPaintFlags(taskViewHolder.taskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
                taskViewHolder.taskCheckBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference itemRef = getRef(i);
                        itemRef.child("checkbox").setValue(!taskModel.isCheckbox());
                    }
                });
                taskViewHolder.taskDeleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference itemRef = getRef(i);
                        itemRef.removeValue();
                        Toast.makeText(getContext(), "Задача удалена", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @NonNull
            @Override
            public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_row, parent, false);
                return new TaskViewHolder(itemView);
            }
        };
        tomorrowTaskView.setAdapter(tomorrowAdapter);
    }

    private void showComingTask() {
        comingTaskDb = database.getReference("Users" + "/" + currentUser.getUid() + "/" + "Предстоящие");
        comingOptions = new FirebaseRecyclerOptions.Builder<TaskModel>().setQuery(comingTaskDb, TaskModel.class).build();
        comingAdapter = new FirebaseRecyclerAdapter<TaskModel, TaskViewHolder>(comingOptions) {
            @Override
            protected void onBindViewHolder(@NonNull TaskViewHolder taskViewHolder, final int i, @NonNull final TaskModel taskModel) {
                taskViewHolder.taskName.setText(taskModel.getTaskName());
                taskViewHolder.taskCheckBox.setChecked(taskModel.isCheckbox());
                if (taskModel.isCheckbox()) {
                    taskViewHolder.taskName.setPaintFlags(taskViewHolder.taskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
                taskViewHolder.taskCheckBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference itemRef = getRef(i);
                        itemRef.child("checkbox").setValue(!taskModel.isCheckbox());
                    }
                });
                taskViewHolder.taskDeleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference itemRef = getRef(i);
                        itemRef.removeValue();
                        Toast.makeText(getContext(), "Задача удалена", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @NonNull
            @Override
            public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_row, parent, false);
                return new TaskViewHolder(itemView);
            }
        };
        comingTaskView.setAdapter(comingAdapter);
    }

    private void showUnlimitedTask() {
        unlimitedTaskDb = database.getReference("Users" + "/" + currentUser.getUid() + "/" + "Без срока");
        unlimitedOptions = new FirebaseRecyclerOptions.Builder<TaskModel>().setQuery(unlimitedTaskDb, TaskModel.class).build();
        unlimitedAdapter = new FirebaseRecyclerAdapter<TaskModel, TaskViewHolder>(unlimitedOptions) {
            @Override
            protected void onBindViewHolder(@NonNull TaskViewHolder taskViewHolder, final int i, @NonNull final TaskModel taskModel) {
                taskViewHolder.taskName.setText(taskModel.getTaskName());
                taskViewHolder.taskCheckBox.setChecked(taskModel.isCheckbox());
                if (taskModel.isCheckbox()) {
                    taskViewHolder.taskName.setPaintFlags(taskViewHolder.taskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
                taskViewHolder.taskCheckBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference itemRef = getRef(i);
                        itemRef.child("checkbox").setValue(!taskModel.isCheckbox());
                    }
                });
                taskViewHolder.taskDeleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference itemRef = getRef(i);
                        itemRef.removeValue();
                        Toast.makeText(getContext(), "Задача удалена", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @NonNull
            @Override
            public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_row, parent, false);
                return new TaskViewHolder(itemView);
            }
        };
        unlimitedTaskView.setAdapter(unlimitedAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonAddTask:
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View mView = getLayoutInflater().inflate(R.layout.dialog_task, null);
                final EditText editTextTaskName = mView.findViewById(R.id.editTextTaskName);
                final ScrollChoice scrollChoiceTime = mView.findViewById(R.id.scrollChoiceTime);
                List<String> time = new ArrayList<>();
                time.add("Сегодня");
                time.add("Завтра");
                time.add("Предстоящие");
                time.add("Без срока");
                scrollChoiceTime.addItems(time, 0);
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
                                String taskTime = scrollChoiceTime.getCurrentSelection();
                                TaskModel taskModel = new TaskModel(taskName, taskTime, false);
                                taskDb = database.getReference("Users" + "/" + currentUser.getUid() + "/" + taskTime);
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
    public void onStart() {
        super.onStart();
        todayAdapter.startListening();
        tomorrowAdapter.startListening();
        comingAdapter.startListening();
        unlimitedAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        todayAdapter.stopListening();
        tomorrowAdapter.stopListening();
        comingAdapter.stopListening();
        unlimitedAdapter.stopListening();
    }
}
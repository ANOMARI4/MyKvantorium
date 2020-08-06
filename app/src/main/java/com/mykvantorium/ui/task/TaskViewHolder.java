package com.mykvantorium.ui.task;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mykvantorium.R;

public class TaskViewHolder extends RecyclerView.ViewHolder /*implements View.OnCreateContextMenuListener*/ {

    public TextView taskName;
    public CheckBox taskCheckBox;
    public ImageButton taskDeleteButton;

    public TaskViewHolder(@NonNull View itemView) {
        super(itemView);
        taskName = itemView.findViewById(R.id.taskName);
        taskCheckBox = itemView.findViewById(R.id.taskCheckbox);
        taskDeleteButton = itemView.findViewById(R.id.taskDeleteButton);
        /*itemView.setOnCreateContextMenuListener(this);*/
    }

    /*@Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle("Меню действий");
        contextMenu.add(0, 0, getAdapterPosition(), "Удалить");
    }*/
}

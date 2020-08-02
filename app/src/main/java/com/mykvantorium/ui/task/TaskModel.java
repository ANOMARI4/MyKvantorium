package com.mykvantorium.ui.task;

public class TaskModel {
    private String taskName, taskTime;
    private boolean checkbox;

    public TaskModel() {
    }

    public TaskModel(String taskName, String taskTime, boolean checkbox) {
        this.taskName = taskName;
        this.taskTime = taskTime;
        this.checkbox = checkbox;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public boolean isCheckbox() {
        return checkbox;
    }

    public void setCheckbox(boolean checkbox) {
        this.checkbox = checkbox;
    }

    public String getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(String taskTime) {
        this.taskTime = taskTime;
    }
}

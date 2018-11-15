package com.proprog.my_todo.viewmodel;

import java.io.Serializable;

public class TaskItem implements Serializable{
    private int viewType;
    public String taskItemName;
    public String taskItemDate;
    public String taskItemType;
    public String taskItemStatus;

    public TaskItem(int viewType, String taskItemName, String taskItemDate, String taskItemType, String taskItemStatus) {
        this.viewType = viewType;
        this.taskItemName = taskItemName;
        this.taskItemDate = taskItemDate;
        this.taskItemType = taskItemType;
        this.taskItemStatus = taskItemStatus;
    }

    public int getViewType() {
        return viewType;
    }

    @Override
    public String toString() {
        return "TaskItem{" +
                "Name='" + taskItemName + '\'' +
                ", Date='" + taskItemDate + '\'' +
                ", Type='" + taskItemType + '\'' +
                ", Status='" + taskItemStatus + '\'' +
                '}';
    }
}

package com.proprog.my_todo.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity(tableName = "task", indices = {@Index("taskStatus")})
public class Task implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int taskID;
    private String taskName;
    private String taskDate;
    private String taskTime;
    private String taskType;
    // not checked->in progress , checked->active
    private String taskStatus;

    public Task(int taskID, String taskName, String taskDate, String taskTime, String taskType, String taskStatus) {
        this.taskID = taskID;
        this.taskName = taskName;
        this.taskDate = taskDate;
        this.taskTime = taskTime;
        this.taskType = taskType;
        this.taskStatus = taskStatus;
    }

    @Ignore
    public Task(String taskName, String taskDate, String taskTime, String taskType, String taskStatus) {
        this.taskName = taskName;
        this.taskDate = taskDate;
        this.taskTime = taskTime;
        this.taskType = taskType;
        this.taskStatus = taskStatus;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }

    public void setTaskTime(String taskTime) {
        this.taskTime = taskTime;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public String getTaskTime() {
        return taskTime;
    }

    public String getTaskType() {
        return taskType;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskID=" + taskID +
                ", taskName='" + taskName + '\'' +
                ", taskDate='" + taskDate + '\'' +
                ", taskTime='" + taskTime + '\'' +
                ", taskType='" + taskType + '\'' +
                ", taskStatus='" + taskStatus + '\'' +
                '}';
    }
}

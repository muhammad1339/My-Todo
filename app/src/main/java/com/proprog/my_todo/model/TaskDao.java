package com.proprog.my_todo.model;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM task ORDER BY taskStatus")
    LiveData<List<Task>> getAllTasks();

    @Query("SELECT * FROM task WHERE taskName=:name")
    Task getTaskByName(String name);

    @Query("SELECT * FROM task WHERE taskType=:type")
    LiveData<List<Task>> getTaskByType(String type);

    @Query("SELECT * FROM task WHERE taskStatus=:state")
    LiveData<List<Task>> getTaskByStatus(String state);

    @Query("SELECT taskID FROM task WHERE taskName=:name")
    int getTaskID(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTask(Task... tasks);

    @Delete
    void deleteTask(Task... tasks);

    @Update
    void updateTask(Task taskToUpdate);

    @Query("UPDATE task SET taskStatus=:status WHERE taskName=:name")
    void updateTaskByName(String name, String status);
}

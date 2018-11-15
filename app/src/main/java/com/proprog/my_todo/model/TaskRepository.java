package com.proprog.my_todo.model;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.support.constraint.Constraints.TAG;

public class TaskRepository {
    private static TaskDao taskDao;

    public TaskRepository(Application application) {
        TaskDB db = TaskDB.getDB_INSTANCE(application, false);
        taskDao = db.getDao();
    }

    public int getTaskID(String name) throws ExecutionException, InterruptedException {
        return new GetTaskId().execute(name).get();
    }
    public LiveData<List<Task>> loadAllTasks() {
        return taskDao.getAllTasks();
    }

    public LiveData<List<Task>> loadTaskByType(String type) {
        return taskDao.getTaskByType(type);
    }

    public LiveData<List<Task>> loadTaskByStatus(String status) {
        return taskDao.getTaskByStatus(status);
    }

    public Task getTaskByName(String name) {
        Task task = null;
        try {
            task = new LoadTaskByName().execute(name).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return task;
    }

    public void updateTask(String name, String status) {
        new UpdateTaskStatus().execute(name, status);
    }

    public void updateTask(Task task) {
        new UpdateTask().execute(task);
        Log.d(TAG, task.toString());
    }

    public void insertTask(Task task) {
        new InsertTask().execute(task);
        Log.d(TAG, "insertTask: " + task.toString());
    }

    public void deleteTask(Task task) {
        new DeleteTask().execute(task);
    }

    private static class GetTaskId extends AsyncTask<String ,Void ,Integer>{

        @Override
        protected Integer doInBackground(String... strings) {
            return taskDao.getTaskID(strings[0]);
        }
    }

    private static class InsertTask extends AsyncTask<Task, Void, Void> {

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.insertTask(tasks[0]);
            return null;
        }
    }

    private static class UpdateTaskStatus extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... tasks) {
            taskDao.updateTaskByName(tasks[0], tasks[1]);
            return null;
        }
    }

    private static class UpdateTask extends AsyncTask<Task, Void, Void> {

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.updateTask(tasks[0]);
            return null;
        }
    }

    private static class DeleteTask extends AsyncTask<Task, Void, Void> {

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.deleteTask(tasks[0]);
            return null;
        }
    }

    private static class LoadTaskByName extends AsyncTask<String, Void, Task> {

        @Override
        protected Task doInBackground(String... strings) {
            Task task = taskDao.getTaskByName(strings[0]);
            return task;
        }
    }
}

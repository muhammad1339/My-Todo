package com.proprog.my_todo.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.proprog.my_todo.R;
import com.proprog.my_todo.model.Task;
import com.proprog.my_todo.model.TaskRepository;
import com.proprog.my_todo.service.TaskAlarmMgr;
import com.proprog.my_todo.view.activities.InputActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.support.constraint.Constraints.TAG;

public class TaskViewModel extends ViewModel {
    public String taskName = "";
    public String taskDate = "";
    public String taskTime = "";
    private String taskType = "";
    private TaskRepository repository;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    public TaskViewModel(Application application) {
        super(application);
        repository = new TaskRepository(application);
    }

    public void launchInputActivity() {
        Intent intent = new Intent(getApplication().getApplicationContext(), InputActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("purpose", "save");
        getApplication().startActivity(intent);
    }

    public void saveTask() {
        if (isFieldsEmpty()) {
            Task task = new Task(taskName, taskDate, taskTime, taskType, "in progress");
            repository.insertTask(task);
            Task savedTask = repository.getTaskByName(taskName);
            new TaskAlarmMgr().setAlarmManager(getApplication().getApplicationContext(), savedTask, savedTask.getTaskID());
            notifyChange();
        } else {
            Log.d(TAG, "Fields is Empty");
        }
    }

    public LiveData<List<TaskItem>> loadAllTasks() {
        return
                Transformations.map(repository.loadAllTasks(), input -> {
                    List<TaskItem> allItems = new ArrayList<>();
                    List<TaskItem> inProgressItems = new ArrayList<>();
                    List<TaskItem> activeItems = new ArrayList<>();

                    if (input.size() > 0) {
                        for (Task task : input) {
                            if (task.getTaskStatus().equals("in progress")) {
                                inProgressItems.add(new TaskItem(1, task.getTaskName(),
                                        task.getTaskDate() + " , " + task.getTaskTime()
                                        , task.getTaskType()
                                        , task.getTaskStatus())
                                );
                            } else if (task.getTaskStatus().equals("active")) {
                                activeItems.add(new TaskItem(1, task.getTaskName(),
                                        task.getTaskDate() + " , " + task.getTaskTime()
                                        , task.getTaskType()
                                        , task.getTaskStatus())
                                );
                            }
                        }

                    }
                    if (inProgressItems.size() > 0) {
                        allItems.add(new TaskItem(0, "",
                                "", "In Progress", "in progress"));
                        allItems.addAll(inProgressItems);
                    }
                    if (activeItems.size() > 0) {
                        allItems.add(new TaskItem(0, "",
                                "", "Finished", "active"));
                        allItems.addAll(activeItems);
                    }
                    return allItems;
                });

    }

    public void updateTaskFields(Task task) {
        taskName = task.getTaskName();
        taskType = task.getTaskType();
        taskDate = task.getTaskDate();
        taskTime = task.getTaskTime();
        notifyChange();
    }

    public boolean updateTaskDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        Calendar selectedCalendar = Calendar.getInstance();
        selectedCalendar.set(year, month, day, hour > 0 ? hour : 0, minute >= 0 ? minute : 0, 0);
        Calendar currentCalendar = Calendar.getInstance();
        int currentYear = currentCalendar.get(Calendar.YEAR);
        int currentMonth = currentCalendar.get(Calendar.MONTH);
        int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
        int currentHour = currentCalendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = currentCalendar.get(Calendar.MINUTE);
        currentCalendar.set(currentYear, currentMonth, currentDay, hour > 0 ? currentHour : 0, minute > 0 ? currentMinute : 0, 0);
        long dif = selectedCalendar.getTimeInMillis() - currentCalendar.getTimeInMillis();
        Log.d(TAG, "onDateSet: " + dif);
        boolean timeValid = false;
        if (hour > 0 && minute >= 0) {
            timeValid = updateTaskTime(hour, minute);
        }
        Log.d(TAG, "onDateSet: " + timeValid);
        if (dif > -1) {
            taskDate = getApplication().getString(R.string.date_format, year, month + 1, day);
            notifyChange();
            return true;
        }
        return false;
    }

    public boolean updateTaskTime(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
        Calendar currentCalendar = Calendar.getInstance();
        int currentYear = currentCalendar.get(Calendar.YEAR);
        int currentMonth = currentCalendar.get(Calendar.MONTH);
        int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
        int currentHour = currentCalendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = currentCalendar.get(Calendar.MINUTE);
        currentCalendar.set(currentYear, currentMonth, currentDay, currentHour, currentMinute, 0);
        Calendar selectedCalender = Calendar.getInstance();
        selectedCalender.set(year, month, day, hour, minute, 0);

        long dif = selectedCalender.getTimeInMillis() - currentCalendar.getTimeInMillis();
        Log.d(TAG, "updateTaskTime: " + dif);
        if (dif > -1) {
            taskTime = getApplication().getString(R.string.time_format, hour, minute);
            notifyChange();
            return true;
        }
        return false;
//        if (year <= currentYear && month <= currentMonth && day <= currentDay) {
//            if (hour == currentHour) {
//                if (minute > currentMinute) {
//                    taskTime = getApplication().getString(R.string.time_format, hour, minute);
//                    notifyChange();
//                    Log.d(TAG, "1-updateTaskTime: " + taskTime);
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//            if (hour >= currentHour) {
//                taskTime = getApplication().getString(R.string.time_format, hour, minute);
//                notifyChange();
//                Log.d(TAG, "2-updateTaskTime: " + taskTime);
//                return true;
//            } else {
//                return false;
//            }
//        } else {
//            taskTime = getApplication().getString(R.string.time_format, hour, minute);
//            Log.d(TAG, "3-updateTaskTime: " + taskTime);
//            notifyChange();
//            return true;
//        }
    }

    public void updateTaskType(String type) {
        taskType = type;
        notifyChange();
    }

    public void updateTaskStatus(boolean state, String name) {
        String status;
        if (state) {
            status = "active";
        } else {
            status = "in progress";
        }
        repository.updateTask(name, status);
        notifyChange();
    }

    public Task loadTaskByName(String name) {
        return repository.getTaskByName(name);
    }

    public void updateTask(String name, String status) {
        int id;
        notifyChange();
        try {
            id = repository.getTaskID(name);
            Task task = new Task(id, taskName, taskDate, taskTime, taskType, status);
            repository.updateTask(task);
            new TaskAlarmMgr().setAlarmManager(getApplication().getApplicationContext(), task, task.getTaskID());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void deleteTask(Task task) {
        int id = 0;
        try {
            id = repository.getTaskID(task.getTaskName());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        repository.deleteTask(task);
        new TaskAlarmMgr().cancelAlarmManager(getApplication().getApplicationContext(), task, id);
    }

    public boolean isFieldsEmpty() {
        return !TextUtils.isEmpty(taskName)
                && !TextUtils.isEmpty(taskDate)
                && !TextUtils.isEmpty(taskTime);
    }

    public boolean isFieldNameEmpty() {
        return TextUtils.isEmpty(taskName);
    }

    public boolean isFieldDateEmpty() {
        return TextUtils.isEmpty(taskDate);
    }

    public boolean isFieldTimeEmpty() {
        return TextUtils.isEmpty(taskTime);
    }
}

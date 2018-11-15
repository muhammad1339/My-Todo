package com.proprog.my_todo.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.proprog.my_todo.model.Task;
import com.proprog.my_todo.viewmodel.TaskItem;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.support.constraint.Constraints.TAG;

public class TaskAlarmMgr {

    public void setAlarmManager(Context context, Task taskItem, int requestCode) {
        Log.d(TAG, "setAlarmManager: " + taskItem.toString());

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("taskItem", taskItem);
        intent.putExtra("position", requestCode);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar = Calendar.getInstance();

        String[] ymd = taskItem.getTaskDate().split("-");
        String[] hm = taskItem.getTaskTime().split(":");
        int year = Integer.parseInt(ymd[0]);
        int month = Integer.parseInt(ymd[1]) - 1;
        int day = Integer.parseInt(ymd[2]);
        int hour = Integer.parseInt(hm[0]);
        int min = Integer.parseInt(hm[1]);
        calendar.set(year, month, day, hour, min, 0);

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        Log.d(TAG, "setAlarmManager: " + calendar.getTime().toString());
    }

    public void cancelAlarmManager(Context context, Task taskItem, int requestCode) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("taskItem", taskItem);
        intent.putExtra("position", requestCode);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
    }

}

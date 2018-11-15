package com.proprog.my_todo.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.proprog.my_todo.R;
import com.proprog.my_todo.model.Task;
import com.proprog.my_todo.view.activities.InputActivity;
import com.proprog.my_todo.view.activities.MainActivity;
import com.proprog.my_todo.viewmodel.TaskItem;

import static android.support.constraint.Constraints.TAG;


public class NotificationService extends IntentService {


    public NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Task taskItem = (Task) intent.getSerializableExtra("taskItem");
        int position = intent.getIntExtra("position", -1);
        Intent mIntent = new Intent(this, InputActivity.class);
        mIntent.putExtra("task", taskItem);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(getApplicationContext(),
                        0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManagerCompat managerCompat =
                NotificationManagerCompat.from(this);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, taskItem.getTaskName());
        builder.setContentText(taskItem.getTaskDate());
        builder.setContentTitle(taskItem.getTaskName() + taskItem.getTaskID());
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.drawable.ic_action_time);
        builder.setAutoCancel(true);
        Notification notification = builder.build();
        managerCompat.notify(position, notification);
        Log.d(TAG, "onHandleIntent: " + "Service is Started");
        Log.d(TAG, "onHandleIntent: " + position);
        stopSelf();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.d(TAG, "onTaskRemoved: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }
}

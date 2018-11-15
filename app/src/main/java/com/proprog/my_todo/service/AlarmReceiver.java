package com.proprog.my_todo.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import com.proprog.my_todo.R;
import com.proprog.my_todo.model.Task;

import static android.support.constraint.Constraints.TAG;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent mIntent = new Intent(context, NotificationService.class);
        Uri notifyUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone ringtone = RingtoneManager.getRingtone(context,notifyUri);
        ringtone.play();
        Task taskItem = (Task) intent.getSerializableExtra("taskItem");
        int position = intent.getIntExtra("position",-1);
        mIntent.putExtra("taskItem", taskItem);
        mIntent.putExtra("position", position);
        context.startService(mIntent);
    }
}

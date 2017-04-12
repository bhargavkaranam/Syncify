package com.example.root.syncify;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.widget.Toast;
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent arg1) {
        Toast.makeText(context, "Alarm received!", Toast.LENGTH_LONG).show();
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        Ringtone r = RingtoneManager.getRingtone(context, notification);
        r.play();
        Vibrator vibrator = (Vibrator)context
                .getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(5000);
    }
}
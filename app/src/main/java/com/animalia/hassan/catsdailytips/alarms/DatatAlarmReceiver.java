package com.animalia.hassan.catsdailytips.alarms;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

import com.animalia.hassan.catsdailytips.service.TipsHandlerService;

public class DatatAlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Intent service1 = new Intent(context, TipsHandlerService.class);
        context.startService(service1);
    }

    public void setAlarmData(Context context)
    {


        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intentA = new Intent(context, DatatAlarmReceiver.class);
        if(PendingIntent.getBroadcast(context, 0,intentA,PendingIntent.FLAG_NO_CREATE) == null) {
            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intentA, 0);

            // Set the alarm to start at approximately 2:00 p.m.
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 18);

            // With setInexactRepeating(), you have to use one of the AlarmManager interval
            // constants--in this case, AlarmManager.INTERVAL_DAY.
            alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, alarmIntent);
            Log.d("reciver .....Data"," NEW is set");
        } else{
            Log.d("reciver .....Data","is already set");
        }
    }


    public void CancelAlarm(Context context)
    {
        Intent intent = new Intent(context, DatatAlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}

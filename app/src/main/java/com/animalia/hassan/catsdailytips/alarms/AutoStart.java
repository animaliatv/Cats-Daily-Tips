package com.animalia.hassan.catsdailytips.alarms;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

public class AutoStart extends BroadcastReceiver {

    DatatAlarmReceiver datatAlarmReceiver;
    PrefDAlarmReceiver prefDAlarmReceiver;


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
/*        if("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            // Set the alarm here.

            SharedPreferences pref = context.getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
            int oldrowsAdd = pref.getInt("rowsAdd", 0);
            int rowsAdd = oldrowsAdd + 1;
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("rowsAdd", rowsAdd );        // Saving integer
            editor.apply();
            CatsTandQHelper catsTandQHelper = CatsTandQHelper.getInstance(context);
            Tips singleTip = catsTandQHelper.getOneTip(rowsAdd);
            if (singleTip == null){
                Log.d("Cursor . . . is ","empty");

            } else {
                String title = singleTip.getTipTitle();
                String body = singleTip.getTipBody();
                int iconR = R.drawable.ic_share;

                Intent intenT = new Intent(context, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 *//* Request code *//*, intent,
                        PendingIntent.FLAG_ONE_SHOT);

                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                        .setSmallIcon(iconR)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(0 *//* ID of notification *//*, notificationBuilder.build());
            }

            *//*datatAlarmReceiver.setAlarmData(context);
            prefDAlarmReceiver.setAlarmPref(context);*//*
        }*/

        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intentA = new Intent(context, DatatAlarmReceiver.class);
        if(PendingIntent.getBroadcast(context, 0,intentA,PendingIntent.FLAG_NO_CREATE) == null) {
            PendingIntent alarmIntentD = PendingIntent.getBroadcast(context, 0, intentA, 0);

            // Set the alarm to start at approximately 2:00 p.m.
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 23);

            // With setInexactRepeating(), you have to use one of the AlarmManager interval
            // constants--in this case, AlarmManager.INTERVAL_DAY.
            alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, alarmIntentD);
            Log.d("reciver .....Data"," NEW is set");
        } else{
            Log.d("reciver .....Data","is already set");
        }

        AlarmManager alarmMangr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intentd = new Intent(context, PrefDAlarmReceiver.class);
        if (PendingIntent.getBroadcast(context, 1,intentd,PendingIntent.FLAG_NO_CREATE) == null) {
            PendingIntent alarmIntentP = PendingIntent.getBroadcast(context, 1, intentd, 0);
            // Set the alarm to start at approximately 8:00 p.m.
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 18);

            // With setInexactRepeating(), you have to use one of the AlarmManager interval
            // constants--in this case, AlarmManager.INTERVAL_DAY.
            alarmMangr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, alarmIntentP);
            Log.d("reciver .....Pref"," NEW is set");
        }else{
            Log.d("reciver .....Pref","is already set");
        }

    }

}

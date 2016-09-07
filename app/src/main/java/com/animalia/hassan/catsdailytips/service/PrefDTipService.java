package com.animalia.hassan.catsdailytips.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.animalia.hassan.catsdailytips.BuildConfig;
import com.animalia.hassan.catsdailytips.database.CatsTandQHelper;
import com.animalia.hassan.catsdailytips.activties.MainActivity;
import com.animalia.hassan.catsdailytips.R;
import com.animalia.hassan.catsdailytips.fragmentsTips.Tips;

public class PrefDTipService extends Service {
    Context ctx = this;
    String json_url = BuildConfig.json_url_t;
    private Looper mServiceLooper;
    private PrefDTipService.ServiceHandler mServiceHandler;

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            // Normally we would do some work here, like download a file.

            SharedPreferences pref = ctx.getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
            int oldrowsAdd = pref.getInt("rowsAdd", 15);
            int rowsAdd = oldrowsAdd + 1;
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("rowsAdd", rowsAdd );        // Saving integer
            editor.apply();

            CatsTandQHelper catsTandQHelper = CatsTandQHelper.getInstance(ctx);
            Tips singleTip = catsTandQHelper.getOneTip(rowsAdd);
            if (singleTip == null){
                Log.d("Cursor . . . is ","empty");

            } else {
                String title = singleTip.getTipTitle();
                String body = singleTip.getTipBody();
                int iconR = R.drawable.cat_icon;

                Intent intent = new Intent(ctx, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0 /* Request code */, intent,
                        PendingIntent.FLAG_ONE_SHOT);

                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(ctx)
                        .setSmallIcon(iconR)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
            }


            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelf(msg.arg1);
        }
    }

    @Override
    public void onCreate() {
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new PrefDTipService.ServiceHandler(mServiceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Updating Tips", Toast.LENGTH_SHORT).show();

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Tips Ready", Toast.LENGTH_SHORT).show();
    }

}

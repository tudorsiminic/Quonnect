package com.project.student.saver;


import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service {
    static final String TAG = "UpdateService";
    long delay = 360000000;
    private Updater updater;
    static final int NOTIFICATION_ID = 89;
    NotificationManager mNotifyMgr;
    SaverApplication app;


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    NotificationCompat.Builder mBuilder;
    NotificationManager mNotificationManager;
    NotificationCompat.Builder notifBuilder;
    SharedPreferences prefs;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate");
        app = (SaverApplication) getApplication();
        this.updater = new Updater();
        this.app = (SaverApplication) getApplication();
        mBuilder = new NotificationCompat.Builder(this);
        prefs = getSharedPreferences(SplashScreen.PREFS_NAME, 0);
        // set the notification properties

        mBuilder.setSmallIcon(R.drawable.firstlogo);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //when clicked go to this activity
		Intent resultIntent = new Intent(this, MainActivity.class);
		mBuilder.setContentIntent(PendingIntent.getActivity(this, 1, resultIntent, Intent.FLAG_ACTIVITY_NEW_TASK));

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        notifBuilder = new NotificationCompat.Builder(this);
        this.updater.start();
        this.app.setServiceRunning(true);


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");

        this.updater.interrupt();
        this.updater = null;
        this.app.setServiceRunning(false);
    }


    class Updater extends Thread {

        private boolean running;

        public Updater() {
            super("UpdaterService-Updater");
            running = true;
        }


        @Override
        public void run() {

            while (running) {
                try {
                    delay = 36000*prefs.getInt("frequency", 5);
                    Thread.sleep(delay);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                    running = false;
                }
                Log.d(TAG, "Update task start");

                Log.e("change", String.valueOf(app.isChange()));
                if (prefs.getBoolean("notifications", false)) {
//                    app.setChange(false);

                    mBuilder.setContentTitle(app.getBudgetStatus());
                    mBuilder.setContentText(String.valueOf(app.getSpent()) + "/" + String.valueOf(app.getBudget()));
                    //Vibration
                    if(prefs.getBoolean("vibration", true)){
                        mBuilder.setVibrate(new long[]{500, 500});
                        mBuilder.setLights(Color.RED, 3000, 3000);
                    }
                    else{
                        mBuilder.setVibrate(new long[]{0});
                    }
                    if(prefs.getBoolean("sound", true)) {
                        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        mBuilder.setSound(alarmSound);
                    }
                    mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
                    Log.d(TAG, "Update task stopped. Sleeping");
                }

            }
        }
    }
}


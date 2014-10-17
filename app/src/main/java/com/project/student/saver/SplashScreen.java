package com.project.student.saver;



import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;


public class SplashScreen extends Activity {

    public static final String PREFS_NAME = "MyPrefsFile";

    SharedPreferences.Editor editor;
    SharedPreferences  prefs;
    DbManager myDb = new DbManager(this);

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash);
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        // set the notification properties
//        mBuilder.setSmallIcon(R.drawable.firstlogo);
//        mBuilder.setContentTitle("Student Saver");
//        mBuilder.setContentText("You are on budget for today");
//        // creates the intent for the next activity
//        //Intent resultIntent = new Intent(this, SecondActivity.class);
//        //mBuilder.setContentIntent(PendingIntent.getActivity(this, 1, resultIntent, 1));
//        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        mNotificationManager.notify(1, mBuilder.build());
        prefs = getSharedPreferences(PREFS_NAME, 0);
        editor = prefs.edit();

        Handler handler = new Handler();
        // run a thread after 2 seconds to start the home screen
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                // make sure we close the splash screen so the user won't come back when it presses back key
                finish();
                // start the home screen
                if(prefs.getBoolean("isfirst", true))
                {
                    editor.putBoolean("isfirst", false);
                    editor.commit();
                    startActivity(new Intent(SplashScreen.this, SetUpActivity.class));
                }
                else{
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);

                    SplashScreen.this.startActivity(intent);
                }

            }

        }, 2000); // time in milliseconds (1 second = 1000 milliseconds) until the run() method will be called

    }

}
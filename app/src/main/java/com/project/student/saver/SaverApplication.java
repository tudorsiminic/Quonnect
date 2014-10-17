package com.project.student.saver;

import android.app.Application;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;

import java.util.Calendar;

public class SaverApplication extends Application {

    Calendar calendar = Calendar.getInstance();
    boolean change = false;
    boolean serviceRunning = false;
    boolean notifOn = false;
    SharedPreferences.Editor editor;
    SharedPreferences  prefs;
    int frequency ;
    String type;
    DbManager myDb;
    float spent = 0;
    float budget = 0;
    final static String ON_BUDGET = "On budget";
    final static String OVER_BUDGET = "Over budget";
    final static String LOW_BUDGET = "Low budget";

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        myDb = new DbManager(getApplicationContext());
        notifyChanges();
    }

    public void notifyChanges()
    {
        prefs = getSharedPreferences(SplashScreen.PREFS_NAME, 0);
        editor = prefs.edit();
        notifOn = prefs.getBoolean("notification", true);
        frequency = prefs.getInt("frequency", 6)*1000;
        type = prefs.getString("type", "Daily");
        budget = prefs.getInt("budget", 0);
        setBudgetStatus();
        setChange(true);


    }

    public void setBudgetStatus()
    {
        if(type.equals("Daily"))
        {
            spent = myDb.getTodayExpenses(calendar);
        }
        else if(type.equals("Weekly"))
        {
            spent = myDb.getWeeklyExpenses(calendar);
        }
        else
            spent = myDb.getMonthlyExpenses(calendar);
    }
    public boolean isChange() {
        return change;
    }


    public void setChange(boolean change) {
        this.change = change;
    }

    public boolean isServiceRunning() {
        return serviceRunning;
    }

    public void setServiceRunning(boolean serviceRunning) {
        this.serviceRunning = serviceRunning;
    }

    public String getBudgetStatus()
    {
        if(budget< spent)
        {
            return OVER_BUDGET;
        }
        else if(budget*0.7 <spent)
        {
            return LOW_BUDGET;
        }
        else
            return ON_BUDGET;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public float getSpent() {
        return spent;
    }

    public int getFrequency() {
        return frequency;
    }

    public float getBudget() {
        return budget;
    }
}

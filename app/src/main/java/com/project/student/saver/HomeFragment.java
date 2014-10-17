package com.project.student.saver;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.graphics.Color;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewAnimator;

import java.util.Calendar;

public class HomeFragment extends Fragment {

    TextView field1, field2, field3;
    float dailyAvg;
    float spToday=0;
    float spWeek=0;
    float spMonth=0;
    SharedPreferences prefs;
    DbManager dbm;
    Calendar calendar;
    Button dateDisplay;
    DialogFragment dateFragment;
    View view;
    boolean isFirstCalendar = true;
    ProgressBar pbToday, pbWeek, pbMonth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.home_activity, container, false);
        /* Calendar issues */

        dateDisplay = (Button)view.findViewById(R.id.dateDisplay);
        pbToday = (ProgressBar)view.findViewById(R.id.progress_spent_today);
        pbWeek = (ProgressBar)view.findViewById(R.id.progress_spent_week);
        pbMonth  = (ProgressBar)view.findViewById(R.id.progress_spent_month);
        calendar = Calendar.getInstance();
        dateDisplay.setText(calendar.get(Calendar.DAY_OF_MONTH) + "-" + String.valueOf(calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.YEAR));
        dateDisplay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        dbm = new DbManager(getActivity().getApplicationContext());
        prefs = getActivity().getSharedPreferences(SettingsFragment.PREFS_NAME, Context.MODE_PRIVATE);
        dailyAvg = calculateDailyAvg(prefs.getInt("budget", 0), prefs.getString("type", "null"));
        updateFields();

        return view;
    }

    public void showDatePickerDialog(View v) {
        dateFragment = new DatePickerFragment();
        dateFragment.show(getActivity().getFragmentManager(), "datePicker");
    }

    class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            if (isFirstCalendar) {
                isFirstCalendar = false;
                return new DatePickerDialog(getActivity(), this, year, month, day);
            }
            else {
                return new DatePickerDialog(getActivity(), this,  calendar.get(Calendar.YEAR),  calendar.get(Calendar.MONTH),  calendar.get(Calendar.DAY_OF_MONTH));
            }
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            dateDisplay.setText( calendar.get(Calendar.DAY_OF_MONTH) + "-" + String.valueOf(calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.YEAR));
            updateFields();
        }
    }

    public void updateFields() {
        spToday = dbm.getTodayExpenses(calendar);
        spWeek= dbm.getWeeklyExpenses(calendar);
        spMonth = dbm.getMonthlyExpenses(calendar);
        field1 = (TextView)view.findViewById(R.id.todayData);
        field1.setText(String.valueOf(spToday)+"/"+String.valueOf(dailyAvg));
        field2 = (TextView)view.findViewById(R.id.weekData);
        field2.setText(String.valueOf(spWeek)+"/"+String.valueOf(dailyAvg*7));
        field3 = (TextView)view.findViewById(R.id.monthData);
        field3.setText(String.valueOf(spMonth)+"/"+String.valueOf(dailyAvg*30));
        if(spToday>dailyAvg) {
            field1.setTextColor(Color.parseColor("#FF0000"));
        }
        else {
            field1.setTextColor(Color.parseColor("#00FF00"));
        }

        if(spWeek>dailyAvg*7) {
            field2.setTextColor(Color.parseColor("#FF0000"));
        }
        else {
            field2.setTextColor(Color.parseColor("#00FF00"));
        }

        if(spMonth>dailyAvg*30) {
            field3.setTextColor(Color.parseColor("#FF0000"));
        }
        else {
            field3.setTextColor(Color.parseColor("#00FF00"));
        }
        Log.e("max",String.valueOf(pbMonth.getMax()));
        if(pbToday.getMax()*spToday/dailyAvg>pbToday.getMax())
            pbToday.setProgress(pbToday.getMax());
        else
            pbToday.setProgress((int) (pbToday.getMax()*spToday/dailyAvg));
        if(pbWeek.getMax()*spWeek/(dailyAvg*7)>pbWeek.getMax())
            pbWeek.setProgress(pbWeek.getMax());
        else
            pbWeek.setProgress((int) (pbWeek.getMax()*spWeek/(dailyAvg*7)));
        if(pbMonth.getMax()*spMonth/(dailyAvg*30)>pbMonth.getMax())
            pbMonth.setProgress(pbMonth.getMax());
        else
            pbMonth.setProgress((int) (pbMonth.getMax()*spMonth/(dailyAvg*30)));
    }

    public float calculateDailyAvg(int budget, String type)
    {
        Log.e("type of budget", type);
        if (type.equals("Monthly")) {
            return(float) ((float)budget / 30.0);
        } else if (type.equals("Weekly")) {
            return (float) ((float)budget / 7.0);
        } else {
            return budget;
        }
    }

}



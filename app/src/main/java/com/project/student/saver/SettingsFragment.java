package com.project.student.saver;

import java.util.ArrayList;
import java.util.List;


import android.app.Activity;
import android.app.Fragment;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsFragment extends Fragment {

    Button btnDone;
    View view;
    Spinner spinner;
    String name,spin;
    int budget;
    TextView txtName, txtBudget;
    EditText txtFrequency,txtStart;
    String time_period;
    Switch notificationsEnable;
    Switch soundEnable;
    Switch vibrationEnable;
    boolean notifications;
    boolean vibration;
    boolean sound;
    int frequency;
    float starting_at;
    SharedPreferences prefs;
    DbManager myDb;
    String type;
    SharedPreferences.Editor editor;
    SaverApplication app;

    public static final String PREFS_NAME = "MyPrefsFile";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        View view = inflater.inflate(R.layout.settings,
                container, false);
        app = (SaverApplication) getActivity().getApplication();
        txtName = (TextView)view.findViewById(R.id.txtName);
        txtBudget = (TextView)view.findViewById(R.id.txtBudget);
        spinner = (Spinner)view.findViewById(R.id.spinner);
        btnDone = (Button) view.findViewById(R.id.btnDone);
        txtFrequency = (EditText)view.findViewById(R.id.txtFrequency);
        notificationsEnable=(Switch)view.findViewById(R.id.not_switch);
        soundEnable=(Switch)view.findViewById(R.id.sound_switch);
        vibrationEnable=(Switch)view.findViewById(R.id.vibration_switch);
        populateSpinner();

        prefs = getActivity().getSharedPreferences(PREFS_NAME, 0);
        editor = prefs.edit();
        notificationsEnable.setChecked(prefs.getBoolean("notifications", true));
        soundEnable.setChecked(prefs.getBoolean("sound", true));
        vibrationEnable.setChecked(prefs.getBoolean("vibration", true));
        txtFrequency.setText(String.valueOf(prefs.getInt("frequency", 2000)));
        txtBudget.setText(String.valueOf(prefs.getInt("budget", 0)));
        txtName.setText(prefs.getString("name", ""));
        ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter(); //cast to an ArrayAdapter
        int spinnerPosition = myAdap.getPosition(prefs.getString("type", "Daily"));
        spinner.setSelection(spinnerPosition);

        myDb = new DbManager(getActivity().getApplicationContext());

        btnDone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                getData();
                updateData();
                popMessage();
                editor.commit();
                app.notifyChanges();
                if(app.isServiceRunning()) {
                    try {
                        getActivity().stopService(new Intent(getActivity(), MyService.class));
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
                if(prefs.getBoolean("notifications", true))
                     getActivity().startService(new Intent(getActivity(), MyService.class));
                ((MainActivity)getActivity()).goHome();
            }
        });

        return view;
    }


    public void updateData()
    {
        editor.putInt("budget", budget);
        editor.putInt("frequency", frequency);
        editor.putBoolean("notifications",notifications);
        editor.putBoolean("sound",sound);
        editor.putBoolean("vibration", vibration);
        editor.putFloat("starting_at", starting_at);
        editor.putString("type", time_period);
        editor.putString("name", name);
    }

    public void popMessage()
    {

        Toast.makeText(getActivity().getApplicationContext(), "Successfully submitted!" , Toast.LENGTH_SHORT).show();

    }
    public void getData() {
        name=txtName.getText().toString();
        if (!txtBudget.getText().toString().equals("")) {
            budget=Integer.parseInt(txtBudget.getText().toString());
        }
        else
            budget=0;
        time_period=spinner.getSelectedItem().toString();
        if (!txtFrequency.getText().toString().equals("")) {
            frequency=Integer.parseInt(txtFrequency.getText().toString());
        }
        else frequency=0;

        if(notificationsEnable.isChecked())
        {
            notifications=true;
        }
        else
        {
            notifications=false;
        }
        if(soundEnable.isChecked())
        {
            sound=true;
        }
        else
        {
            sound=false;
        }
        if(vibrationEnable.isChecked())
        {
            vibration=true;
        }
        else
        {
            vibration=false;
        }

//		ContentValues cv= new ContentValues();
//		cv.put(myDb.getDatabase().ENABLE_NOT, notifications);
//		cv.put(myDb.getDatabase().ENABLE_VIB, vibration);
//		cv.put(myDb.getDatabase().ENABLE_SOUND, sound);
//		cv.put(myDb.getDatabase().FREQUENCY, frequency);
//		cv.put(myDb.getDatabase().STARTING_AT, starting_at);
//		cv.put(myDb.getDatabase().NAME	, name);
//		cv.put(myDb.getDatabase().BUDGET, budget);
//		cv.put(myDb.getDatabase().TIME_PERIOD, time_period);
//		myDb.populateSettings(cv);
        //	Log.e("name: ", name);
        //	Log.e("budget:", String.valueOf(budget));
        //	Log.e("Spinner:", time_period);
    }

    public void populateSpinner() {
        List<String> list = new ArrayList<String>();
        list.add("Daily");
        list.add("Weekly");
        list.add("Monthly");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),R.layout.spinner_item_text, list);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item_down);
        spinner.setAdapter(dataAdapter);
    }

}


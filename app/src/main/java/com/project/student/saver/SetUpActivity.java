package com.project.student.saver;

import java.util.ArrayList;
import java.util.List;

import com.project.student.saver.R.string;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class SetUpActivity extends Activity {

	Button btnDone;
	View view;
	Spinner spinner;
	String name="",spin;
	int budget;
	TextView txtName, txtBudget;
	String timePeriod;

    SharedPreferences.Editor editor;
    SharedPreferences  prefs;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.set_up);
		
		txtName = (TextView)findViewById(R.id.txtName);
		txtBudget = (TextView)findViewById(R.id.txtBudget);
		spinner = (Spinner)findViewById(R.id.spinner);
		btnDone = (Button) findViewById(R.id.btnDone);
        prefs = getSharedPreferences(SplashScreen.PREFS_NAME, 0);
        editor = prefs.edit();
		
		populateSpinner();
		
		btnDone.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				getData();
                editor.putInt("budget", budget);
                editor.putString("type", timePeriod);
                editor.putString("name", name);
                editor.putBoolean("notification", true);
                editor.commit();
                startActivity(new Intent(SetUpActivity.this, MainActivity.class));
			}
		});
		
	}

	public void getData() {
		if (txtName.getText().toString().equals("")) {
			AlertDialog.Builder builder=new AlertDialog.Builder(this);
			builder.setTitle("Please type in your name!");
			builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			AlertDialog alert=builder.create();
			alert.show();
		}
		else {
			name=txtName.getText().toString();
		}
		if (!txtBudget.getText().toString().equals("")) {
			budget=Integer.parseInt(txtBudget.getText().toString());
		}
		else
			budget=0;
		timePeriod=spinner.getSelectedItem().toString();
		Log.e("name: ", name);
		Log.e("budget:", String.valueOf(budget));
		Log.e("Spinner:", timePeriod);
	}
	
	public void populateSpinner() {
		List<String> list = new ArrayList<String>();
		list.add("Daily");
		list.add("Weekly");
		list.add("Monthly");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_item_text, list);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item_down);
		spinner.setAdapter(dataAdapter);
	}

}

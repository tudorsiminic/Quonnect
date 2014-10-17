package com.project.student.saver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddPurchaseFragment extends Fragment{

    Spinner spinner;
    Button done;
    Double price = 0.0;
    EditText mEdit, msEdit;
    String type;
    String description;
    DbManager myDb;
    SaverApplication app;
    TextView date;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        view = inflater.inflate(R.layout.add_purchase,
                container, false);
        app = (SaverApplication) getActivity().getApplication();

        myDb = new DbManager(getActivity());
        //BUTTONS
        done = (Button) view.findViewById(R.id.btnDone);
        mEdit = (EditText) view.findViewById(R.id.txtDescription);
        msEdit = (EditText) view.findViewById(R.id.txtPrice);
        msEdit.setHint(".00");
        msEdit.requestFocus();
        msEdit.setSelection(0);
        spinner = (Spinner)view.findViewById(R.id.spinner1);

        date = (TextView)view.findViewById(R.id.dateView);
        date.setText(app.getCalendar().get(Calendar.DAY_OF_MONTH) + "-" + String.valueOf(app.getCalendar().get(Calendar.MONTH) + 1) + "-" + app.getCalendar().get(Calendar.YEAR));

        populateSpinner();
        done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!mEdit.getText().toString().equals("")) {
                    description=mEdit.getText().toString();
                }
                else description=" ";
                if(!msEdit.getText().toString().equals("")) {
                    try {
                        price = Double.parseDouble(msEdit.getText().toString());

                    }
                    catch (Exception e) {
                        Log.e("Invalid price!", "Invalid price!");
                    }
                }
                else price=0.0;
                if (price == 0.0) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(view.getContext());
                    builder1.setMessage("Please insert a valid amount!");
                    builder1.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
                else {
                    Toast.makeText(getActivity(), "succesfully submitted!",	Toast.LENGTH_SHORT).show();
                    type = spinner.getSelectedItem().toString();

                    Calendar cal = Calendar.getInstance();
                    //use the db

                    ContentValues cv = new ContentValues();
                    cv.clear();

                    Time now = new Time();
                    now.setToNow();
                    String curr_date = now.monthDay+"/"+String.valueOf(now.month+1)+"/"+now.year;
                    cv.put(myDb.getDatabase().C_CREATED_AT, curr_date);
                    cv.put(myDb.getDatabase().PRICE, price);
                    cv.put(myDb.getDatabase().DESCRIPTION, description);
                    cv.put(myDb.getDatabase().TYPE, type);
                    myDb.addPurchaseData(cv);
                    app.notifyChanges();
                    ((MainActivity)getActivity()).goHome();
                }
            }
        });
        return view;
    }

    public void populateSpinner() {
        List<String> list = new ArrayList<String>();
        list.add("Daily purchase");
        list.add("Extra spendings");
        list.add("Food shopping");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),R.layout.spinner_item_text, list);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item_down);
        spinner.setAdapter(dataAdapter);
    }

}

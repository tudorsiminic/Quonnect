package com.project.student.saver;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SpendingsFragment extends Fragment{

	private  DbManager myDb;
	ImageButton house, settings, money, add;
    ViewGroup myCont;
    long lastId = 0;
    String lastDescription ="", lastPrice="", lastType="";
    SimpleCursorAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		 View view = inflater.inflate(R.layout.spendings,
			        container, false);
        myCont = container;
		 myDb = new DbManager(getActivity().getApplicationContext());
		Cursor c = myDb.getSettingsData();
		ListView lw = (ListView)view.findViewById(R.id.listview);
		Cursor myCursor = myDb.getPurchaseData();
		
//		startService(new Intent(SpendingsActivity.this, MyService.class));#
		//oldAdapter = new SimpleAdapter(this, list, R.layout.item, new String[]{"name","purpose"}, new int[]{R.id.textView1, R.id.textView2});		

		adapter = new SimpleCursorAdapter(getActivity().getApplicationContext(), R.layout.item, myCursor, new String[]{myDb.getDatabase().C_CREATED_AT, myDb.getDatabase().TYPE, myDb.getDatabase().PRICE}, new int[]{R.id.lblDate, R.id.lblType, R.id.lblPrice},getActivity().RESULT_OK);
		myCursor.moveToFirst();
		lw.setAdapter(adapter);
        lw.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> adapterView, View arg1,int position, long arg3)
            {
                Cursor newCursor = (Cursor) adapter.getItem(position);
                String theId = newCursor.getString(newCursor.getColumnIndex(DbHelper.C_ID));
                Log.e("id", String.valueOf(theId));
                lastId = Long.parseLong(theId);
                lastDescription = newCursor.getString(newCursor.getColumnIndex(DbHelper.DESCRIPTION));
                lastPrice = newCursor.getString(newCursor.getColumnIndex(DbHelper.PRICE));
                lastType = newCursor.getString(newCursor.getColumnIndex(DbHelper.TYPE));
//              myDb.removeItem(theId);
                CustomDialog cd = new CustomDialog();
                cd.show(getFragmentManager(),null);
                adapter.notifyDataSetChanged();
            }
        });
		adapter.notifyDataSetChanged();
		return view;
	}

	public void popMessage()
	{
		Toast.makeText(getActivity().getApplicationContext(), "Successfully submitted!" , Toast.LENGTH_SHORT).show();
	}

    class CustomDialog extends DialogFragment {
        Spinner spinner;

        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            builder.setTitle("Purchase information");
            builder.setView(inflater.inflate(R.layout.dialog, null));
            View view = inflater.inflate(R.layout.dialog, myCont, false);
            spinner = (Spinner) view.findViewById(R.id.spinner1);
            EditText price = (EditText) view.findViewById(R.id.txtPriceDialog);
            EditText description = (EditText) view.findViewById(R.id.txtDescription);
            TextView textViewPrice = (TextView) view.findViewById(R.id.lblPrice);
            populateSpinner();
            textViewPrice.setText(lastPrice);
            description.setText(lastDescription);
            spinner.setSelection(1);
            Log.e("Price", lastPrice);
            Log.e("Type", lastType);
            Log.e("Description", lastDescription);
            builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dismiss();
                }
            });
            builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
//                    myDb.removeItem(lastPosition);
                }
            });
            builder.setNeutralButton("Modify", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            //Log.e("id",String.valueOf(lastId));
            adapter.notifyDataSetChanged();
            // Create the AlertDialog object and return it
            return builder.create();
        }

        public void populateSpinner() {
            List<String> list = new ArrayList<String>();
            list.add("Daily purchase");
            list.add("Extra spendings");
            list.add("Food shopping");
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_item_text, list);
            dataAdapter.setDropDownViewResource(R.layout.spinner_item_down);
            spinner.setAdapter(dataAdapter);
        }
    }
}

///////////////////////////////////////////
        //to be used in the superclass

//	@Override
//	public boolean onMenuItemSelected(int featureId, MenuItem item) {
//		// TODO Auto-generated method stub
//		
//		switch(item.getItemId())
//		{
//		case R.id.close:
//			
//			stopService(new Intent(this, MyService.class));
//			System.exit(1);
//			break;
//		case R.id.stopservice:
//			stopService(new Intent(this, MyService.class));
//			break;
//		}
//		return super.onMenuItemSelected(featureId, item);
//	}

//	@Override
//	protected void onDestroy() {
//		// TODO Auto-generated method stub
//		super.onDestroy();
//		myDb.closeDB();
//	
//	}


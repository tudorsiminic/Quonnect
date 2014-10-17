package com.project.student.saver;

import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.format.Time;
import android.util.Log;


public class DbManager {

	
	DbHelper dbHelper;
	SQLiteDatabase db;
    Calendar calendar;

	public DbManager(Context context)
	{
		dbHelper = new DbHelper(context);
		
	}
	
	public void addValues(ContentValues cv, String table)
	{
		db = dbHelper.getWritableDatabase();
		db.insert(table, null, cv);
	}
	
	public Cursor getPurchaseData()
	{
		db = this.dbHelper.getReadableDatabase();
		Cursor c = db.query(dbHelper.TABLE, null, null, null, null, null, dbHelper.GET_ALL_ORDER_BY);
	
		return c;
	}
	
    public void removeItem(String id)
    {
        db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM " + dbHelper.TABLE +" WHERE "+dbHelper.C_ID+" = '" + id + "'");

    }


	
	public void addPurchaseData(ContentValues cv)
	{
		db = dbHelper.getWritableDatabase();
		db.insertOrThrow(dbHelper.TABLE, null, cv);
	}
	
	public Cursor getSettingsData()
	{
		db = this.dbHelper.getReadableDatabase();
		Cursor c = db.query(dbHelper.TABLE, null, null, null, null, null, null);
	
		return c;
	}
	
	public void closeDB()
	{
		db.close();
	}
	
	public DbHelper getDatabase()
	{
		return dbHelper;
	}

    public float getTodayExpenses(Calendar calendar)
    {
        this.calendar = calendar;
        db = this.dbHelper.getReadableDatabase();
        //Calendar cal = Calendar.getInstance();
        float sum = 0;
        Cursor c = db.query(dbHelper.TABLE, null, null, null, null, null, null);
        c.moveToFirst();
        while(!c.isAfterLast())
        {
            //Time now = new Time();
            //now.setToNow();
            int monthDay = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);
            String curr_date = monthDay+"/"+String.valueOf(month+1)+"/"+year;
            if (curr_date.equals(c.getString(c.getColumnIndex(dbHelper.C_CREATED_AT))))
            {
                sum += c.getFloat(c.getColumnIndex(dbHelper.PRICE));
            }
            c.moveToNext();
        }
        c.close();
        return sum;
    }

    public float getWeeklyExpenses(Calendar calendar)
    {
        this.calendar = calendar;
        db = this.dbHelper.getReadableDatabase();
        Calendar cal = Calendar.getInstance();
        float sum = 0;
        Cursor c = db.query(dbHelper.TABLE, null, null, null, null, null, null);
        c.moveToFirst();
        int monthDay = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
//      Time now = new Time();
//      now.setToNow();
        // int dayWeek = now.weekDay; 		// 0-6
        int dayWeek = calendar.get(Calendar.DAY_OF_WEEK); 		// 0-6
        dayWeek++;		// 1-7
        String curr_date;
        while(!c.isAfterLast())
        {
            for (int i=0;i<dayWeek;++i) {
                curr_date = monthDay-i+"/"+String.valueOf(month+1)+"/"+year;
                Log.e("data", curr_date);
                if (curr_date.equals(c.getString(c.getColumnIndex(dbHelper.C_CREATED_AT))))
                {
                    sum += c.getFloat(c.getColumnIndex(dbHelper.PRICE));
                }
            }
            c.moveToNext();
        }
        c.close();
        return sum;
    }

    public float getMonthlyExpenses(Calendar calendar)
    {
        this.calendar = calendar;
        db = this.dbHelper.getReadableDatabase();
        Calendar cal = Calendar.getInstance();
        float sum = 0;
        Cursor c = db.query(dbHelper.TABLE, null, null, null, null, null, null);
        c.moveToFirst();
        int monthDay = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
//      Time now = new Time();
//      now.setToNow();
//      int dayWeek = now.monthDay; 		// 1-31
        int dayWeek = calendar.get(Calendar.DAY_OF_MONTH); 		// 1-31
        String curr_date;
        while(!c.isAfterLast())
        {
            for (int i=0;i<dayWeek;++i) {
                curr_date = monthDay-i+"/"+String.valueOf(month+1)+"/"+year;
                Log.e("data", curr_date);
                if (curr_date.equals(c.getString(c.getColumnIndex(dbHelper.C_CREATED_AT))))
                {
                    sum += c.getFloat(c.getColumnIndex(dbHelper.PRICE));
                }
            }
            c.moveToNext();
        }
        c.close();
        return sum;
    }
}

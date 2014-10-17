package com.project.student.saver;



import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {
	
	static final String TAG = "DbHelper";
	
	static final String DB_NAME = "data.db";
	static final int DB_VERSION = 2;
	
	static final String TABLE = "purchases";
	static final String C_ID = BaseColumns._ID;
	static final String C_CREATED_AT = "created_at";
	static final String TYPE = "type";
	static final String PRICE = "price";
	static final String DESCRIPTION = "description";
	
//	static final String TABLE2 = "settings";
	static final String C_ID2 = BaseColumns._ID;
	static final String NAME = "name";
	static final String BUDGET = "budget";
	static final String TIME_PERIOD = "time_period";
	static final String ENABLE_NOT = "enable_not";
	static final String ENABLE_SOUND = "enable_sound";
	static final String ENABLE_VIB = "enable_vib";
	static final String FREQUENCY = "frequency";
	static final String STARTING_AT = "starting_at";
	static final String FIRST_HIT = "FIRST_HIT";
	
	static final String GET_ALL_ORDER_BY = C_CREATED_AT + " DESC";
	
	Context context;

	public DbHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		this.context=context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table " + TABLE + " ("+ C_ID + " INTEGER PRIMARY KEY, " +
					C_CREATED_AT + " date, "+ TYPE + " text, " + PRICE + " int, " + DESCRIPTION + " text)";
		db.execSQL(sql);
//		String sql1 = "create table " + TABLE2 + " ("+ C_ID2 + " int primary key, " +
//				NAME + " text, "+ BUDGET + " text, " + TIME_PERIOD + " text, "+ ENABLE_NOT +" int, "
//				+ ENABLE_SOUND+" int, "+ ENABLE_VIB +" int, "  + FREQUENCY + " int, " + STARTING_AT + " int, " + FIRST_HIT+" int)";
//		db.execSQL(sql1);
//		for(int i = 1;i<=10;i++)
//		{
//		ContentValues cv = new ContentValues();
//		cv.clear();
//		cv.put(NAME, "a");
//		cv.put(BUDGET, 24);
//		cv.put(TIME_PERIOD, "Daily");
//		cv.put(ENABLE_NOT, 1);
//		cv.put(ENABLE_SOUND, 1);
//		cv.put(ENABLE_VIB, 1);
//		cv.put(FREQUENCY, 6);
//		cv.put(STARTING_AT, 1000);
//		cv.put(FIRST_HIT, 0);
//		db.insert(TABLE2, null, cv);
//		}
//		Log.d(TAG,"Created: "+sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL("drop table if exists "+ TABLE);
		onCreate(db);
		Log.d(TAG, "onUpgrade");
	}

}

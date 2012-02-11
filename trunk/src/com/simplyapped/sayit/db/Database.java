package com.simplyapped.sayit.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database extends SQLiteOpenHelper {

	public static final String TABLE_PHRASES = "phrases";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_PHRASES = "phrases";

	private static final String DATABASE_NAME = "com.simplyapped.sayit.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_PHRASES + "( " + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_PHRASES
			+ " text not null);";

	public Database(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) 
	{
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(Database.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS" + TABLE_PHRASES);
		onCreate(db);
	}

}
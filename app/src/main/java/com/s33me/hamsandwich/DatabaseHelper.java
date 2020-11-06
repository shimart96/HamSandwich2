package com.s33me.hamsandwich;

	import android.content.Context;
	import android.database.sqlite.SQLiteDatabase;
	import android.database.sqlite.SQLiteOpenHelper;

	public class DatabaseHelper extends SQLiteOpenHelper {

		// Table Name
		public static final String TABLE_NAME = "HamContacts";

		// Table columns
		public static final String _ID = "_id";
		public static final String DATE = "date";
		public static final String CALL = "call";
		public static final String NAME = "name";
		public static final String FREQ = "freq";
	

		// Database Information
		static final String DB_NAME = "Ham_Contacts.DB";

		// database version
		static final int DB_VERSION = 1;

		// Creating table query
		private static final String CREATE_TABLE = "create table " + TABLE_NAME + "("
		+ _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
		+ DATE + " DATE NOT NULL," 
		+ CALL + " TEXT, " 
		+ NAME + " TEXT,"
		+ FREQ + " TEXT"+")";

		public DatabaseHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			db.execSQL(CREATE_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			onCreate(db);
		}
	}


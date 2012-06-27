package com.zacck.prproDB;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SqlHandler {

	public static final String KEYNEWS_ROWID = "news_id";
	public static final String KEYNEWS_TIME = "news_time";
	public static final String KEYNEWS_SOURCE = "news_source";
	public static final String KEYNEWS_HEADLINE = "news_headline";
	public static final String KEYNEWS_LINK = "news_link";
	public static final String KEYNEWS_INSTIME = "news_ins_time";
	public static final String KEYNEWS_FLAG = "news_flag";

	public static final String KEYREPUTATION_ROWID = "reputation_id";
	public static final String KEYREPUTATION_USER = "reputation_user";
	public static final String KEYREPUTATION_PLATFORM = "reputation_platform";
	public static final String KEYREPUTATION_TIME = "reputation_time";
	public static final String KEYREPUTATION_SENTIMENT = "reputation_sentiment";
	public static final String KEYREPUTATION_INSTIME = "reputation_ins_time";
	public static final String KEYREPUTATION_TEXT = "reputation_text";

	public static final String KEYHOT_ROWID = "hot_id";
	public static final String KEYHOT_TIME = "hot_time";
	public static final String KEYHOT_SOURCE = "hot_source";
	public static final String KEYHOT_HEADLINE = "hot_headline";
	public static final String KEYHOT_LINK = "hot_link";
	public static final String KEYHOT_INS_TIME = "hot_ins_time";

	public static final String KEY_KEYS_id = "keys_id";
	public static final String KEY_KEYS_word = "keys_word";
	public static final String KEY_KEYS_user = "keys_user";
	public static final String KEY_KEYS_date = "keys_date";

	private static final String DATBASE_NAME = "syncDb";
	private static final String TABLE_TYNEWS = "TbYourNews";
	private static final String TABLE_REPUTATION = "TbReputation";
	private static final String TABLE_HOT_TOPICS = "TbHotTopics";
	private static final int DATABASE_VERSION = 4;
	private static final String TABLE_TYKEYS = "TbKeywords";

	private DbHelper ourHelper;
	private final Context ourCTX;
	public SQLiteDatabase ourDb;

	private static class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context) {
			super(context, DATBASE_NAME, null, DATABASE_VERSION);

		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// create the database using SQL
			db.execSQL("CREATE TABLE " + TABLE_TYNEWS + " (" + KEYNEWS_ROWID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEYNEWS_TIME
					+ " TEXT NOT NULL, " + KEYNEWS_SOURCE + " TEXT NOT NULL, "
					+ KEYNEWS_HEADLINE + " TEXT NOT NULL, " + KEYNEWS_LINK
					+ " TEXT NOT NULL)" + KEYNEWS_FLAG + "TEXT NOT NULL;");

			db.execSQL("CREATE TABLE " + TABLE_HOT_TOPICS + " (" + KEYHOT_ROWID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEYHOT_TIME
					+ " TEXT NOT NULL, " + KEYHOT_SOURCE + " TEXT NOT NULL, "
					+ KEYHOT_HEADLINE + " UNIQUE TEXT NOT NULL, " + KEYHOT_LINK
					+ " TEXT NOT NULL);");

			db.execSQL("CREATE TABLE " + TABLE_REPUTATION + " ("
					+ KEYREPUTATION_ROWID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ KEYREPUTATION_USER + " TEXT NOT NULL, "
					+ KEYREPUTATION_TIME + " TEXT NOT NULL, "
					+ KEYREPUTATION_PLATFORM + " TEXT NOT NULL, "
					+ KEYREPUTATION_SENTIMENT + " INTEGER NOT NULL, "
					+ KEYREPUTATION_TEXT + "TEXT NOT NULL);");
			db.execSQL("CREATE TABLE " + TABLE_TYKEYS + " (" + KEY_KEYS_id
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_KEYS_user
					+ " TEXT NOT NULL, " + KEY_KEYS_word + " TEXT NOT NULL, "
					+ KEY_KEYS_date + " TEXT NOT NULL);");

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// what happens when the database is upgraded
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOT_TOPICS);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPUTATION);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_TYNEWS);

			onCreate(db);

		}

	}

	public SqlHandler(Context c) {
		ourCTX = c;
	}

	public SqlHandler open() throws SQLException {
		ourHelper = new DbHelper(ourCTX);
		ourDb = ourHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		ourHelper.close();
	}

	public long createHotEntry(String Htime, String Hsource, String Hhead,
			String Hlink) {
		ContentValues cv = new ContentValues();

		cv.put(KEYHOT_TIME, Htime);
		cv.put(KEYHOT_SOURCE, Hsource);
		cv.put(KEYHOT_HEADLINE, Hhead);
		cv.put(KEYHOT_LINK, Hlink);

		return ourDb.insert(TABLE_HOT_TOPICS, null, cv);

	}

	public long createkeysEntry(String word, String user, String time) {
		ContentValues cv = new ContentValues();

		cv.put(KEY_KEYS_word, word);
		cv.put(KEY_KEYS_user, user);
		cv.put(KEY_KEYS_date, time);

		return ourDb.insert(TABLE_TYKEYS, null, cv);

	}

	public long createRepEntry(String RUser, String RPlat, String Rtime,
			String RSent, String RText) {
		ContentValues cv = new ContentValues();
		Log.v("Inserted", RUser + RPlat + Rtime + RSent);
		cv.put(KEYREPUTATION_USER, RUser);
		cv.put(KEYREPUTATION_PLATFORM, RPlat);
		cv.put(KEYREPUTATION_SENTIMENT, RSent);
		cv.put(KEYREPUTATION_TIME, Rtime);
		cv.put(KEYREPUTATION_TEXT, RText);

		return ourDb.insert(TABLE_REPUTATION, null, cv);

	}

	public Cursor getNegsPos() {
		Cursor c = null;
		
		

		return c;
	}

	public long createYNEWEntry(String Ntime, String Nsource, String NHead,
			String Nlink, String flag) {
		ContentValues cv = new ContentValues();
		cv.put(KEYNEWS_TIME, Ntime);
		cv.put(KEYNEWS_SOURCE, Nsource);
		cv.put(KEYNEWS_HEADLINE, NHead);
		cv.put(KEYNEWS_LINK, Nlink);
		cv.put(KEYNEWS_FLAG, flag);

		return ourDb.insert(TABLE_TYNEWS, null, cv);

	}

}

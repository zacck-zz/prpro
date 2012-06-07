package com.zacck.prproDB;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

public class SqlHandler {

	public static final String KEYNEWS_ROWID = "news_id";
	public static final String KEYNEWS_TIME = "news_time";
	public static final String KEYNEWS_SOURCE = "news_source";
	public static final String KEYNEWS_HEADLINE = "news_headline";
	public static final String KEYNEWS_LINK = "news_link";
	
	public static final String KEYREPUTATION_ROWID = "reputation_id";
	public static final String KEYREPUTATION_USER = "reputation_id";
	public static final String KEYREPUTATION_PLATFORM = "reputation_id";
	public static final String KEYREPUTATION_TIME = "reputation_id";
	public static final String KEYREPUTATION_SENTIMENT = "reputation_sentiment";
	
	public static final String KEYHOT_ROWID = "hot_id";
	public static final String KEYHOT_TIME = "hot_time";
	public static final String KEYHOT_SOURCE = "hot_source";
	public static final String KEYHOT_HEADLINE = "hot_headline";
	public static final String KEYHOT_LINK = "hot_link";
	
	
	
	
	private static final String DATBASE_NAME = "syncDb";
	private static final String TABLE_TYNEWS = "TbYourNews";
	private static final String TABLE_REPUTATION = "TbReputation";
	private static final String TABLE_HOT_TOPICS = "TbHotTopics";
	private static final int DATABASE_VERSION = 1;

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
					+ " TEXT NOT NULL);");
			
			db.execSQL("CREATE TABLE " + TABLE_HOT_TOPICS + " (" + KEYHOT_ROWID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEYHOT_TIME
					+ " TEXT NOT NULL, " + KEYHOT_SOURCE + " TEXT NOT NULL, "
					+ KEYHOT_HEADLINE + " TEXT NOT NULL, " + KEYHOT_LINK
					+ " TEXT NOT NULL);");
			
			db.execSQL("CREATE TABLE " + TABLE_REPUTATION + " (" + KEYREPUTATION_ROWID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEYREPUTATION_USER
					+ " TEXT NOT NULL, " + KEYREPUTATION_TIME + " TEXT NOT NULL, "
					+ KEYREPUTATION_PLATFORM + " TEXT NOT NULL, " + KEYREPUTATION_SENTIMENT
					+ " TEXT NOT NULL);");

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

	public long createHotEntry(String name, String sname, String town, String item) {
		ContentValues cv = new ContentValues();
		cv.put(KEY_NAME, name);
		cv.put(KEY_SNAME, sname);
		cv.put(KEY_TOWN, town);
		cv.put(KEY_ITEM, item);

		return ourDb.insert(TABLE_NAME, null, cv);

	}
	
	public long createRepEntry(String name, String sname, String town, String item) {
		ContentValues cv = new ContentValues();
		cv.put(KEY_NAME, name);
		cv.put(KEY_SNAME, sname);
		cv.put(KEY_TOWN, town);
		cv.put(KEY_ITEM, item);

		return ourDb.insert(TABLE_NAME, null, cv);

	}
	public long createYNEWEntry(String name, String sname, String town, String item) {
		ContentValues cv = new ContentValues();
		cv.put(KEY_NAME, name);
		cv.put(KEY_SNAME, sname);
		cv.put(KEY_TOWN, town);
		cv.put(KEY_ITEM, item);

		return ourDb.insert(TABLE_NAME, null, cv);

	}

	public String getData() {
		// read from Db
		String[] columns = new String[] { KEY_ROWID, KEY_NAME, KEY_SNAME,
				KEY_TOWN, KEY_ITEM };
		Cursor c = ourDb.query(TABLE_NAME, columns, null, null, null, null,
				null);
		String res = "";
		int iRow = c.getColumnIndex(KEY_ROWID);
		int iName = c.getColumnIndex(KEY_NAME);
		int iSname = c.getColumnIndex(KEY_SNAME);
		int iTown = c.getColumnIndex(KEY_TOWN);
		int iItem = c.getColumnIndex(KEY_ITEM);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			res = res + c.getString(iRow) + "\n" + c.getString(iName) + " "
					+ c.getString(iSname) + "\n" + c.getString(iTown) + "\n"
					+ c.getString(iItem) + "\n";
		}

		return res;
	}

	public String getName(long m) {
		// query db with constraint
		String[] columns = new String[] { KEY_ROWID, KEY_NAME };
		Cursor c = ourDb.query(TABLE_NAME, columns, KEY_ROWID + "=" + m, null,
				null, null, null);

		String nameR = null;
		if (c != null) {
			c.moveToFirst();
			nameR = c.getString(1);
		}

		return nameR;
	}

	// this class handles posting to the database
	// should probably be separate
	void sync(String row, String fname, String sname, String town, String item) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(
				"http://zacckos.heliohost.org/syncer/poster.php");

		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
			nameValuePairs.add(new BasicNameValuePair("id", row));
			nameValuePairs.add(new BasicNameValuePair("f_name", fname));
			nameValuePairs.add(new BasicNameValuePair("s_name", sname));
			nameValuePairs.add(new BasicNameValuePair("town", town));
			nameValuePairs.add(new BasicNameValuePair("item", item));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);
			Log.v("resp", response.toString());

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
	}

	/*
	 * this method chooses the latest entry from the online database then
	 * selects then selects the later entries that this and posts them to the
	 * remote db
	 */
	public void startSync() {
		// read from Db
		int no_rows = 0;

		HttpClient httpclienty = new DefaultHttpClient();
		HttpPost httpposty = new HttpPost(
				"http://zacckos.heliohost.org/syncer/rows.php");
		try {
			HttpResponse responsey = httpclienty.execute(httpposty);
			String result = EntityUtils.toString(responsey.getEntity());
			JSONObject jo = new JSONObject(result);
			if(jo.toString().equals("{\"rows\":null}")){
				Log.v("obj", "is null");
			}
			else
			{
				JSONArray ja = jo.getJSONArray("rows");
				Log.v("lenght", ja.toString());

				JSONObject cobj = ja.getJSONObject(0);
				Log.v("c", cobj.toString());
				no_rows = cobj.getInt("row");
			}
			

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}

		String[] columns = new String[] { KEY_ROWID, KEY_NAME, KEY_SNAME,
				KEY_TOWN, KEY_ITEM };
		final Cursor c = ourDb.query(TABLE_NAME, columns, KEY_ROWID + ">"
				+ no_rows, null, null, null, null);

		final int iRow = c.getColumnIndex(KEY_ROWID);
		final int iName = c.getColumnIndex(KEY_NAME);
		final int iSname = c.getColumnIndex(KEY_SNAME);
		final int iTown = c.getColumnIndex(KEY_TOWN);
		final int iItem = c.getColumnIndex(KEY_ITEM);

		class sender extends AsyncTask<Void, Void, Void> {

			@Override
			protected Void doInBackground(Void... params) {
				for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
					sync(c.getString(iRow), c.getString(iName),
							c.getString(iSname), c.getString(iTown),
							c.getString(iItem));

				}

				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				close();
				
			}

		}
		sender s = new sender();
		s.execute();
	}

}

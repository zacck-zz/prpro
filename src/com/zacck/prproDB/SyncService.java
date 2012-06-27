package com.zacck.prproDB;

import java.io.InputStream;
import java.net.URI;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.zacck.prproBaseApp.RepMon;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

public class SyncService extends Service {
	String tuju, raila, uhuru;

	

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		collector syncColl = new collector();
		syncColl.execute();
	}

	
	private class collector extends AsyncTask<Void, Void, Void> {
		
		protected void onPreExecute() 
		{
			
			
			tuju =  "https://ajax.googleapis.com/ajax/services/search/news?v=1.0&q=raphael+tuju&rsz=8&h1=en&ned=en_ke&scoring=d";
			raila = "https://ajax.googleapis.com/ajax/services/search/news?v=1.0&&rsz=8&h1=en&topic=h&ned=en_ke&scoring=d&q=Raila%20odinga";
			uhuru = "https://ajax.googleapis.com/ajax/services/search/news?v=1.0&q=Uhuru%20kenyatta&rsz=8&h1=en&ned=en_ke&scoring=d";
			
		}
		
		

		@Override
		protected Void doInBackground(Void... params) {
			for(int n=1; n>0; n++)
			{
				ynewsLoader(tuju, "y");
				ynewsLoader(raila, "c1");
				ynewsLoader(uhuru, "c2");
				try{
					Thread.sleep(18000000);
				}
				catch (Exception e) {
					Log.v("threading exception", e.toString());	
				}
				
			}

			return null;
		}

	}

	public void ynewsLoader(String link, String flag) {
		JSONObject commentAR;
		//
		try {
			HttpClient getComment = new DefaultHttpClient();
			HttpGet pullComm = new HttpGet();
			pullComm.setURI(new URI(link));
			HttpResponse comments = getComment.execute(pullComm);
			if (comments.getStatusLine().getStatusCode() == 200) {
				HttpEntity entit = comments.getEntity();
				if (entit != null) {
					commentAR = new JSONObject(EntityUtils.toString(comments.getEntity()));
					if (commentAR.length() == 0) {
						Log.v("Empty!", "commentarr is empty");

					} else {

						SqlHandler newsSql = new SqlHandler(
								getApplicationContext());
						newsSql.open();
						for (int i = 0; i < commentAR
								.getJSONObject("responseData")
								.getJSONArray("results").length(); i++) {
							JSONObject OComm = commentAR
									.getJSONObject("responseData")
									.getJSONArray("results").getJSONObject(i);
							newsSql.createYNEWEntry(
									OComm.getString("publishedDate"),
									"Source: " + OComm.getString("publisher"),
									OComm.getString("titleNoFormatting"),
									OComm.getString("unescapedUrl"), flag);

						}
						newsSql.close();

					}

				}

			}

		} catch (Exception e) {
			Log.v("Exception", e.toString());

		} finally {

		}

	}

	public void repLoader()
	{
		Date d = new Date();
		String s = d.getTime()+"";
		JSONObject commentAR;
		String[] keywords, users = null;
		try {
			HttpClient getComment = new DefaultHttpClient();
			HttpGet pullComm = new HttpGet();
			pullComm.setURI(new URI(
					"http://api2.socialmention.com/search?q=raphael%20tuju&f=json&t[]=all&from_ts=86400&lang=en&sentiment=true&strict=true&meta=top"));
			HttpResponse comments = getComment.execute(pullComm);
			if (comments.getStatusLine().getStatusCode() == 200) {
				HttpEntity entit = comments.getEntity();
				if (entit != null) {
					
					commentAR = new JSONObject(EntityUtils.toString(comments.getEntity()));
					Log.v("len", commentAR.length() + " ");
					if (commentAR.length() == 0) {
						Log.v("log", "you told me not to go there");

					} else {
						
						JSONArray p = commentAR.getJSONObject("top_users")
								.names();
						users= new String[5];
						for (int m = 0; m < 5; m++) {
							users[m] = p.getString(m);
							Log.v("users", users[m]);
						}
						keywords = new String[5];
						JSONArray k = commentAR.getJSONObject(
								"top_keywords").names();
						for (int n = 0; n < 5; n++) {
							keywords[n] = k.getString(n);
						}
						

						SqlHandler sql = new SqlHandler(SyncService.this);
						sql.open();
						for(int n =0; n<5; n++)
						{
							sql.createkeysEntry(keywords[n], users[n], s);
							
						}
		
						sql.close();

					}

				}

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}
	}
	
}

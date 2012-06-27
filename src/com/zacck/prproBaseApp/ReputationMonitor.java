package com.zacck.prproBaseApp;

import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.TabActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class ReputationMonitor extends TabActivity {
	String TAG = "REPUTATIONMONITOR";
	
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rep_list);
		
		reputer rep = new reputer();
		rep.execute();
	}

	public class reputer extends AsyncTask<Void, Void, Void> {
		JSONObject itemsObject;

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				HttpClient itemsClient = new DefaultHttpClient();
				HttpGet itemsGetter = new HttpGet();
				itemsGetter
						.setURI(new URI(
								"http://api2.socialmention.com/search?q=tbwa&f=json&t[]=all&from_ts=86400&lang=en&sentiment=true&strict=true&meta=top"));
				HttpResponse gotten = itemsClient.execute(itemsGetter);
				String s = EntityUtils.toString(gotten.getEntity());
				Log.v(TAG, gotten.getStatusLine().getStatusCode()+"\n"+s);

			} catch (Exception e) {
				Log.v(TAG, e.toString());

			}

			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Toast.makeText(ReputationMonitor.this, "We are done fetching", Toast.LENGTH_LONG).show();
		}
	}

}

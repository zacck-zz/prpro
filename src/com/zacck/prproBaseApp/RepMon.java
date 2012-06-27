package com.zacck.prproBaseApp;

import java.net.URI;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.widget.TabHost;
import android.widget.TextView;

public class RepMon extends TabActivity {

	public String TAG = "REPMON";

	public String names[];
	public String commsFromDb[];
	public String times[];
	public String links[];
	TextView tvp, tvn;
	TabHost tabHost;
	public String users[];
	public String keywords[];

	private void tabinit() {

		Log.v(TAG, "Running the tabs ");
		// deal with the tabs
		Resources res = getResources(); // Resource object to get Drawables
		tabHost = getTabHost(); // The activity TabHost
		TabHost.TabSpec spec; // Resusable TabSpec for each tab
		Intent intent; // Reusable Intent for each tab

		// Create an Intent to launch an Activity for the tab (to be reused)
		intent = new Intent().setClass(this, users.class);

		intent.putExtra("users", users);

		// Initialize a TabSpec for each tab and add it to the TabHost
		spec = tabHost
				.newTabSpec("all")
				.setIndicator("Top Users",
						res.getDrawable(R.drawable.ic_launcher))
				.setContent(intent);
		tabHost.addTab(spec);

		// Do the same for the other tabs
		intent = new Intent().setClass(this, keywords.class);
		intent.putExtra("keys", keywords);

		spec = tabHost
				.newTabSpec("resp")
				.setIndicator("Top KeyWords",
						res.getDrawable(R.drawable.ic_launcher))
				.setContent(intent);
		tabHost.addTab(spec);

		tabHost.setCurrentTab(0);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rep_list);

		tvp = (TextView) findViewById(R.id.sentimentsp);
		tvn = (TextView) findViewById(R.id.sentimentsn);

		dataGetter dg = new dataGetter();
		dg.execute();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inf = getMenuInflater();
		inf.inflate(R.menu.mentions_menu, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.negMens:
			break;
		case R.id.posMens:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	final class dataGetter extends AsyncTask<Void, Integer, Void> {

		JSONObject commentAR;
		ProgressDialog pd;
		boolean allsWell;

		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(RepMon.this);
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setMessage("Analysing Sentiments");
			pd.setCancelable(true);
			pd.show();
			Log.v(TAG, "prexecuted");
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			Log.v(TAG, "do in background");
			
			try {
				HttpClient getComment = new DefaultHttpClient();
				HttpGet pullComm = new HttpGet();
				//"http://api2.socialmention.com/search?q=tbwa&f=json&t[]=all&from_ts=86400&lang=en&sentiment=true&strict=true&meta=top"
				
				pullComm.setURI(new URI("http://semasoftltd.com/dwebservices/fixer.php"));
				HttpResponse comments = getComment.execute(pullComm);
				commentAR = new JSONObject(EntityUtils.toString(comments.getEntity()));
				Log.v(TAG, commentAR.toString());

				
				if (commentAR.length() == 0) {
					Log.v(TAG, "you told me not to go there");

				} else {
					names = new String[commentAR.getJSONArray("items").length()];
					commsFromDb = new String[commentAR.getJSONArray("items")
							.length()];
					times = new String[commentAR.getJSONArray("items").length()];
					links = new String[commentAR.getJSONArray("items").length()];
					users = new String[commentAR.getJSONObject("top_users")
							.length()];
					 JSONArray p =
					 commentAR.getJSONObject("top_users").names();
					 Log.v(TAG, p.toString());
					 for (int m = 0; m < p.length(); m++) {
					 users[m] = p.getString(m);
					 Log.v(TAG, users[m]);
					 }
					 keywords = new String[commentAR.getJSONObject(
					 "top_keywords").length()];
					 JSONArray k = commentAR.getJSONObject("top_keywords")
					 .names();
					 Log.v(TAG, k.toString());
					 for (int n = 0; n < k.length(); n++) {
					 keywords[n] = k.getString(n);
					 }

					for (int i = 0; i < commentAR.getJSONArray("items")
							.length(); i++) {
						JSONObject OComm = commentAR.getJSONArray("items")
								.getJSONObject(i);
						names[i] = OComm.getString("source");
						commsFromDb[i] = OComm.getString("title");
						times[i] = OComm.getString("sentiment");
						Log.v(TAG, times[i]);
						links[i] = OComm.getString("link");
						Log.v(TAG, links[i]);

					}

				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.v(TAG, e.toString());
			} finally {

			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {

			// pd.setProgress(values[0]);

		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Log.v(TAG, "in post execute");

			ArrayList<HashMap<String, String>> commList = new ArrayList<HashMap<String, String>>();

			try {

				for (int j = 0; j < links.length; j++) {
					HashMap<String, String> objComm = new HashMap<String, String>();

					objComm.put("name", names[j]);
					Log.v("coms", names[j]);
					objComm.put("suggestion", commsFromDb[j]);
					Log.v("coms", commsFromDb[j]);
					objComm.put("time", "Sentiment: " + times[j]);
					Log.v("coms", names[j]);
					commList.add(objComm);
					tvp.setText("Positive Mentions " + findPN(times)[1]);
					tvn.setText("Negative Mentions " + findPN(times)[0]);
					Log.v(TAG, "text set!");

				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.v(TAG, e.toString());
			}
			pd.dismiss();
			

			tabinit();

		}

	}

	public int av(String[] s) {
		int n = 0;
		for (int i = 0; i < s.length; i++) {
			n += Integer.parseInt(s[i]);

		}
		return n / s.length;

	}

	public String[] findPN(String[] t) {
		int positive = 0;
		int negative = 0;
		String[] res;
		for (int m = 0; m < t.length; m++) {
			if (t[m].startsWith("-")) {
				negative++;
			} else {
				positive++;
			}
			Log.v(TAG, "sorted");

		}
		res = new String[] { negative + "", positive + "" };
		return res;

	}

}

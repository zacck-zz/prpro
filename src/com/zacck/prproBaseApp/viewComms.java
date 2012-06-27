package com.zacck.prproBaseApp;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class viewComms extends ListActivity {

	public String names[];
	public String commsFromDb[];
	public String times[];
	public String links[];
	public String TAG = "DEBUGGING";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment_list);

		dataGetter dg = new dataGetter();
		dg.execute();

	}

	final class dataGetter extends AsyncTask<Void, Integer, Void> {

		JSONObject commentAR;
		ProgressDialog pd;
		boolean allsWell;

		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(viewComms.this);
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setMessage("Loading Posts");
			pd.setCancelable(true);
			pd.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			// create http client to gather comments
			try {
				HttpClient getComment = new DefaultHttpClient();
				HttpParams httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
				HttpConnectionParams.setSoTimeout(httpParams, 10000);
				HttpGet pullComm = new HttpGet();

				pullComm.setURI(new URI(
						"http://semasoftltd.com/dwebservices/fixer.php"));

				HttpResponse comments = getComment.execute(pullComm);
				Log.v(TAG, comments.getStatusLine().getStatusCode() + "");
				commentAR = new JSONObject(EntityUtils.toString(comments
						.getEntity()));

				if (commentAR.length() == 0) {

				} else {
					names = new String[commentAR.getJSONArray("items").length()];
					commsFromDb = new String[commentAR.getJSONArray("items")
							.length()];
					times = new String[commentAR.getJSONArray("items").length()];
					links = new String[commentAR.getJSONArray("items").length()];
					Log.v("resp", commentAR.getJSONArray("items").toString());

					for (int i = 0; i < commentAR.getJSONArray("items")
							.length(); i++) {
						JSONObject OComm = commentAR.getJSONArray("items")
								.getJSONObject(i);
						names[i] = OComm.getString("user") + " Source: "
								+ OComm.getString("source");
						commsFromDb[i] = OComm.getString("title");
						times[i] = conv(OComm.getString("timestamp"));
						links[i] = OComm.getString("link");
						Log.v(TAG, links[i]);
						publishProgress(i);

					}

				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {

			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {

			super.onProgressUpdate(values);

			pd.setProgress(values[0]);

		}

		@Override
		protected void onPostExecute(Void result) {

			// if (allsWell) {

			super.onPostExecute(result);
			ArrayList<HashMap<String, String>> commList = new ArrayList<HashMap<String, String>>();

			try {
				for (int j = 0; j < names.length; j++) {
					HashMap<String, String> objComm = new HashMap<String, String>();
					objComm.put("name", names[j]);
					Log.v("coms", names[j]);
					objComm.put("suggestion", commsFromDb[j]);
					Log.v("coms", commsFromDb[j]);
					objComm.put("time", times[j]);
					Log.v("coms", names[j]);
					commList.add(objComm);

				}
			} catch (Exception e) {

				e.printStackTrace();
				Log.v(TAG, e.toString());
			}

			SimpleAdapter adapter = new SimpleAdapter(viewComms.this, commList,
					R.layout.comment_row, new String[] { "name", "suggestion",
							"time" }, new int[] { R.id.CommName, R.id.CommText,
							R.id.CommTime });

			setListAdapter(adapter);
			Log.v(TAG, "adapter set!");

			ListView lv = getListView();
			lv.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg,
						int pos, long arg3) {
					Intent fullView = new Intent(viewComms.this, FullComm.class);
					fullView.putExtra("title", names[pos]);
					fullView.putExtra("time", times[pos]);
					fullView.putExtra("comm", commsFromDb[pos]);
					startActivity(fullView);

					return true;

				}
			});
			pd.dismiss();
			// } else {
			// pd.dismiss();
			//
			// Toast.makeText(
			// viewComms.this,
			// "It Appears Internet speeds are low or an Interruption has occured Please bear with us While we fix this",
			// Toast.LENGTH_LONG).show();
			// try {
			// Thread.sleep(2000);
			// } catch (Exception e) {
			//
			// }
			// startActivity(new Intent(viewComms.this, PRProActivity.class));
			//
			// }

		}

	}

	public String conv(String l) {
		long dl;
		dl = Long.parseLong(l);
		Date d = new Date(dl * 1000);
		return d.toGMTString();

	}

}

package com.zacck.prproBaseApp;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

 
public class HotTopics extends ListActivity {
	
	public String names[];
	public String commsFromDb[];
	public String times[];
	public String links[];
	InetAddress ownIP;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment_list);
		try {
			ownIP =InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		dataGetter dg = new dataGetter();
		dg.execute();
		
	
	}
	
	private static String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the
		 * BufferedReader.readLine() method. We iterate until the BufferedReader
		 * return null which means there's no more data to read. Each line will
		 * appended to a StringBuilder and returned as String.
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	final class dataGetter extends AsyncTask<Void, Integer, Void> {
		
		JSONObject commentAR;
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(HotTopics.this);
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setMessage("Collecting Posts");
			pd.setCancelable(true);
			pd.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			// create http client to gather comments
			try {
				HttpClient getComment = new DefaultHttpClient();
				HttpGet pullComm = new HttpGet();
				pullComm.setURI(new URI(
						"https://ajax.googleapis.com/ajax/services/search/news?v=1.0&rsz=8&h1=en&ned=en_ke&scoring=d&q=Unilever+africa"));
				HttpResponse comments = getComment.execute(pullComm);
				Log.v("resp", comments.toString());
				if (comments.getStatusLine().getStatusCode() == 200) {
					HttpEntity entit = comments.getEntity();
					if (entit != null) {
						InputStream commStream = entit.getContent();
						commentAR = new JSONObject(
								convertStreamToString(commStream));
						Log.v("len", commentAR.length() + " ");
						if (commentAR.length() == 0) {

						} else {
							names = new String[commentAR.getJSONObject("responseData").getJSONArray("results")
									.length()];
							commsFromDb = new String[commentAR.getJSONObject("responseData").getJSONArray("results")
														.length()];
							times = new String[commentAR.getJSONObject("responseData").getJSONArray("results")
												.length()];
							links = new String[commentAR.getJSONObject("responseData").getJSONArray("results")
												.length()];
							
									

							for (int i = 0; i < commentAR.getJSONObject("responseData").getJSONArray("results")
									.length(); i++) {
								JSONObject OComm = commentAR.getJSONObject("responseData").getJSONArray("results").getJSONObject(i);
								names[i] =OComm.getString("titleNoFormatting");
								commsFromDb[i] ="Source: "+ OComm.getString("publisher");
								times[i] = OComm.getString("publishedDate");
								links[i] = OComm.getString("unescapedUrl");
								Log.v("link", links[i]);
							}

						}

					}

				}

			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
			return null;
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {

			pd.setProgress(values[0]);

		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			// super.onPostExecute(result);
			ArrayList<HashMap<String, String>> commList = new ArrayList<HashMap<String, String>>();

			try {
				for (int j = 0; j < commentAR.getJSONObject("responseData").getJSONArray("results")
						.length(); j++) {
					HashMap<String, String> objComm = new HashMap<String, String>();
					objComm.put("name", names[j]);
					Log.v("coms", names[j]);
					objComm.put("suggestion", commsFromDb[j]);
					Log.v("coms", commsFromDb[j]);
					objComm.put("time", times[j]);
					Log.v("coms", names[j]);
					commList.add(objComm);

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			SimpleAdapter adapter = new SimpleAdapter(HotTopics.this, commList,
					R.layout.comment_row, new String[] { "name", "suggestion",
							"time" }, new int[] { R.id.CommName, R.id.CommText,
							R.id.CommTime });

			setListAdapter(adapter);

			ListView lv = getListView();
			lv.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg,
						int pos, long arg3) {

					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(links[pos]));
					startActivity(i);

					return true;

				}
			});
			
			pd.dismiss();

		}

	}

	
	

}

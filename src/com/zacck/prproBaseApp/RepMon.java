package com.zacck.prproBaseApp;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

 
public class RepMon extends ListActivity {
	
	public String names[];
	public String commsFromDb[];
	public String times[];
	public String links[];


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment_list);
		
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
			pd = new ProgressDialog(RepMon.this);
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setMessage("Collecting Posts");
			pd.setCancelable(false);
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
						"http://api2.socialmention.com/search?q=tbwa&f=json&t[]=all&from_ts=86400&lang=en&sentiment=true&strict=true"));
				HttpResponse comments = getComment.execute(pullComm);
				if (comments.getStatusLine().getStatusCode() == 200) {
					HttpEntity entit = comments.getEntity();
					if (entit != null) {
						InputStream commStream = entit.getContent();
						commentAR = new JSONObject(
								convertStreamToString(commStream));
						Log.v("len", commentAR.length() + " ");
						if (commentAR.length() == 0) {

						} else {
							names = new String[commentAR.getJSONArray("items")
									.length()];
							commsFromDb = new String[commentAR.getJSONArray(
									"items").length()];
							times = new String[commentAR.getJSONArray("items")
									.length()];
							links = new String[commentAR.getJSONArray("items")
									.length()];
							Log.v("resp", commentAR.getJSONArray("items")
									.toString());

							for (int i = 0; i < commentAR.getJSONArray("items")
									.length(); i++) {
								JSONObject OComm = commentAR.getJSONArray(
										"items").getJSONObject(i);
								names[i] =OComm.getString("source");
								commsFromDb[i] = OComm.getString("title");
								times[i] = 	OComm.getString("sentiment");
								links[i] = OComm.getString("link");
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
				for (int j = 0; j < commentAR.getJSONArray("items").length(); j++) {
					HashMap<String, String> objComm = new HashMap<String, String>();
					objComm.put("name", names[j]);
					Log.v("coms", names[j]);
					objComm.put("suggestion", commsFromDb[j]);
					Log.v("coms", commsFromDb[j]);
					objComm.put("time", "Sentiment: "+times[j]);
					Log.v("coms", names[j]);
					commList.add(objComm);

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			SimpleAdapter adapter = new SimpleAdapter(RepMon.this, commList,
					R.layout.comment_row, new String[] { "name", "suggestion",
							"time" }, new int[] { R.id.CommName, R.id.CommText,
							R.id.CommTime });

			setListAdapter(adapter);
			Window w = getWindow();
			w.setTitle("The average sentiment is "+av(times));

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
	
	public int av(String[] s)
	{
		int n=0;
		for(int i=0; i<s.length; i++)
		{
			n += Integer.parseInt(s[i]);
					
		}
		return n/s.length;
		
	}

	
	

}

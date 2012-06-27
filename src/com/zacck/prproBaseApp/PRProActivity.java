package com.zacck.prproBaseApp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
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

import android.R.array;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class PRProActivity extends Activity implements OnClickListener {
	/** Called when the activity is first created. */
	Button infButton, mInButton, btBreakiButton, btRepMon, traciButton, BtHot;
	SharedPreferences myPrefs;
	String name, c1, c2, phrase;
	boolean Runy;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		init();

//		if (myPrefs.contains("hasrun")) {
//
//		} else {
//			alert = new AlertDialog.Builder(this);
//
//			alert.setTitle("Settings");
//			alert.setMessage("input your passphrase");
//
//			// Set an EditText view to get user input
//			final EditText namey = new EditText(this);
//			namey.setHint("passphrase");
//
//			final LinearLayout input = new LinearLayout(this);
//			input.setOrientation(LinearLayout.VERTICAL);
//			input.addView(namey);
//
//			alert.setView(input);
//
//			alert.setPositiveButton("Ok",
//					new DialogInterface.OnClickListener() {
//						public void onClick(DialogInterface dialog,
//								int whichButton) {
//
//							phrase = namey.getText().toString();
//
//							firstRun();
//
//						}
//					});
//
//			alert.setNegativeButton("Cancel",
//					new DialogInterface.OnClickListener() {
//						public void onClick(DialogInterface dialog,
//								int whichButton) {
//							// Canceled.
//						}
//					});
//
//			alert.show();
//
//		}

	}

	private void init() {

		// initialize preferences

		myPrefs = this.getSharedPreferences("myPrefs", MODE_WORLD_READABLE);

		infButton = (Button) findViewById(R.id.btInTra);

		infButton.setOnClickListener(this);

		btBreakiButton = (Button) findViewById(R.id.btBnews);
		btBreakiButton.setOnClickListener(this);
		btRepMon = (Button) findViewById(R.id.btRepMon);
		btRepMon.setOnClickListener(this);

		BtHot = (Button) findViewById(R.id.btHtop);
		BtHot.setOnClickListener(this);

	}

	private void firstRun() {
		// initialize preferences

		Runy = true;

		SharedPreferences.Editor prefsEditor = myPrefs.edit();
		prefsEditor.putBoolean("hasrun", Runy);
		postData(phrase);
		prefsEditor.putString("n", name);
		prefsEditor.putString("comp1", c1);
		prefsEditor.putString("comp2", c2);
		
		Toast.makeText(PRProActivity.this, "Your name is "+name+" your competitors are "+c1+" "+c2, Toast.LENGTH_LONG).show();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btInTra:
			startActivity(new Intent(PRProActivity.this, viewComms.class));

			break;

		case R.id.btBnews:
			startActivity(new Intent(PRProActivity.this, Categories.class));
			break;

		case R.id.btRepMon:
			startActivity(new Intent(PRProActivity.this, RepMon.class));
			break;

		case R.id.btHtop:
			startActivity(new Intent(PRProActivity.this, HotyTopics.class));
			break;

		}

	}

	public void postData(String p) {
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(
				"http://www.webaraza.com/webaraza2/collector.php");

		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("p", p));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);

			JSONObject parentobJsonObject = new JSONObject(
					EntityUtils.toString(response.getEntity()));
			JSONArray childarr = parentobJsonObject.getJSONArray("dety");
			JSONObject childobj = childarr.getJSONObject(0).getJSONObject(
					"post");
			name = childobj.getString("name");
			c1 = childobj.getString("c1");
			c2 = childobj.getString("c2");

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
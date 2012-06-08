package com.zacck.prproBaseApp;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import android.widget.TabHost;

public class Categories extends TabActivity {

	TabHost tabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.categories);

		tabinit();

	}

	private void tabinit() {

		Log.v("tabby", "Running the tabs ");
		// deal with the tabs
		Resources res = getResources(); // Resource object to get Drawables
		tabHost = getTabHost(); // The activity TabHost
		TabHost.TabSpec spec; // Resusable TabSpec for each tab
		Intent intent; // Reusable Intent for each tab

		// Create an Intent to launch an Activity for the tab (to be reused)
		intent = new Intent().setClass(this, AndroidXMLParsingActivity.class);

		// Initialize a TabSpec for each tab and add it to the TabHost
		spec = tabHost
				.newTabSpec("all")
				.setIndicator("Safaricom")
				.setContent(intent);
		tabHost.addTab(spec);

		// Do the same for the other tabs
		intent = new Intent().setClass(this, Breaking_News.class);

		spec = tabHost
				.newTabSpec("SCANAD")
				.setIndicator("Airtel")
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, HotTopics.class);

		spec = tabHost
				.newTabSpec("WPP")
				.setIndicator("Orange")
				.setContent(intent);
		tabHost.addTab(spec);

		tabHost.setCurrentTab(0);

	}

}

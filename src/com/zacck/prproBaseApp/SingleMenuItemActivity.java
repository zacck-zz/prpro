package com.zacck.prproBaseApp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

public class SingleMenuItemActivity  extends Activity {
	
	// XML node keys
	static final String KEY_NAME = "title";
	static final String KEY_COST = "link";
	static final String KEY_DESC = "pubDate";
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_list_item);
        
        // getting intent data
        Intent in = getIntent();
        
        WebView w = (WebView)findViewById(R.id.webby);
        
        // Get XML values from previous intent
        String name = in.getStringExtra(KEY_NAME);
        Log.v("name", name);
        String cost = in.getStringExtra(KEY_COST);
        Log.v("cost", cost);
        String description = in.getStringExtra(KEY_DESC);
        Log.v("desc", description);
        
        // Displaying all values on the screen
        TextView lblName = (TextView) findViewById(R.id.name_label);
        TextView lblCost = (TextView) findViewById(R.id.cost_label);
        TextView lblDesc = (TextView) findViewById(R.id.description_label);
        
        lblName.setText(name);
        lblCost.setText(cost);
        //lblDesc.setText(description);
        w.loadUrl(description);
    }
}

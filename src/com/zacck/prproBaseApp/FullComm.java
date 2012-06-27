package com.zacck.prproBaseApp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class FullComm extends Activity {
	
	TextView tvTitle, tvTime, tvSource;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fullcomm);
		
		init();
		
		Bundle n = getIntent().getExtras();
		tvTitle.setText(n.getString("comm"));
		tvTime.setText(n.getString("time"));
		tvSource.setText(n.getString("title"));
		
		
		
		
	}

	private void init() {
		tvTitle = (TextView)findViewById(R.id.tvfTitle);
		tvTime = (TextView)findViewById(R.id.tvfTime);
		tvSource = (TextView)findViewById(R.id.tvfSource);
	}
	

}

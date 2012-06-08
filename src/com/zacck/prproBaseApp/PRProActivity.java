package com.zacck.prproBaseApp;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class PRProActivity extends Activity implements OnClickListener {
	/** Called when the activity is first created. */
	Button infButton, mInButton, btBreakiButton, btRepMon, traciButton, BtHot;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		init();

	}

	private void init() {
		infButton = (Button) findViewById(R.id.btInTra);

		infButton.setOnClickListener(this);
		
		btBreakiButton = (Button)findViewById(R.id.btBnews);
		btBreakiButton.setOnClickListener(this);
		btRepMon = (Button)findViewById(R.id.btRepMon);
		btRepMon.setOnClickListener(this);
		
		BtHot = (Button)findViewById(R.id.btHtop);
		BtHot.setOnClickListener(this);
		

	}

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.btInTra:
			startActivity(new Intent(PRProActivity.this, viewComms.class));
			
			
			break;
		
		case R.id.btBnews:
			startActivity(new Intent(PRProActivity.this, Categories.class));
			break;
			
		case R.id.btRepMon:
			startActivity(new Intent(PRProActivity.this, AverageTemperatureChart.class));
			break;
			
		
			
		case R.id.btHtop:
			startActivity(new Intent(PRProActivity.this, HotyTopics.class));
			break;
			
			
			
			
			
		}
		
	}
}
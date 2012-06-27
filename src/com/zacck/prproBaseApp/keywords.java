package com.zacck.prproBaseApp;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class keywords extends ListActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_list);
		Bundle n = getIntent().getExtras();
		
		if(n!=null){
			 setListAdapter(new ArrayAdapter<String>(this, R.layout.iteml, n.getStringArray("keys")));
			
		}
		else
		{
			Toast.makeText(keywords.this, "No users posted about you today", Toast.LENGTH_LONG).show();
		}
		
		

		 
	}

}

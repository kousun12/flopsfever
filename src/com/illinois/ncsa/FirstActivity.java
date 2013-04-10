package com.illinois.ncsa;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class FirstActivity extends Activity {
	String user;
	SharedPreferences sp;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("gameScore", 0);
		user = sp.getString("user","");
	
	if(user.equalsIgnoreCase("")){
		
		setContentView(R.layout.main);
		
		findViewById(R.id.btnSave).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				EditText name = (EditText)findViewById(R.id.name);
				String username = name.getText().toString();
				if(!username.equalsIgnoreCase("")){
					Editor e = sp.edit();
					long id = (long)(Math.random()*1000000000);
					e.putString("user", username);
					e.putLong("id", id);
					e.commit();
					Intent i = new Intent(FirstActivity.this,FlopsFeverActivity.class);
					startActivity(i);
					finish();
				}
				else{
					Toast.makeText(getApplicationContext(), "Please Enter Your Name", Toast.LENGTH_LONG).show();
				}
			}
			
			
		});
		
	}
	else{
		Intent i = new Intent(FirstActivity.this,FlopsFeverActivity.class);
		startActivity(i);
		finish();
	}
	}


}

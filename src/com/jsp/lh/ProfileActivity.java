package com.jsp.lh;

import entities.UserEntity;
import DBLayout.UserProfile;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public class ProfileActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}
	
	public void gotoHomeScreen(View v)
	{
		try{
		EditText fn = (EditText)findViewById(R.id.fname);
		EditText ln = (EditText)findViewById(R.id.lname);
		EditText age = (EditText)findViewById(R.id.ageText);
		EditText ft = (EditText)findViewById(R.id.feetText);
		EditText inch = (EditText)findViewById(R.id.inchText);
		EditText weight = (EditText)findViewById(R.id.weightText);
		
		String fname = fn.getText().toString();
		String lname = ln.getText().toString();
		String agetext = age.getText().toString();
		String feet = ft.getText().toString();
		String inchtext = inch.getText().toString();
		String wttext = weight.getText().toString();
		
		if (fname.matches("") || lname.matches("") || 
				agetext.matches("") || feet.matches("") || inchtext.matches("") ||
				wttext.matches(""))
		{
			Toast.makeText(getApplicationContext(), "Kindly check input entered", Toast.LENGTH_LONG).show();			
		}
		else
		{
			int agereq = Integer.parseInt(agetext);
			int feetreq = Integer.parseInt(feet);
			int inchreq = Integer.parseInt(inchtext);
			int weightreq = Integer.parseInt(wttext);
			
			UserEntity ue = new UserEntity(fname, lname, agereq, feetreq, inchreq, weightreq);
			
			UserProfile con = new UserProfile(ProfileActivity.this);
			
			con.open();
			
			UserEntity ret = con.getOneUser(1);
			con.close();
			
			if (ret == null)
			{
				con.insertUser(ue);
				Toast.makeText(getApplicationContext(), "Entered New user", Toast.LENGTH_LONG).show();
			}
			else
			{
				con.updateUser(1, ue);
				Toast.makeText(getApplicationContext(), "Updated old user", Toast.LENGTH_LONG).show();
			}
		}
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), "Kindly check input entered", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		EditText fn = (EditText)findViewById(R.id.fname);
		EditText ln = (EditText)findViewById(R.id.lname);
		EditText age = (EditText)findViewById(R.id.ageText);
		EditText ft = (EditText)findViewById(R.id.feetText);
		EditText inch = (EditText)findViewById(R.id.inchText);
		EditText weight = (EditText)findViewById(R.id.weightText);
		
		
		UserProfile con1 = new UserProfile(ProfileActivity.this);
		
		con1.open();
		
		UserEntity ret1 = con1.getOneUser(1);
		con1.close();
		if (ret1 != null)
		{
			fn.setText(ret1.getFname());
			ln.setText(ret1.getLname());
			age.setText(String.valueOf(ret1.getAge()));
			ft.setText(String.valueOf(ret1.getHft()));
			inch.setText(String.valueOf(ret1.getHinch()));
			weight.setText(String.valueOf(ret1.getWeight()));
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_profile,
					container, false);
			return rootView;
		}
	}

	public void submitProfile(View view) {
		Intent intent = new Intent(this, MainActivity.class);
	    startActivity(intent);
	}
}

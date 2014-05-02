package com.jsp.lh;

import entities.FoodEntity;
import entities.FoodRecordEntity;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import DBLayout.*;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		populateDatabase();
	}

	/* Function to pre-populate the Database with existing 
	 * food items.
	 */
	void populateDatabase()
	{
		MainActivity.this.deleteDatabase("FoodDetails");
		MainActivity.this.deleteDatabase("FoodRecord");
		FoodEntity fe = new FoodEntity("coca cola", 100, "04900005010");
		FoodEntity fe1 = new FoodEntity("pepsi", 200, "8906001055440");
		FoodDatabase connector = new FoodDatabase(MainActivity.this);
		
		connector.insertFood(fe);
		connector.insertFood(fe1);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
	
	public void recordFoodIntake(View view) {
		Intent intent = new Intent(this, RecordFoodActivity.class);
	    startActivity(intent);
	}

	public void recordExercise(View view) {
		Intent intent = new Intent(this, RExerciseActivity.class);
	    startActivity(intent);
	}
	
	public void viewFoodDatabase(View view) {
		Intent intent = new Intent(this, FoodDetailsActivity.class);
	    startActivity(intent);
	}
	
	public void viewReports(View view) {
		Intent intent = new Intent(this, ReportsHomeActivity.class);
	    startActivity(intent);
	}
	
	public void editProfile(View view) {
		Intent intent = new Intent(this, ProfileActivity.class);
	    startActivity(intent);
	}
	
	public void endApplication(View view) {
		FoodRecord fr = new FoodRecord(MainActivity.this);
		fr.open();
		fr.getRecordsPerDay();
		fr.close();
	}
}


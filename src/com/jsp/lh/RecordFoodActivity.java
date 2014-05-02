package com.jsp.lh;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import entities.FoodEntity;
import entities.FoodRecordEntity;
import DBLayout.FoodDatabase;
import DBLayout.FoodRecord;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.os.Build;

public class RecordFoodActivity extends Activity {

	private static final int VOICE_REQUEST_CODE = 1234;
	public static final int SCAN_REQUEST_CODE = 0x0000c0de;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_food);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
			.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.record_food, menu);
		

	 	
		EditText edit_Text = (EditText)findViewById(R.id.foodText);
		edit_Text.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
			    if(hasFocus){
			        Toast.makeText(getApplicationContext(), "got the focus", Toast.LENGTH_LONG).show();
			    }else {
			        Toast.makeText(getApplicationContext(), "lost the focus", Toast.LENGTH_LONG).show();
			        FoodEntity fe = null;
			        EditText edit_text1 = (EditText)findViewById(R.id.foodText);
			        System.out.println("Word loooking for is "+edit_text1);
			        String match = edit_text1.getText().toString();
					FoodDatabase db = new FoodDatabase(RecordFoodActivity.this);
			        db.open();
			        
					fe = db.getOneFood(match);
					TextView calt = (TextView) findViewById(R.id.calText);
					if (fe != null)
					{

						calt.setText(String.valueOf(fe.getCalories()));
					}
					else
					{
						calt.setText("Food not found");
					}
					db.close();
			    }
			   }
			});
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
	
	public void recordEntry(View v)
	{
		//try {
		EditText fname = (EditText)findViewById(R.id.foodText);
		TextView cals = (TextView)findViewById(R.id.calText);
		DatePicker dp = (DatePicker)findViewById(R.id.datePicker1);
		TimePicker tp = (TimePicker)findViewById(R.id.timePicker1);
		
		
		System.out.println("LOL Food name = "+fname.getText().toString()+" cals: "+cals.getText()+
				" date: "+dp.getYear()+"/"+(dp.getMonth()+1)+"/"+dp.getDayOfMonth()+
				" Time: "+tp.getCurrentHour()+":"+tp.getCurrentMinute());

		
		Calendar calendar = Calendar.getInstance();
		calendar.set(dp.getYear(), dp.getMonth(), dp.getDayOfMonth(), 
		             tp.getCurrentHour(), tp.getCurrentMinute(), 0);
		long startTime = calendar.getTimeInMillis();
		
		FoodEntity fe = null;
        EditText edit_text1 = (EditText)findViewById(R.id.foodText);

        String match = edit_text1.getText().toString();
        System.out.println("Word loooking for is "+match);
		FoodDatabase db = new FoodDatabase(RecordFoodActivity.this);
        db.open();
        
		fe = db.getOneFood(match);
		TextView calt = (TextView) findViewById(R.id.calText);
		if (fe != null)
		{
			System.out.println("Creating new fre "+fe.getName()+" time:"+startTime);
			FoodRecordEntity fre = new FoodRecordEntity(fe.getName(), startTime, fe.getCalories());
			FoodRecord frdb = new FoodRecord(RecordFoodActivity.this);
			frdb.insertRecord(fre);
		}
		else
		{
			calt.setText("Food not found");
		}
		db.close();
//		}
//		
//		catch (Exception e)
//		{
//			Toast.makeText(getApplicationContext(), "Kindly check the input entered", Toast.LENGTH_LONG).show();
//		}		
	}

	/* Callback for Voice Button Click */
	public void onSpeak(View v)
	{
		//		Button speakButton = (Button) findViewById(R.id.speakButton);
		// 
		//        // Disable button if no recognition service is present
		//        PackageManager pm = getPackageManager();
		//        List<ResolveInfo> activities = pm.queryIntentActivities(
		//                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
		//        if (activities.size() == 0)
		//        {
		//            speakButton.setEnabled(false);
		//            speakButton.setText("Recognizer not present");
		//        }

		startVoiceRecognitionActivity();
	}

	public void onScan(View v)
	{
		IntentIntegrator scanIntegrator = new IntentIntegrator(this);
		scanIntegrator.initiateScan();
	}



	/**
	 * Fire an intent to start the voice recognition activity.
	 */
	private void startVoiceRecognitionActivity()
	{
		System.out.println("Starting activity lel");
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Voice recognition Demo...");
		startActivityForResult(intent, VOICE_REQUEST_CODE);
	}

	/**
	 * Handle the results from the voice recognition activity.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		/* On Voice Result */
		if (requestCode == VOICE_REQUEST_CODE)
		{
			System.out.println("Came here");
			String matchedWord = null;
			FoodEntity fe = null;
			FoodDatabase db = new FoodDatabase(RecordFoodActivity.this);
			if (requestCode == VOICE_REQUEST_CODE && resultCode == RESULT_OK)
			{
				// Populate the wordsList with the String values the recognition engine thought it heard
				ArrayList<String> matches = data.getStringArrayListExtra(
						RecognizerIntent.EXTRA_RESULTS);
				System.out.println("Printing matched words");
				matchedWord = matches.get(0);
			}
			super.onActivityResult(requestCode, resultCode, data);

			EditText text3 = (EditText) findViewById(R.id.foodText);

			if (matchedWord != null)
			{
				text3.setText(matchedWord);
				db.open();
				fe = db.getOneFood(matchedWord);
				TextView calt = (TextView) findViewById(R.id.calText);
				if (fe != null)
				{

					calt.setText(String.valueOf(fe.getCalories()));
				}
				else
				{
					calt.setText("Food not found");
				}
				db.close();
			}
			else
			{
				text3.setText("No Match");
			}
		}

		else if (requestCode == SCAN_REQUEST_CODE)
		{
			/* On Scan Code Result */
			IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
			if (scanningResult != null) {
				//we have a result
				String scanContent = scanningResult.getContents();
				String scanFormat = scanningResult.getFormatName();
				if (scanContent == null)
				{
					System.out.println("ScanContent is null");
				}
				else
				{
					FoodDatabase db = new FoodDatabase(RecordFoodActivity.this);
					System.out.println("Results= Format:"+scanFormat+" Contents: "+scanContent);
					FoodEntity fe = null;
					db.open();
					fe = db.getOneFoodFromBarCode(scanContent);
					TextView calt = (TextView) findViewById(R.id.calText);
					EditText text3 = (EditText) findViewById(R.id.foodText);
					if (fe != null)
					{

						calt.setText(String.valueOf(fe.getCalories()));
						text3.setText(fe.getName());
					}
					else
					{
						calt.setText("Food not found");
					}
					db.close();

				}

			}
			else{
				Toast toast = Toast.makeText(getApplicationContext(), 
						"No scan data received!", Toast.LENGTH_SHORT);
				toast.show();
			}
		}
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
			View rootView = inflater.inflate(R.layout.fragment_record_food,
					container, false);
			return rootView;
		}
	}
	
}

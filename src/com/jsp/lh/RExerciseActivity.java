package com.jsp.lh;

import java.util.ArrayList;
import java.util.Calendar;

import entities.ExerciseEntity;
import entities.ExerciseRecordEntity;
import entities.FoodEntity;
import entities.FoodRecordEntity;
import DBLayout.ExerciseDatabase;
import DBLayout.ExerciseRecord;
import DBLayout.FoodDatabase;
import DBLayout.FoodRecord;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnFocusChangeListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.os.Build;

public class RExerciseActivity extends Activity {

	private static final int VOICE_REQUEST_CODE = 1234;
	private ShakeListener mShaker;
	private AutoCompleteTextView mactv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rexercise);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
			.add(R.id.container, new PlaceholderFragment()).commit();
		}
		final Vibrator vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

	    mShaker = new ShakeListener(this);
	    mShaker.setOnShakeListener(new ShakeListener.OnShakeListener () {
	      public void onShake()
	      {
	        vibe.vibrate(100);
	        Toast.makeText(getApplicationContext(), "Shooken", Toast.LENGTH_LONG).show();
	        clearFields();
	      }
	    });
		
	}
	
	
	
	public void clearFields()
	{
		EditText wtype = (EditText)findViewById(R.id.input_workout_type);
		EditText calp = (EditText)findViewById(R.id.input_calpermin);
		EditText min = (EditText)findViewById(R.id.input_workout_duration);
		EditText tot = (EditText)findViewById(R.id.total_cal_burnt);
		wtype.setText("");
		calp.setText("");
		min.setText("");
		tot.setText("");
		
	}
	
	@Override
	  public void onResume()
	  {
	    mShaker.resume();

		ExerciseDatabase fd = new ExerciseDatabase(RExerciseActivity.this);
		fd.open();
		ArrayList<String> ar = new ArrayList<String>();
		ar = fd.getAllExerciseNames();
		fd.close();

		String[] countries = new String[ar.size()];
		countries = ar.toArray(countries);
		ArrayAdapter adapter = new ArrayAdapter
				(this,android.R.layout.simple_list_item_1,countries);

		mactv = (AutoCompleteTextView) findViewById
				(R.id.input_workout_type);
		mactv.setThreshold(0);

		mactv.setAdapter(adapter);
	    super.onResume();
	  }
	  @Override
	  public void onPause()
	  {
	    mShaker.pause();
	    super.onPause();
	  }
	
	
	
	public void addWorkout(View v)
	{
		Intent intent = new Intent(this, AddWorkoutActivity.class);
	    startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		try {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.rexercise, menu);

			EditText edit_Text = (EditText)findViewById(R.id.input_workout_type);
			edit_Text.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if(hasFocus){
						//Toast.makeText(getApplicationContext(), "got the focus", Toast.LENGTH_LONG).show();
					}else {
						TextView calp = (TextView) findViewById(R.id.input_calpermin);

						//Toast.makeText(getApplicationContext(), "lost the focus", Toast.LENGTH_LONG).show();
						ExerciseEntity fe = null;
						EditText edit_text1 = (EditText)findViewById(R.id.input_workout_type);
						System.out.println("Word loooking for is "+edit_text1);
						String match = edit_text1.getText().toString();
						ExerciseDatabase db = new ExerciseDatabase(RExerciseActivity.this);
						db.open();

						fe = db.getOneExercise(match);

						if (fe != null)
						{

							calp.setText(String.valueOf(fe.getCaloriesPerMin()));
						}
						else
						{
							calp.setText("Exercise not found");
						}
						db.close();
					}
				}
			});


			EditText edit_Text1 = (EditText)findViewById(R.id.input_workout_duration);
			edit_Text1.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if(hasFocus){
						//Toast.makeText(getApplicationContext(), "got the focus", Toast.LENGTH_LONG).show();
					}else {
						EditText wtype = (EditText)findViewById(R.id.input_workout_type);
						if (wtype.getText().toString().matches(""))
							return;
						EditText calp = (EditText) findViewById(R.id.input_calpermin);

						//Toast.makeText(getApplicationContext(), "lost the focus", Toast.LENGTH_LONG).show();

						String calptext = calp.getText().toString();
						EditText wout = (EditText) findViewById(R.id.input_workout_duration);
						String wouttext = wout.getText().toString();
						int cals = 0;
						int wdur = 0;
						if (calptext.matches(""))
						{
							cals = 0;
						}
						else
						{
							cals = Integer.parseInt(calp.getText().toString());
						}

						if (wouttext.matches(""))
						{
							wdur = 0;
						}
						else
						{
							wdur = Integer.parseInt(wout.getText().toString());
						}

						EditText calt = (EditText) findViewById(R.id.total_cal_burnt);

						int tot = cals * wdur;
						calt.setText(""+tot);
					}
				}
			});
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), "Wrong Input!", Toast.LENGTH_LONG).show();
		}
		return true;
	}
	
	public void recordEntry(View v)
	{
		try {
		EditText wtype = (EditText)findViewById(R.id.input_workout_type);
		EditText calp = (EditText)findViewById(R.id.input_calpermin);
		EditText min = (EditText)findViewById(R.id.input_workout_duration);
		EditText tot = (EditText)findViewById(R.id.total_cal_burnt);
		
		DatePicker dp = (DatePicker)findViewById(R.id.datePicker2);
		TimePicker tp = (TimePicker)findViewById(R.id.timePicker2);
		
		
		System.out.println("LOL Workout type = "+wtype.getText().toString()+" cals: "+calp.getText()+
				"duration: "+min.getText()+"total: "+tot.getText()+
				" date: "+dp.getYear()+"/"+(dp.getMonth()+1)+"/"+dp.getDayOfMonth()+
				" Time: "+tp.getCurrentHour()+":"+tp.getCurrentMinute());

		
		Calendar calendar = Calendar.getInstance();
		calendar.set(dp.getYear(), dp.getMonth(), dp.getDayOfMonth(), 
		             tp.getCurrentHour(), tp.getCurrentMinute(), 0);
		long startTime = calendar.getTimeInMillis();
		
		ExerciseEntity fe = null;
        
		ExerciseDatabase db = new ExerciseDatabase(RExerciseActivity.this);
        db.open();
        
		fe = db.getOneExercise(wtype.getText().toString());
	
		if (fe != null && !(min.getText().toString().matches("")))
		{
			System.out.println("Creating new fre "+fe.getName()+" time:"+startTime);
			int dur = Integer.parseInt(min.getText().toString());
			int tcal = dur * fe.getCaloriesPerMin();
			ExerciseRecordEntity fre = new ExerciseRecordEntity(fe.getName(), dur, tcal, startTime);
			ExerciseRecord frdb = new ExerciseRecord(RExerciseActivity.this);
			frdb.insertRecord(fre);
			Toast.makeText(getApplicationContext(), "Record Inserted Successfully!", Toast.LENGTH_LONG).show();
			wtype.setText("");
			calp.setText("");
			min.setText("");
			tot.setText("");
		}
		else
		{
			Toast.makeText(getApplicationContext(), "Kindly check the input entered", Toast.LENGTH_LONG).show();
		}
		db.close();
		
		}
		
		catch (Exception e)
		{
			Toast.makeText(getApplicationContext(), "Kindly check the input entered", Toast.LENGTH_LONG).show();
		}		
	}

	/* Callback for Voice Button Click */
	public void onSpeak(View v)
	{
		startVoiceRecognitionActivity();
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
			View rootView = inflater.inflate(R.layout.fragment_rexercise,
					container, false);
			return rootView;
		}
	}

	/**
	 * Handle the results from the voice recognition activity.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		System.out.println("Came here");
		String matchedWord = null;
		ExerciseEntity fe = null;
		ExerciseDatabase db = new ExerciseDatabase(RExerciseActivity.this);
		if (requestCode == VOICE_REQUEST_CODE && resultCode == RESULT_OK)
		{
			// Populate the wordsList with the String values the recognition engine thought it heard
			ArrayList<String> matches = data.getStringArrayListExtra(
					RecognizerIntent.EXTRA_RESULTS);
			System.out.println("Printing matched words");
			matchedWord = matches.get(0);
		}
		super.onActivityResult(requestCode, resultCode, data);

		EditText text3 = (EditText) findViewById(R.id.input_workout_type);

		if (matchedWord != null)
		{
			text3.setText(matchedWord);
			db.open();
			fe = db.getOneExercise(matchedWord);
			TextView calt = (TextView) findViewById(R.id.input_calpermin);
			if (fe != null)
			{
				calt.setText(String.valueOf(fe.getCaloriesPerMin()));
			}
			else
			{
				calt.setText("Exercise not found");
			}
			db.close();
		}
		else
		{
			text3.setText("No Match");
		}


	}

}

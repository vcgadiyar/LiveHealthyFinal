package com.jsp.lh;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import entities.FoodEntity;
import entities.FoodRecordEntity;
import DBLayout.FoodDatabase;
import DBLayout.FoodRecord;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.os.Build;

public class RecordFoodActivity extends Activity implements
TextToSpeech.OnInitListener{

	private static final int VOICE_REQUEST_CODE = 1234;
	public static final int SCAN_REQUEST_CODE = 0x0000c0de;
	public static String cal_value = "--";
	private AutoCompleteTextView mactv;
	private ShakeListener mShaker;
	private TextToSpeech tts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_food);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
			.add(R.id.container, new PlaceholderFragment()).commit();
		}
		cal_value = "--";
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
	@Override
	public void onResume()
	{

		FoodDatabase fd = new FoodDatabase(RecordFoodActivity.this);
		fd.open();
		ArrayList<String> ar = new ArrayList<String>();
		ar = fd.getAllFoodNames();
		fd.close();

		String[] countries = new String[ar.size()];
		countries = ar.toArray(countries);
		ArrayAdapter adapter = new ArrayAdapter
				(this,android.R.layout.simple_list_item_1,countries);

		mactv = (AutoCompleteTextView) findViewById
				(R.id.foodText);
		mactv.setThreshold(0);

		mactv.setAdapter(adapter);
		super.onResume();
	}
	
	@Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
	
	@Override
    public void onInit(int status) {
 
        if (status == TextToSpeech.SUCCESS) {
 
            int result = tts.setLanguage(Locale.US);
 
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                
                speakOut();
            }
 
        } else {
            Log.e("TTS", "Initilization Failed!");
        }
 
    }


	private void speakOut() {
		 
        String text = "Kindly Enter the Food Details";
 
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

	public void clearFields()
	{
		EditText fname = (EditText)findViewById(R.id.foodText);
		TextView cals = (TextView)findViewById(R.id.calText);
		fname.setText("");
		cals.setText("");

	}

	public void addNewFood(View v)
	{
		Intent intent = new Intent(this, AddFoodItem.class);
		startActivity(intent);
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
					//Toast.makeText(getApplicationContext(), "got the focus", Toast.LENGTH_LONG).show();
				}else {
					//Toast.makeText(getApplicationContext(), "lost the focus", Toast.LENGTH_LONG).show();
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
						cal_value = String.valueOf(fe.getCalories());
						calt.setText(String.valueOf(fe.getCalories()));
					}
					else
					{
						cal_value = "Food not found";
						calt.setText("Food not found");
					}
					db.close();
				}
			}
		});
		TextView calText = (TextView)findViewById(R.id.calText);
		calText.setText(cal_value);
		
		tts = new TextToSpeech(this, this);
		speakOut();


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
		try {
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
				Toast.makeText(getApplicationContext(), "Record Inserted Successfully!", Toast.LENGTH_LONG).show();
				String text = "Record inserted successfully!";
				 
		        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
				fname.setText("");
				calt.setText("");
				cal_value = "--";
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Kindly check the input entered", Toast.LENGTH_LONG).show();
				String text = "Kindly check the input entered!";
				 
		        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
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

	public void onScan(View v)
	{
		IntentIntegrator scanIntegrator = new IntentIntegrator(this);
		scanIntegrator.initiateScan();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// This overrides default action
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
					cal_value = String.valueOf(fe.getCalories());
					calt.setText(String.valueOf(fe.getCalories()));
				}
				else
				{
					cal_value = "Food not found";
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
						cal_value = String.valueOf(fe.getCalories());
						calt.setText(String.valueOf(fe.getCalories()));
						text3.setText(fe.getName());
					}
					else
					{
						cal_value = "Food not found";
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

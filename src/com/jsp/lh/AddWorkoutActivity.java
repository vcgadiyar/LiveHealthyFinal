package com.jsp.lh;

import java.util.ArrayList;

import entities.ExerciseEntity;
import entities.FoodEntity;
import DBLayout.ExerciseDatabase;
import DBLayout.FoodDatabase;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Build;

public class AddWorkoutActivity extends Activity {

	private static final int VOICE_REQUEST_CODE = 1234;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_workout);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_workout, menu);
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

	/* Callback for Voice Button Click */
	public void onSpeak(View v) {
		startVoiceRecognitionActivity();
	}

	/**
	 * Fire an intent to start the voice recognition activity.
	 */
	private void startVoiceRecognitionActivity() {
		System.out.println("Starting activity lel");
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
				"Voice recognition Demo...");
		startActivityForResult(intent, VOICE_REQUEST_CODE);
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
			View rootView = inflater.inflate(R.layout.fragment_add_workout,
					container, false);
			return rootView;
		}
	}

	public void recordEntry(View v) {
		try {
			EditText wname = (EditText) findViewById(R.id.input_workout_type1);
			EditText cal = (EditText) findViewById(R.id.input_calpermin1);

			String ftext = wname.getText().toString();
			String caltext = cal.getText().toString();

			if (ftext.matches("") || caltext.matches("")) {
				Toast toast = Toast.makeText(getApplicationContext(),
						"Kindly check Input entered!", Toast.LENGTH_SHORT);
				toast.show();
			} else {
				ExerciseEntity fe = new ExerciseEntity(ftext,
						Integer.parseInt(caltext));
				ExerciseDatabase connector = new ExerciseDatabase(
						AddWorkoutActivity.this);
				if (connector.insertExercise(fe) < 0) {
					Toast toast = Toast.makeText(getApplicationContext(),
							"Recording failed!", Toast.LENGTH_SHORT);
					toast.show();

				} else {
					Toast toast = Toast
							.makeText(getApplicationContext(),
									"Successfully inserted record!",
									Toast.LENGTH_SHORT);
					toast.show();
				}
				wname.setText("");
				cal.setText("");
			}
		} catch (Exception e) {
			Toast toast = Toast.makeText(getApplicationContext(),
					"Kindly check Input entered!", Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	/**
	 * Handle the results from the voice recognition activity.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		/* On Voice Result */
		if (requestCode == VOICE_REQUEST_CODE) {
			System.out.println("Came here");
			String matchedWord = null;

			if (requestCode == VOICE_REQUEST_CODE && resultCode == RESULT_OK) {
				// Populate the wordsList with the String values the recognition
				// engine thought it heard
				ArrayList<String> matches = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				System.out.println("Printing matched words");
				matchedWord = matches.get(0);
			}
			super.onActivityResult(requestCode, resultCode, data);

			EditText text3 = (EditText) findViewById(R.id.input_workout_type1);

			if (matchedWord != null) {
				text3.setText(matchedWord);
			} else {
				Toast toast = Toast.makeText(getApplicationContext(),
						"No Voice data received!", Toast.LENGTH_SHORT);
				toast.show();
			}
		}
	}

}

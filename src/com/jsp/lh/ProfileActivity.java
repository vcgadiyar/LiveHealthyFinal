package com.jsp.lh;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import entities.UserEntity;
import DBLayout.UserProfile;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ProfileActivity extends Activity {
	
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	private Uri fileUri;
	private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}
	
	public void startCam(View v)
	{
		// create Intent to take a picture and return control to the calling application
	    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

	    fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
	    String fpath = fileUri.getEncodedPath();
	    System.out.println("File Path is "+ fpath);
	    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

	    // start the image capture Intent
	    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
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
	
	/** Create a file Uri for saving an image or video */
	private static Uri getOutputMediaFileUri(int type){
	      return Uri.fromFile(getOutputMediaFile(type));
	}
	
	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type){
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.

	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "MyCameraApp");
	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.

	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            Log.d("MyCameraApp", "failed to create directory");
	            return null;
	        }
	    }

	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile;
	    if (type == MEDIA_TYPE_IMAGE){
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "IMG_"+ timeStamp + ".jpg");
	    } else if(type == MEDIA_TYPE_VIDEO) {
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "VID_"+ timeStamp + ".mp4");
	    } else {
	        return null;
	    }

	    return mediaFile;
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
	        if (resultCode == RESULT_OK) {
	            // Image captured and saved to fileUri specified in the Intent
	            String path = fileUri.getEncodedPath();
	            ImageView im = (ImageView)findViewById(R.id.imageView1);
	            Bitmap image = BitmapFactory.decodeFile(path);
	            im.setImageBitmap(image);
	        } else if (resultCode == RESULT_CANCELED) {
	            // User cancelled the image capture
	        } else {
	            // Image capture failed, advise user
	        }
	    }

	    if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
	        if (resultCode == RESULT_OK) {
	            // Video captured and saved to fileUri specified in the Intent
	            Toast.makeText(this, "Video saved to:\n" +
	                     data.getData(), Toast.LENGTH_LONG).show();
	        } else if (resultCode == RESULT_CANCELED) {
	            // User cancelled the video capture
	        } else {
	            // Video capture failed, advise user
	        }
	    }
	}
}

package DBLayout;
//DatabaseConnector.java
//Provides easy connection and creation of UserContacts database.


import java.util.ArrayList;

import entities.UserEntity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class UserProfile 
{
	// database name
	private static final String DATABASE_NAME = "UserProfile";
	private SQLiteDatabase database; // database object
	private DatabaseOpenHelper databaseOpenHelper; // database helper

	// public constructor for DatabaseConnector
	public UserProfile(Context context) 
	{
		// create a new DatabaseOpenHelper
		databaseOpenHelper = 
				new DatabaseOpenHelper(context, DATABASE_NAME, null, 1);
	} // end DatabaseConnector constructor

	// open the database connection
	public void open() throws SQLException 
	{
		// create or open a database for reading/writing
		database = databaseOpenHelper.getWritableDatabase();
	} // end method open

	// close the database connection
	public void close() 
	{
		if (database != null)
			database.close(); // close the database connection
	} // end method close

	// inserts a new contact in the database
	public void insertUser(UserEntity u) 
	{
		ContentValues newUser = new ContentValues();
		newUser.put("fname", u.getFname());
		newUser.put("lname", u.getLname());
		newUser.put("age", u.getAge());
		newUser.put("hft", u.getHft());
		newUser.put("hinch", u.getHinch());
		newUser.put("weight", u.getWeight());

		open(); // open the database
		database.insert("UserProfile", null, newUser);
		close(); // close the database
	} // end method insertContact

	// inserts a new contact in the database
	public void updateUser(int id, UserEntity u) 
	{
		ContentValues editUser = new ContentValues();
		editUser.put("fname", u.getFname());
		editUser.put("lname", u.getLname());
		editUser.put("age", u.getAge());
		editUser.put("hft", u.getHft());
		editUser.put("hinch", u.getHinch());
		editUser.put("weight", u.getWeight());

		open(); // open the database
		database.update("UserProfile", editUser, "id=" + id, null);
		close(); // close the database
	} // end method updateContact

	// return a Cursor with all contact information in the database
	public ArrayList<UserEntity> getAllUsers() 
	{
		Cursor result = database.query("UserProfile", null, 
				null, null, null, null, null);
		int fnameIndex;
		int lnameIndex;
		int ageIndex;
		int hftIndex;
		int hinchIndex;
		int wtIndex;

		UserEntity e1;
		ArrayList<UserEntity> ar = new ArrayList<UserEntity>();
		if( result != null && result.moveToFirst() )
		{
			while (result.isAfterLast() == false) 
			{
				fnameIndex = result.getColumnIndex("fname");
				lnameIndex = result.getColumnIndex("lname");
				ageIndex = result.getColumnIndex("age");
				hftIndex = result.getColumnIndex("hft");
				hinchIndex = result.getColumnIndex("hinch");
				wtIndex = result.getColumnIndex("weight");
				// fill TextViews with the retrieved data

				e1 = new UserEntity(result.getString(fnameIndex), result.getString(lnameIndex),
						result.getInt(ageIndex), result.getInt(hftIndex), result.getInt(hinchIndex),
						result.getInt(wtIndex));
				ar.add(e1);
				result.moveToNext();
			}
		}
		return ar;
	} // end method getAllContacts

	// get a Cursor containing all information about the contact specified
	// by the given id
	public UserEntity getOneUser(int id) 
	{
		Cursor result = database.query("UserProfile", null, 
				null, null, null, null, null);
		int fnameIndex;
		int lnameIndex;
		int ageIndex;
		int hftIndex;
		int hinchIndex;
		int wtIndex;

		UserEntity e1 = null;
		if( result != null && result.moveToFirst() )
		{
			fnameIndex = result.getColumnIndex("fname");
			lnameIndex = result.getColumnIndex("lname");
			ageIndex = result.getColumnIndex("age");
			hftIndex = result.getColumnIndex("hft");
			hinchIndex = result.getColumnIndex("hinch");
			wtIndex = result.getColumnIndex("weight");
			// fill TextViews with the retrieved data

			e1 = new UserEntity(result.getString(fnameIndex), result.getString(lnameIndex),
					result.getInt(ageIndex), result.getInt(hftIndex), result.getInt(hinchIndex),
					result.getInt(wtIndex));

			result.moveToNext();

		}
		
		return e1;
	} // end method getOnContact

	public void removeAll()
	{
		// db.delete(String tableName, String whereClause, String[] whereArgs);
		// If whereClause is null, it will delete all rows.
		open();
		database.delete(DATABASE_NAME, null, null);
		close();
	}

	// delete the contact specified by the given String name
	public void deleteFood(int id) 
	{
		open(); // open the database
		database.delete("UserProfile", "id=" + id, null);
		close(); // close the database
	} // end method deleteContact

	private class DatabaseOpenHelper extends SQLiteOpenHelper 
	{
		// public constructor
		public DatabaseOpenHelper(Context context, String name,
				CursorFactory factory, int version) 
		{
			super(context, name, factory, version);
		} // end DatabaseOpenHelper constructor

		// creates the contacts table when the database is created
		@Override
		public void onCreate(SQLiteDatabase db) 
		{
			// query to create a new table named contacts
			String createQuery = "CREATE TABLE UserProfile" +
					"(id integer primary key," +
					"fname TEXT, lname TEXT, age integer,  hft integer, hinch integer, weight integer);";

			db.execSQL(createQuery); // execute the query
		} // end method onCreate

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, 
				int newVersion) 
		{
		} // end method onUpgrade
	} // end class DatabaseOpenHelper
} // end class DatabaseConnector


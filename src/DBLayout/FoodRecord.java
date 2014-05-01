package DBLayout;
//DatabaseConnector.java
//Provides easy connection and creation of UserContacts database.


import java.util.ArrayList;

import entities.FoodRecordEntity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class FoodRecord 
{
	// database name
	private static final String DATABASE_NAME = "FoodRecord";
	private SQLiteDatabase database; // database object
	private DatabaseOpenHelper databaseOpenHelper; // database helper

	// public constructor for DatabaseConnector
	public FoodRecord(Context context) 
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
	public void insertRecord(FoodRecordEntity f) 
	{
		ContentValues newFood = new ContentValues();
		newFood.put("foodid", f.getFoodid());
		newFood.put("timestamp", f.getTimestamp());


		open(); // open the database
		database.insert("FoodRecord", null, newFood);
		close(); // close the database
	} // end method insertContact

	// inserts a new contact in the database
	// Timestamp can be of the format '2007-01-01 10:00:00'
	public void updateRecord(int id, FoodRecordEntity f) 
	{
		ContentValues editFood = new ContentValues();
		editFood.put("foodid", f.getFoodid());
		editFood.put("timestamp", f.getTimestamp());

		open(); // open the database
		database.update("FoodRecord", editFood, "id=" + id, null);
		close(); // close the database
	} // end method updateContact

	// return a Cursor with all contact information in the database
	public ArrayList<FoodRecordEntity> getAllRecords() 
	{
		Cursor result = database.query("FoodRecord", null, 
				null, null, null, null, null);
		int idIndex;
		int tsIndex;
		FoodRecordEntity e1;
		ArrayList<FoodRecordEntity> ar = new ArrayList<FoodRecordEntity>();
		if( result != null && result.moveToFirst() )
		{
			while (result.isAfterLast() == false) 
			{
				idIndex = result.getColumnIndex("foodid");
				tsIndex = result.getColumnIndex("timestamp");
				// fill TextViews with the retrieved data

				e1 = new FoodRecordEntity(result.getInt(idIndex), result.getString(tsIndex));
				ar.add(e1);
				result.moveToNext();
			}
		}
		return ar;
	} // end method getAllContacts

	// get a Cursor containing all information about the contact specified
	// by the given id
	public FoodRecordEntity getOneRecord(int id) 
	{
		Cursor result = database.query("FoodRecord", null, 
				null, null, null, null, null);
		int idIndex;
		int tsIndex;
		FoodRecordEntity e1 = null;
		
		if( result != null && result.moveToFirst() )
		{
			
				idIndex = result.getColumnIndex("foodid");
				tsIndex = result.getColumnIndex("timestamp");
				// fill TextViews with the retrieved data

				e1 = new FoodRecordEntity(result.getInt(idIndex), result.getString(tsIndex));
			
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
	public void deleteRecord(int id) 
	{
		open(); // open the database
		database.delete("FoodRecord", "id=" + id, null);
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
			String createQuery = "CREATE TABLE FoodRecord" +
					"(id integer primary key autoincrement," +
					"foodid integer, timestamp TEXT"
					+ "FOREIGN KEY (foodid) REFERENCES FoodDetails (id));";

			db.execSQL(createQuery); // execute the query
		} // end method onCreate

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, 
				int newVersion) 
		{
		} // end method onUpgrade
	} // end class DatabaseOpenHelper
} // end class DatabaseConnector


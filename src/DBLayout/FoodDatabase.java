package DBLayout;

//DatabaseConnector.java
//Provides easy connection and creation of UserContacts database.

import java.util.ArrayList;

import entities.FoodEntity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class FoodDatabase {
	// database name
	private static final String DATABASE_NAME = "FoodDetails";
	private SQLiteDatabase database; // database object
	private DatabaseOpenHelper databaseOpenHelper; // database helper

	// public constructor for DatabaseConnector
	public FoodDatabase(Context context) {
		// create a new DatabaseOpenHelper
		databaseOpenHelper = new DatabaseOpenHelper(context, DATABASE_NAME,
				null, 1);

	} // end DatabaseConnector constructor

	// open the database connection
	public void open() throws SQLException {
		// create or open a database for reading/writing
		database = databaseOpenHelper.getWritableDatabase();
	} // end method open

	// close the database connection
	public void close() {
		if (database != null)
			database.close(); // close the database connection
	} // end method close

	// inserts a new contact in the database
	public long insertFood(FoodEntity f) {
		ContentValues newFood = new ContentValues();
		newFood.put("name", f.getName());
		newFood.put("calories", f.getCalories());
		newFood.put("barcode", f.getBarcode());

		open(); // open the database
		long ret = database.insert("FoodDetails", null, newFood);
		close(); // close the database
		return ret;
	} // end method insertContact

	// inserts a new contact in the database
	public void updateFood(FoodEntity f) {
		ContentValues editFood = new ContentValues();
		editFood.put("name", f.getName());
		editFood.put("calories", f.getCalories());
		editFood.put("barcode", f.getBarcode());

		open(); // open the database
		database.update("FoodDetails", editFood, "name=" + f.getName(), null);
		close(); // close the database
	} // end method updateContact

	// return a Cursor with all contact information in the database
	public ArrayList<FoodEntity> getAllFoods() {
		Cursor result = database.query("FoodDetails", null, null, null, null,
				null, null);
		int nameIndex;
		int calIndex, barindex;
		FoodEntity e1;
		ArrayList<FoodEntity> ar = new ArrayList<FoodEntity>();
		if (result != null && result.moveToFirst()) {
			while (result.isAfterLast() == false) {
				nameIndex = result.getColumnIndex("name");
				calIndex = result.getColumnIndex("calories");
				barindex = result.getColumnIndex("barcode");
				// fill TextViews with the retrieved data

				String exName = result.getString(nameIndex);
				int calpermin = result.getInt(calIndex);
				String barcode = result.getString(barindex);
				e1 = new FoodEntity(exName, calpermin, barcode);
				ar.add(e1);
				result.moveToNext();
			}
		}
		return ar;
	} // end method getAllContacts

	// get a Cursor containing all information about the contact specified
	// by the given id
	public FoodEntity getOneFood(String name) {
		Cursor result = database.rawQuery(
				"SELECT * FROM FoodDetails WHERE name = '" + name.trim()
						+ "' COLLATE NOCASE", null);
		int nameIndex;
		int calIndex, barindex;
		FoodEntity e1 = null;
		if (result != null && result.moveToFirst()) {
			System.out.println("Name input = " + name);
			nameIndex = result.getColumnIndex("name");
			calIndex = result.getColumnIndex("calories");
			barindex = result.getColumnIndex("barcode");
			// fill TextViews with the retrieved data

			System.out.println("nameIndex:" + nameIndex + " calIndex:"
					+ calIndex + " barindex:" + barindex);

			String exName = result.getString(nameIndex);
			int calpermin = result.getInt(calIndex);
			String barcode = result.getString(barindex);
			e1 = new FoodEntity(exName, calpermin, barcode);
		}
		return e1;
	} // end method getOnContact
	
	public ArrayList<String> getAllFoodNames()
	{
		Cursor result = database.rawQuery(
				"SELECT * FROM FoodDetails", null);
		int nameIndex;
		ArrayList<String> ar = new ArrayList<String>();
		if (result != null && result.moveToFirst()) {
			while (result.isAfterLast() == false) {
				nameIndex = result.getColumnIndex("name");
				
				String exName = result.getString(nameIndex);
				
				ar.add(exName);
				result.moveToNext();
			}
		}
		return ar;
		
	}

	public FoodEntity getOneFoodFromBarCode(String bcode) {
		Cursor result = database.rawQuery(
				"SELECT * FROM FoodDetails WHERE barcode = '" + bcode.trim()
						+ "' COLLATE NOCASE", null);
		int nameIndex;
		int calIndex, barindex;
		FoodEntity e1 = null;
		if (result != null && result.moveToFirst()) {
			System.out.println("Name input = " + bcode);
			nameIndex = result.getColumnIndex("name");
			calIndex = result.getColumnIndex("calories");
			barindex = result.getColumnIndex("barcode");
			// fill TextViews with the retrieved data

			System.out.println("nameIndex:" + nameIndex + " calIndex:"
					+ calIndex + " barindex:" + barindex);

			String exName = result.getString(nameIndex);

			int calpermin = result.getInt(calIndex);
			String barcode = result.getString(barindex);
			System.out.println("Name =" + exName + " barcode=" + barcode
					+ " cals: " + calpermin);
			e1 = new FoodEntity(exName, calpermin, barcode);
		}
		return e1;
	} // end method getOnContact

	public void removeAll() {
		// db.delete(String tableName, String whereClause, String[] whereArgs);
		// If whereClause is null, it will delete all rows.
		open();
		database.delete(DATABASE_NAME, null, null);
		close();
	}

	// delete the contact specified by the given String name
	public void deleteFood(String name) {
		open(); // open the database
		database.delete("FoodDetails", "name=" + name, null);
		close(); // close the database
	} // end method deleteContact

	private class DatabaseOpenHelper extends SQLiteOpenHelper {

		// public constructor
		public DatabaseOpenHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);

		} // end DatabaseOpenHelper constructor

		// creates the contacts table when the database is created
		@Override
		public void onCreate(SQLiteDatabase db) {
			// query to create a new table named contacts
			String createQuery = "CREATE TABLE FoodDetails"
					+ "(name TEXT primary key, barcode TEXT, calories integer);";

			db.execSQL(createQuery); // execute the query
		} // end method onCreate

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		} // end method onUpgrade
	} // end class DatabaseOpenHelper
} // end class DatabaseConnector


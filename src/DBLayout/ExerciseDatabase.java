package DBLayout;
//DatabaseConnector.java
//Provides easy connection and creation of UserContacts database.


import java.util.ArrayList;

import entities.ExerciseEntity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class ExerciseDatabase 
{
// database name
private static final String DATABASE_NAME = "ExerciseDetails";
private SQLiteDatabase database; // database object
private DatabaseOpenHelper databaseOpenHelper; // database helper

// public constructor for DatabaseConnector
public ExerciseDatabase(Context context) 
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
public void insertExercise(ExerciseEntity e) 
{
   ContentValues newExercise = new ContentValues();
   newExercise.put("name", e.getName());
   newExercise.put("caloriespermin", e.getCaloriesPerMin());
   
 
   open(); // open the database
   database.insert("ExerciseDetails", null, newExercise);
   close(); // close the database
} // end method insertContact

// inserts a new contact in the database
public void updateExercise(int id, ExerciseEntity e) 
{
   ContentValues editExercise = new ContentValues();
   editExercise.put("name", e.getName());
   editExercise.put("caloriespermin", e.getCaloriesPerMin());
 
   open(); // open the database
   database.update("ExerciseDetails", editExercise, "id=" + id, null);
   close(); // close the database
} // end method updateContact

// return a Cursor with all contact information in the database
public ArrayList<ExerciseEntity> getAllExercises() 
{
	Cursor result = database.query("ExerciseDetails", null, 
			null, null, null, null, null);
	int nameIndex;
	int calIndex;
	ExerciseEntity e1;
	ArrayList<ExerciseEntity> ar = new ArrayList<ExerciseEntity>();
	if( result != null && result.moveToFirst() )
	{
		while (result.isAfterLast() == false) 
		{
			nameIndex = result.getColumnIndex("name");
			calIndex = result.getColumnIndex("caloriespermin");
			// fill TextViews with the retrieved data

			String exName = result.getString(nameIndex);
			int calpermin = result.getInt(calIndex);
			e1 = new ExerciseEntity(exName, calpermin);
			ar.add(e1);
			result.moveToNext();
		}
	}
	return ar;
} // end method getAllContacts

// get a Cursor containing all information about the contact specified
// by the given id
public ExerciseEntity getOneExercise(int id) 
{
	Cursor result = database.query(
      "ExerciseDetails", null, "id=" + id, null, null, null, null);
	int nameIndex;
	int calIndex;
	ExerciseEntity e1 = null;
	if( result != null && result.moveToFirst() )
	{
		nameIndex = result.getColumnIndex("name");
		calIndex = result.getColumnIndex("caloriespermin");
		// fill TextViews with the retrieved data

		String exName = result.getString(nameIndex);
		int calpermin = result.getInt(calIndex);
		e1 = new ExerciseEntity(exName, calpermin);
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
   database.delete("ExerciseDetails", "id=" + id, null);
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
      String createQuery = "CREATE TABLE ExerciseDetails" +
         "(id integer primary key," +
         "name TEXT, caloriespermin integer);";
               
      db.execSQL(createQuery); // execute the query
   } // end method onCreate

   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, 
       int newVersion) 
   {
   } // end method onUpgrade
} // end class DatabaseOpenHelper
} // end class DatabaseConnector


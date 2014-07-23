package com.brightr.weathermate.databases;

import java.util.ArrayList;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LocationStorage {
	
	
	public static final String KEY_ROWID = "row_id";
	public static final String KEY_LOCATIONNAME = "location";
	public static final String KEY_LATITUDE = "latitude";
	public static final String KEY_LONGITUDE = "longitude";
	public static final String KEY_COUNTRY = "country";
	
	private static final String DATABASE_NAME = "LocationsDatabase";
	private static final String DATABASE_TABLE = "saved_locations";
	private static final int DATABASE_VERSION = 2;
	
	
	
	private LocationDBHelper mHelper;
	private final Context mContext;
	private SQLiteDatabase mDatabase;
	
	ArrayList<String> mNames = new ArrayList<String>();
	ArrayList<String> mLatitudes = new ArrayList<String>();
	ArrayList<String> mLongitudes = new ArrayList<String>();
	private ArrayList<String> mCountries = new ArrayList<String>();
	
	
	private static class LocationDBHelper extends SQLiteOpenHelper {

		public LocationDBHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);

		}
		
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" + KEY_ROWID
					+ " INTEGER PRIMARY KEY,  " + KEY_LOCATIONNAME
					+ " TEXT, " + KEY_COUNTRY + " TEXT, " + KEY_LATITUDE + " TEXT, " + KEY_LONGITUDE +  " TEXT);");

			
		}
		
		
		
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);

		}
		
	}




		public LocationStorage(Context context) {
			mContext = context;
		}
		
		
		
		public LocationStorage open() throws SQLException {

			mHelper = new LocationDBHelper(mContext);
			mDatabase = mHelper.getWritableDatabase();

			return this;

		}
		
		
		public void close() {
			mHelper.close();
		}
		
		
		
		public long insertData(String location, String country, String latitude, String longitude) {
			
			

			ContentValues cv = new ContentValues();
			try {

				cv.put(KEY_LOCATIONNAME, location);
				cv.put(KEY_LATITUDE, latitude);
				cv.put(KEY_LONGITUDE, longitude);
				cv.put(KEY_COUNTRY, country);
				

				Log.d("LocationStorage", "Location stored successfully!");

			} catch (Exception e) {
				e.printStackTrace();
			}

			return mDatabase.insert(DATABASE_TABLE, null, cv);

		}
		
		
		public void removeEntry(int rowIndex) {

			try {
				mDatabase.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowIndex, null);

				Log.w("LocationStorage", "DATABASE ---> Location at index  " + rowIndex
						+ " successfully removed from DB");

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		public void getLocations(){
			
			String[] columns = {KEY_ROWID, KEY_LOCATIONNAME, KEY_COUNTRY, KEY_LATITUDE, KEY_LONGITUDE};
			
			try{
			
			Cursor c = mDatabase.query(DATABASE_TABLE, columns, null, null,
					null, null, null);
			
			int index_row = c.getColumnIndex(KEY_ROWID);
			int column_location = c.getColumnIndex(KEY_LOCATIONNAME);
			int column_country = c.getColumnIndex(KEY_COUNTRY);
			int column_latitude = c.getColumnIndex(KEY_LATITUDE);
			int column_longitude = c.getColumnIndex(KEY_LONGITUDE);
			
			
			for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				
				String location = c.getString(column_location);
				mNames.add(location);
				
				String latitude = c.getString(column_latitude);
				mLatitudes.add(latitude);
				
				String longitude = c.getString(column_longitude);
				mLongitudes.add(longitude);
				
				String country = c.getString(column_country);
				mCountries.add(country);
				
				
				
			}
			
			c.close();
			Log.d("LocationStorage", "Retrieved Locations List Successfully!");
			Log.d("LocationStorage", "Country List --> " + mCountries.toString());
			
			
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		
		public ArrayList<String> getCountries(){
			return mCountries;
		}
		
		public ArrayList<String> getLocationNames(){
			
			return mNames;
		}

		public ArrayList<String> getLatitudes() {

			return mLatitudes;
		}

		public ArrayList<String> getLongitudes() {

			return mLongitudes;
		}


	

}
	


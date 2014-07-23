package com.brightr.weathermate.databases;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FlightStorage {

	public static final String KEY_ROWID = "_ID";

	public static final String KEY_FLIGHT_NUMBER = "flight_number";

	// Columns for departure data

	public static final String KEY_DEPARTURE_CITY = "departure_city";
	public static final String KEY_DEPARTURE_AIRPORT = "departure_airport";
	public static final String KEY_DEPARTURE_DATETIME = "departure_date_time";
	public static final String KEY_DEPARTURE_TERMINAL = "departure_terminal";
	public static final String KEY_DEPARTURE_GATE = "departure_gate";

	// Colums for arrival data

	public static final String KEY_ARRIVAL_CITY = "arrival_city";
	public static final String KEY_ARRIVAL_AIRPORT = "arrival_airport";
	public static final String KEY_AIRCRAFT_TYPE = "aircraft_type";
	public static final String KEY_ARRIVAL_DATETIME = "arrival_date_time";
	public static final String KEY_ARRIVAL_TERMINAL = "arrival_terminal";
	public static final String KEY_ARRIVAL_GATE = "arrival_gate";
	public static final String KEY_BAGGAGE_CLAIM = "baggage_claim";
	public static final String KEY_PLANE_SPEED = "plane_speed";
	public static final String KEY_PLANE_ALTITUDE = "plane_altitude";

	private static final String DATABASE_NAME = "FlightInfoDatabase";
	private static final String DATABASE_TABLE = "Flights";
	private static final int DATABASE_VERSION = 2;

	public static final String KEY_FLIGHT_ID = "flight_id";

	// Global ContentValues

	private FlightDBHelper mHelper;
	private final Context mContext;
	private SQLiteDatabase mDatabase;

	// Empty strings for database retrieval
	String row_id = "";
	String flight_number;
	String aircraft_type;
	String departure_airport;
	String departure_city;
	String departure_dateTime;
	String departure_terminal;
	String departure_gate;
	String arrival_city;
	String arrival_airport;
	String arrival_dateTime;
	String arrival_terminal;
	String arrival_gate;
	String baggage_claim;
	String plane_speed;
	String plane_altitude;
	String flight_id;

	// ArrayLists that will store flight data
	ArrayList<String> mRowIds = new ArrayList<String>();
	ArrayList<String> mFlightNumbers = new ArrayList<String>();
	ArrayList<String> mAircraftTypes = new ArrayList<String>();
	ArrayList<String> mDepartureAirports = new ArrayList<String>();
	ArrayList<String> mDepartureDateTimes = new ArrayList<String>();
	ArrayList<String> mDepartureCities = new ArrayList<String>();
	ArrayList<String> mDepartureTerminals = new ArrayList<String>();
	ArrayList<String> mDepartureGates = new ArrayList<String>();
	ArrayList<String> mArrivalCities = new ArrayList<String>();
	ArrayList<String> mArrivalAirports = new ArrayList<String>();
	ArrayList<String> mArrivalDateTimes = new ArrayList<String>();
	ArrayList<String> mArrivalTerminals = new ArrayList<String>();
	ArrayList<String> mArrivalGates = new ArrayList<String>();
	ArrayList<String> mBaggageClaims = new ArrayList<String>();
	ArrayList<String> mPlaneSpeeds = new ArrayList<String>();
	ArrayList<String> mPlaneAltitudes = new ArrayList<String>();
    ArrayList<String> mFlightIds = new ArrayList<String>();
    
	private static class FlightDBHelper extends SQLiteOpenHelper {

		public FlightDBHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);

		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" + KEY_ROWID
					+ " INTEGER PRIMARY KEY, " + KEY_FLIGHT_NUMBER + " TEXT, "
					+ KEY_AIRCRAFT_TYPE + " TEXT, " + KEY_DEPARTURE_AIRPORT
					+ " TEXT, " + KEY_DEPARTURE_CITY + " TEXT, "
					+ KEY_DEPARTURE_DATETIME + " TEXT, "
					+ KEY_DEPARTURE_TERMINAL + " TEXT, " + KEY_DEPARTURE_GATE
					+ " TEXT, " + KEY_ARRIVAL_CITY + " TEXT, "
					+ KEY_ARRIVAL_AIRPORT + " TEXT, " + KEY_ARRIVAL_DATETIME
					+ " TEXT, " + KEY_ARRIVAL_TERMINAL + " TEXT, "
					+ KEY_ARRIVAL_GATE + " TEXT, " + KEY_BAGGAGE_CLAIM
					+ " TEXT, " + KEY_PLANE_SPEED + " TEXT, "
					+ KEY_PLANE_ALTITUDE + " TEXT, " + KEY_FLIGHT_ID + " TEXT);"

			);

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}

	}

	public FlightStorage(Context context) {
		mContext = context;
	}

	public FlightStorage open() throws SQLException {

		mHelper = new FlightDBHelper(mContext);
		mDatabase = mHelper.getWritableDatabase();

		return this;

	}

	public void close() {
		mHelper.close();
	}

	public long insertData(String flightNumber, String departureCity,
			String departureAirport, String departureDateTime,
			String aircraftType, String departureTerminal,
			String departureGate, String arrivalCity, String arrivalAirport,
			String arrivalDateTime, String arrivalTerminal, String arrivalGate,
			String baggageClaim, String planeSpeed, String planeAltitude, String flightId) {
		ContentValues cv = new ContentValues();
		try {

			cv.put(KEY_FLIGHT_NUMBER, flightNumber);
			cv.put(KEY_DEPARTURE_CITY, departureCity);
			cv.put(KEY_DEPARTURE_AIRPORT, departureAirport);
			cv.put(KEY_DEPARTURE_DATETIME, departureDateTime);
			cv.put(KEY_AIRCRAFT_TYPE, aircraftType);
			cv.put(KEY_DEPARTURE_TERMINAL, departureTerminal);
			cv.put(KEY_DEPARTURE_GATE, departureGate);
			cv.put(KEY_ARRIVAL_CITY, arrivalCity);
			cv.put(KEY_ARRIVAL_AIRPORT, arrivalAirport);
			cv.put(KEY_ARRIVAL_DATETIME, arrivalDateTime);
			cv.put(KEY_ARRIVAL_TERMINAL, arrivalTerminal);
			cv.put(KEY_ARRIVAL_GATE, arrivalGate);
			cv.put(KEY_BAGGAGE_CLAIM, baggageClaim);
			cv.put(KEY_PLANE_SPEED, planeSpeed);
			cv.put(KEY_PLANE_ALTITUDE, planeAltitude);
			cv.put(KEY_FLIGHT_ID, flightId);

			Log.d("FlightStorage", "Dates stored successfully!");

		} catch (Exception e) {
			e.printStackTrace();
		}
       // mDatabase.delete(DATABASE_TABLE, null, null);
		return mDatabase.insert(DATABASE_TABLE, null, cv);

	}

	/*
	 * public ArrayList<String> getDates() {
	 * 
	 * String[] columns = new String[] { KEY_ROWID, KEY_DAY1_DATE,
	 * KEY_DAY2_DATE, KEY_DAY3_DATE, KEY_DAY4_DATE, KEY_DAY5_DATE };
	 * ArrayList<String> dates = new ArrayList<String>(); Cursor c =
	 * mDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);
	 * 
	 * String row; String first = " "; String second = ""; String third = "";
	 * String fourth = ""; String fifth = "";
	 * 
	 * int index_row = c.getColumnIndex(KEY_ROWID); int day1_row =
	 * c.getColumnIndex(KEY_DAY1_DATE); int day2_row =
	 * c.getColumnIndex(KEY_DAY2_DATE); int day3_row =
	 * c.getColumnIndex(KEY_DAY3_DATE); int day4_row =
	 * c.getColumnIndex(KEY_DAY4_DATE); int day5_row =
	 * c.getColumnIndex(KEY_DAY5_DATE);
	 * 
	 * for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
	 * 
	 * row = c.getString(index_row); first = c.getString(day1_row); second =
	 * c.getString(day2_row); third = c.getString(day3_row); fourth =
	 * c.getString(day4_row); fifth = c.getString(day5_row); }
	 * 
	 * dates.add(first); dates.add(second); dates.add(third); dates.add(fourth);
	 * dates.add(fifth); c.close(); Log.d("WeatherStorage",
	 * "Retrieved dates --> " + dates.toString()); return dates;
	 * 
	 * }
	 */
	
	public void getLastRecord(){
		
	}

	public void getFlightDetails() {

		try {

			String[] columns = new String[] { KEY_ROWID, KEY_FLIGHT_NUMBER,
					KEY_AIRCRAFT_TYPE, KEY_DEPARTURE_AIRPORT,
					KEY_DEPARTURE_CITY, KEY_DEPARTURE_DATETIME,
					KEY_DEPARTURE_TERMINAL, KEY_DEPARTURE_GATE,
					KEY_ARRIVAL_CITY, KEY_ARRIVAL_AIRPORT,
					KEY_ARRIVAL_DATETIME, KEY_ARRIVAL_TERMINAL,
					KEY_ARRIVAL_GATE, KEY_BAGGAGE_CLAIM, KEY_PLANE_SPEED,
					KEY_PLANE_ALTITUDE, KEY_FLIGHT_ID };

			Cursor c = mDatabase.query(DATABASE_TABLE, columns, null, null,
					null, null, null);

			int index_row = c.getColumnIndex(KEY_ROWID);
			int column1 = c.getColumnIndex(KEY_FLIGHT_NUMBER);
			int column2 = c.getColumnIndex(KEY_AIRCRAFT_TYPE);
			int column3 = c.getColumnIndex(KEY_DEPARTURE_AIRPORT);
			int column4 = c.getColumnIndex(KEY_DEPARTURE_CITY);
			int column5 = c.getColumnIndex(KEY_DEPARTURE_DATETIME);
			int column6 = c.getColumnIndex(KEY_DEPARTURE_TERMINAL);
			int column7 = c.getColumnIndex(KEY_DEPARTURE_GATE);
			int column8 = c.getColumnIndex(KEY_ARRIVAL_CITY);
			int column9 = c.getColumnIndex(KEY_ARRIVAL_AIRPORT);
			int column10 = c.getColumnIndex(KEY_ARRIVAL_DATETIME);
			int column11 = c.getColumnIndex(KEY_ARRIVAL_TERMINAL);
			int column12 = c.getColumnIndex(KEY_ARRIVAL_GATE);
			int column13 = c.getColumnIndex(KEY_BAGGAGE_CLAIM);
			int column14 = c.getColumnIndex(KEY_PLANE_SPEED);
			int column15 = c.getColumnIndex(KEY_PLANE_ALTITUDE);
			int column16 = c.getColumnIndex(KEY_FLIGHT_ID);

			for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {

				// Iterate through each column and each row, getting each piece
				// of flight data and storing it to a respective ArrayList

				row_id = c.getString(index_row);
				mRowIds.add(row_id);

				flight_number = c.getString(column1);
				mFlightNumbers.add(flight_number);

				aircraft_type = c.getString(column2);
				mAircraftTypes.add(aircraft_type);

				departure_airport = c.getString(column3);
				mDepartureAirports.add(departure_airport);

				departure_city = c.getString(column4);
				mDepartureCities.add(departure_city);

				departure_dateTime = c.getString(column5);
				mDepartureDateTimes.add(departure_dateTime);

				departure_terminal = c.getString(column6);
				mDepartureTerminals.add(departure_terminal);

				departure_gate = c.getString(column7);
				mDepartureGates.add(departure_gate);

				arrival_city = c.getString(column8);
				mArrivalCities.add(arrival_city);

				arrival_airport = c.getString(column9);
				mArrivalAirports.add(arrival_airport);

				arrival_dateTime = c.getString(column10);
				mArrivalDateTimes.add(arrival_dateTime);

				arrival_terminal = c.getString(column11);
				mArrivalTerminals.add(arrival_terminal);

				arrival_gate = c.getString(column12);
				mArrivalGates.add(arrival_gate);

				baggage_claim = c.getString(column13);
				mBaggageClaims.add(baggage_claim);

				plane_speed = c.getString(column14);
				mPlaneSpeeds.add(plane_speed);

				plane_altitude = c.getString(column15);
				mPlaneAltitudes.add(plane_altitude);

				flight_id = c.getString(column16);
				mFlightIds.add(flight_id);
			}

			c.close();
			Log.d("FlightStorage", "Retrieved Flight Details Successfully!");
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("FlightStorage",
					"Error getting Flight details from database!");
		}

	}
	
	public void removeEntry(int rowIndex){
		
		try{
		mDatabase.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowIndex, null);
		//mHelper.onUpgrade(mDatabase, DATABASE_VERSION, DATABASE_VERSION + 1);
		Log.w("FlightStorage", "DATABASE ---> Flight Record  " + rowIndex + " successfully removed from DB" );
		
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> getFlightIds(){
		
		return mFlightIds;
	}

	public ArrayList<String> getFlightNumbers() {

		return mFlightNumbers;
	}

	public ArrayList<String> getAircraftTypes() {

		return mAircraftTypes;
	}

	public ArrayList<String> getDepartureAirports() {

		return mDepartureAirports;
	}

	public ArrayList<String> getDepartureCities() {

		return mDepartureCities;
	}

	public ArrayList<String> getDepartureDateTimes() {

		return mDepartureDateTimes;
	}

	public ArrayList<String> getDepartureTerminals() {

		return mDepartureTerminals;
	}

	public ArrayList<String> getDepartureGates() {

		return mDepartureGates;
	}

	public ArrayList<String> getArrivalCities() {

		return mArrivalCities;
	}

	public ArrayList<String> getArrivalAirports() {

		return mArrivalAirports;
	}

	public ArrayList<String> getArrivalDateTimes() {

		return mArrivalDateTimes;
	}

	public ArrayList<String> getArrivalTerminals() {

		return mArrivalTerminals;
	}

	public ArrayList<String> getArrivalGates() {

		return mArrivalGates;
	}

	public ArrayList<String> getBaggageClaims() {

		return mBaggageClaims;
	}

	public ArrayList<String> getPlaneSpeeds() {

		return mPlaneSpeeds;
	}

	public ArrayList<String> getPlaneAltitudes() {

		return mPlaneAltitudes;
	}

}

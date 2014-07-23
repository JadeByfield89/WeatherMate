package com.brightr.weathermate.databases;

import java.util.ArrayList;

import com.brightr.weathermate.R;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NewsStorage {

	public static final String KEY_ROWID = "row_id";
	public static final String KEY_SOURCE_CATEGORY = "category";
	public static final String KEY_SOURCE_LABEL = "label";
	public static final String KEY_SOURCE_URL = "url";
	public static final String KEY_SOURCE_ICON = "icon";

	private static final String DATABASE_NAME = "NewsSourceDatabase";
	private static final String DATABASE_TABLE = "News_Sources";
	private static final int DATABASE_VERSION = 2;
	public static final String KEY_CNN_ICON = "2";
	

	private NewsDBHelper mHelper;
	private final Context mContext;
	private SQLiteDatabase mDatabase;
	ArrayList<String> mLabels = new ArrayList<String>();
	ArrayList<String> mUrls = new ArrayList<String>();
	ArrayList<String> mIcons = new ArrayList<String>();

	private static class NewsDBHelper extends SQLiteOpenHelper {

		public NewsDBHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);

		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" + KEY_ROWID
					+ " INTEGER PRIMARY KEY,  " + KEY_SOURCE_CATEGORY
					+ " TEXT, " + KEY_SOURCE_LABEL + " TEXT, " + KEY_SOURCE_URL + " TEXT, " + KEY_SOURCE_ICON
					+ " TEXT);");

			// Populate the database with initial data for the categories, labels, and
			// the associated urls
			ContentValues cv = new ContentValues();

			// Put in default values for General category
			cv.put(KEY_SOURCE_CATEGORY, "general");
			cv.put(KEY_SOURCE_LABEL, "CNN");
			cv.put(KEY_SOURCE_ICON, "news_cnn");
			cv.put(KEY_SOURCE_URL, "http://www.cnn.com");
			db.insert(DATABASE_TABLE, null, cv);

			cv.put(KEY_SOURCE_CATEGORY, "general");
			cv.put(KEY_SOURCE_LABEL, "Yahoo");
			cv.put(KEY_SOURCE_URL, "http://news.yahoo.com/");
			cv.put(KEY_SOURCE_ICON, "news_yahoo");
			db.insert(DATABASE_TABLE, null, cv);

			cv.put(KEY_SOURCE_CATEGORY, "general");
			cv.put(KEY_SOURCE_LABEL, "ABC News");
			cv.put(KEY_SOURCE_URL, "http://abcnews.go.com/");
			cv.put(KEY_SOURCE_ICON, "news_abc");
			db.insert(DATABASE_TABLE, null, cv);

			cv.put(KEY_SOURCE_CATEGORY, "general");
			cv.put(KEY_SOURCE_LABEL, "NY Times");
			cv.put(KEY_SOURCE_URL, "http://www.nytimes.com/");
			cv.put(KEY_SOURCE_ICON, "news_nytimes");
			db.insert(DATABASE_TABLE, null, cv);

			cv.put(KEY_SOURCE_CATEGORY, "general");
			cv.put(KEY_SOURCE_LABEL, "CNET");
			cv.put(KEY_SOURCE_URL, "http://www.cnet.com/");
			cv.put(KEY_SOURCE_ICON, "news_cnet");
			db.insert(DATABASE_TABLE, null, cv);

			cv.put(KEY_SOURCE_CATEGORY, "general");
			cv.put(KEY_SOURCE_LABEL, "Engadget");
			cv.put(KEY_SOURCE_URL, "http://www.engadget.com/");
			cv.put(KEY_SOURCE_ICON, "news_engadget");
			db.insert(DATABASE_TABLE, null, cv);

			cv.put(KEY_SOURCE_CATEGORY, "general");
			cv.put(KEY_SOURCE_LABEL, "Reddit");
			cv.put(KEY_SOURCE_URL, "http://www.reddit.com/");
			cv.put(KEY_SOURCE_ICON, "news_reddit");
			db.insert(DATABASE_TABLE, null, cv);

			// Put in values for the Politics category

			cv.put(KEY_SOURCE_CATEGORY, "politics");
			cv.put(KEY_SOURCE_LABEL, "Politico");
			cv.put(KEY_SOURCE_URL, "http://www.politico.com/");
			cv.put(KEY_SOURCE_ICON, "news_politico");
			db.insert(DATABASE_TABLE, null, cv);

			cv.put(KEY_SOURCE_CATEGORY, "politics");
			cv.put(KEY_SOURCE_LABEL, "Raw Story");
			cv.put(KEY_SOURCE_URL, "http://rawstory.com/");
			cv.put(KEY_SOURCE_ICON, "news_rawstory");
			db.insert(DATABASE_TABLE, null, cv);

			cv.put(KEY_SOURCE_CATEGORY, "politics");
			cv.put(KEY_SOURCE_LABEL, "Reuters");
			cv.put(KEY_SOURCE_URL, "http://reuters.com");
			cv.put(KEY_SOURCE_ICON, "news_reuters");
			db.insert(DATABASE_TABLE, null, cv);

			cv.put(KEY_SOURCE_CATEGORY, "politics");
			cv.put(KEY_SOURCE_LABEL, "Drudge Report");
			cv.put(KEY_SOURCE_URL, "http://www.drudgereport.com/");
			cv.put(KEY_SOURCE_ICON, "news_drudge");
			db.insert(DATABASE_TABLE, null, cv);

			cv.put(KEY_SOURCE_CATEGORY, "politics");
			cv.put(KEY_SOURCE_LABEL, "Huff Post");
			cv.put(KEY_SOURCE_URL, "http://www.huffingtonpost.com");
			cv.put(KEY_SOURCE_ICON, "news_huffingtonpost");
			db.insert(DATABASE_TABLE, null, cv);

			// Put in values for the sports category
			cv.put(KEY_SOURCE_CATEGORY, "sports");
			cv.put(KEY_SOURCE_LABEL, "ESPN");
			cv.put(KEY_SOURCE_URL, "http://www.espn.com");
			cv.put(KEY_SOURCE_ICON, "news_espn");
			db.insert(DATABASE_TABLE, null, cv);

			cv.put(KEY_SOURCE_CATEGORY, "sports");
			cv.put(KEY_SOURCE_LABEL, "Yahoo Sports");
			cv.put(KEY_SOURCE_URL, "http://sports.yahoo.com/");
			cv.put(KEY_SOURCE_ICON, "news_yahoosports");
			db.insert(DATABASE_TABLE, null, cv);

			cv.put(KEY_SOURCE_CATEGORY, "sports");
			cv.put(KEY_SOURCE_LABEL, "CBS Sports");
			cv.put(KEY_SOURCE_URL, "http://www.cbssports.com/");
			cv.put(KEY_SOURCE_ICON, "news_cbssports");
			db.insert(DATABASE_TABLE, null, cv);

			cv.put(KEY_SOURCE_CATEGORY, "sports");
			cv.put(KEY_SOURCE_LABEL, "Bleacher");
			cv.put(KEY_SOURCE_URL, "http://bleacherreport.com/");
			cv.put(KEY_SOURCE_ICON, "news_bleacherreport");
			db.insert(DATABASE_TABLE, null, cv);

			cv.put(KEY_SOURCE_CATEGORY, "sports");
			cv.put(KEY_SOURCE_LABEL, "SI");
			cv.put(KEY_SOURCE_URL, "http://sportsillustrated.cnn.com/");
			cv.put(KEY_SOURCE_ICON, "news_sportsillustrated");
			db.insert(DATABASE_TABLE, null, cv);

			// Put in values for entertainment category
			cv.put(KEY_SOURCE_CATEGORY, "entertainment");
			cv.put(KEY_SOURCE_LABEL, "Youtube");
			cv.put(KEY_SOURCE_URL, "http://www.youtube.com");
			cv.put(KEY_SOURCE_ICON, "news_logo");
			db.insert(DATABASE_TABLE, null, cv);

			cv.put(KEY_SOURCE_CATEGORY, "entertainment");
			cv.put(KEY_SOURCE_LABEL, "CollegeHumor");
			cv.put(KEY_SOURCE_URL, "http://www.collegehumor.com");
			cv.put(KEY_SOURCE_ICON, "news_collegehumor");
			db.insert(DATABASE_TABLE, null, cv);

			cv.put(KEY_SOURCE_CATEGORY, "entertainment");
			cv.put(KEY_SOURCE_LABEL, "Forbes");
			cv.put(KEY_SOURCE_URL, "http://www.forbes.com");
			cv.put(KEY_SOURCE_ICON, "news_forbes");
			db.insert(DATABASE_TABLE, null, cv);

			cv.put(KEY_SOURCE_CATEGORY, "entertainment");
			cv.put(KEY_SOURCE_LABEL, "9Gag");
			cv.put(KEY_SOURCE_URL, "http://www.9gag.com");
			cv.put(KEY_SOURCE_ICON, "news_ninegag");
			db.insert(DATABASE_TABLE, null, cv);

			cv.put(KEY_SOURCE_CATEGORY, "entertainment");
			cv.put(KEY_SOURCE_LABEL, "Funny or Die");
			cv.put(KEY_SOURCE_URL, "http://www.funnyordie.com");
			cv.put(KEY_SOURCE_ICON, "news_funnyordie");
			db.insert(DATABASE_TABLE, null, cv);

			cv.put(KEY_SOURCE_CATEGORY, "entertainment");
			cv.put(KEY_SOURCE_LABEL, "TED");
			cv.put(KEY_SOURCE_URL, "http://www.ted.com");
			cv.put(KEY_SOURCE_ICON, "news_ted");

			db.insert(DATABASE_TABLE, null, cv);

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);

		}

	}

	public NewsStorage(Context context) {
		mContext = context;
	}

	public NewsStorage open() throws SQLException {

		mHelper = new NewsDBHelper(mContext);
		mDatabase = mHelper.getWritableDatabase();

		return this;

	}

	public void close() {
		mHelper.close();
	}

	public long insertData(String category, String label, String url) {
		
		

		ContentValues cv = new ContentValues();
		try {

			cv.put(KEY_SOURCE_CATEGORY, category);
			cv.put(KEY_SOURCE_LABEL, label);
			cv.put(KEY_SOURCE_URL, url);
			cv.put(KEY_SOURCE_ICON, "news_newsite");

			Log.d("NewsStorage", "News Sources stored successfully!");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mDatabase.insert(DATABASE_TABLE, null, cv);

	}

	public void removeEntry(int rowIndex) {

		try {
			mDatabase.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowIndex, null);

			Log.w("NewsStorage", "DATABASE ---> News Record  " + rowIndex
					+ " successfully removed from DB");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Returns all the news sources of a certain categerory ie; Entertainment,
	// Sports, etc
	public void getSources(String cat) {

		String[] columns = { KEY_ROWID, KEY_SOURCE_CATEGORY, KEY_SOURCE_LABEL,
				KEY_SOURCE_URL };

		/*
		 * Cursor c = mDatabase.query(DATABASE_TABLE, columns, null, null, null,
		 * null, null);
		 * 
		 * int index_row = c.getColumnIndex(KEY_ROWID); int cat_column =
		 * c.getColumnIndex(KEY_SOURCE_CATEGORY); int label_column =
		 * c.getColumnIndex(KEY_SOURCE_LABEL); int source_column =
		 * c.getColumnIndex(KEY_SOURCE_URL);
		 */

		String query = "SELECT * FROM News_Sources WHERE category LIKE '%" + cat + "%'";

		Cursor cursor = mDatabase.rawQuery(query, null);
		
		
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			
			String label = cursor.getString(cursor
					.getColumnIndex(KEY_SOURCE_LABEL));
			mLabels.add(label);
			Log.w("NewsStorage",
					"LABELS RETURNED --> " + mLabels.toString());
			String url = cursor.getString(cursor
					.getColumnIndex(KEY_SOURCE_URL));
			mUrls.add(url);
			
			String icon = cursor.getString(cursor
					.getColumnIndex(KEY_SOURCE_ICON));
			
			
			mIcons .add(icon);
			
			
			Log.w("NewsStorage", "URLS RETURNED --> " + mUrls.toString());
			Log.w("NewsStorage", "ICONS RETURNED --> " + mIcons.toString());
			
			
			
		}
		
		cursor.close();
		
		

	/*	if (cursor.moveToFirst()) {
			while (cursor.isAfterLast() != true) {
				String label = cursor.getString(cursor
						.getColumnIndex(KEY_SOURCE_LABEL));
				mLabels.add(label);
				Log.w("NewsStorage",
						"LABELS RETURNED --> " + mLabels.toString());
				String url = cursor.getString(cursor
						.getColumnIndex(KEY_SOURCE_URL));
				Log.w("NewsStorage", "URLS RETURNED --> " + mUrls.toString());
				mUrls.add(url);
			}*/
			
			

		}
		
		

	

	public ArrayList<String> getLabels() {

		return mLabels;
	}

	public ArrayList<String> getUrls() {

		return mUrls;
	}
	
	public ArrayList<String> getIcons(){
		
		return mIcons;
	}

}

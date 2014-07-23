package com.brightr.weathermate.parsers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class TwitterFeedParser {

	ArrayList<String> mUserNames = new ArrayList<String>();
	ArrayList<String> mTweets = new ArrayList<String>();
	ArrayList<String> mTimes = new ArrayList<String>();

	HttpClient client = getThreadSafeClient();
	private HttpResponse response;
	private HttpEntity e;

	final static String URL = "http://api.twitter.com/1/statuses/user_timeline.json?screen_name=";

	public JSONObject getLastTweet(String username)
			throws ClientProtocolException, IOException, JSONException {

		StringBuilder url = new StringBuilder(URL);
		url.append(username);

		HttpGet get = new HttpGet(url.toString());
		response = client.execute(get);
		int status = response.getStatusLine().getStatusCode();

		if (status == 200) {

			e = response.getEntity();
			
			 
			 
			String data = EntityUtils.toString(e);

			JSONArray timeline = new JSONArray(data);

			// Get the last tweet from this user
			JSONObject last = timeline.getJSONObject(0);
			String text = last.getString("text");
			mTweets.add(text);

			String time = last.getString("created_at");
			Date date = new Date(time);
			DateFormat df = new SimpleDateFormat(
					"EEEE, MM-dd-yyyy, KK:mm a", Locale.US);
			String simpleTime = df.format(date);
			mTimes.add(simpleTime);

			//String name = last.getString("name");
			mUserNames.add("cnn");

			Log.w("TwitterFeedParser", "" + last.toString());
			Log.w("TwitterFeedParser", "Last Tweet -->" + text);
			Log.w("TwitterFeedParser", "Last Tweet Time -->" + time);
			//Log.w("TwitterFeedParser", "Last Tweet Username -->" + name);
			
			

		


			return last;
			
		}
		
		

		else
			if(e != null) {
			e.consumeContent(); 
		    client.getConnectionManager().shutdown();
			}
			return null;

	}

	public void consumeContent() {

		if (response.getEntity() != null) {
			try {
				response.getEntity().consumeContent();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

	}

	public static DefaultHttpClient getThreadSafeClient() {

		DefaultHttpClient client = new DefaultHttpClient();
		ClientConnectionManager mgr = client.getConnectionManager();
		HttpParams params = client.getParams();
		client = new DefaultHttpClient(new ThreadSafeClientConnManager(params,

		mgr.getSchemeRegistry()), params);
		return client;
	}

	public ArrayList<String> getTweets() {

		return mTweets;
	}

	public ArrayList<String> getTimes() {

		return mTimes;
	}

	public ArrayList<String> getUsernames() {

		return mUserNames;
	}

}

package com.brightr.weathermate.activities;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.brightr.weathermate.R;
import com.brightr.weathermate.adapters.TweetAdapter;
import com.brightr.weathermate.parsers.TwitterFeedParser;

import eu.erikw.PullToRefreshListView;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class SocialMediaActivity extends SherlockActivity {

	// ActionBar
	ActionBar actionBar;

	// Adapter
	TweetAdapter mTweetAdapter;

	eu.erikw.PullToRefreshListView socialList;
	ArrayAdapter<String> adapter;

	View v;
	TextView headerText;

	// Twitter parse
	TwitterFeedParser mTwitterFeed;

	String[] tweets = { "Tweet1", "Tweet2", "Tweet3", "Tweet4", "Tweet5",
			"Tweet6" };

	private ProgressDialog pd;

	private ArrayList<String> mTweets;
	private ArrayList<String> mTimes;

	private ArrayList<String> mUsernames;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// set up the action bar
		setContentView(R.layout.social_layout);

		socialList = (PullToRefreshListView) findViewById(R.id.SocialList);
		v = findViewById(R.id.SocialHeader);
		headerText = (TextView) findViewById(R.id.FlightNameHeader);
		headerText.setText("What's new on Twitter");

		// adapter = new ArrayAdapter<String>(this,
		// android.R.layout.simple_list_item_1, tweets);

		// socialList.setAdapter(adapter);

		initUserList();

		actionBar = getSupportActionBar();
		actionBar.setTitle("Social");
		actionBar.setIcon(R.drawable.gossip_birds);

		actionBar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.weathermate_actionbar_4));

		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);

		pd = new ProgressDialog(this);
		new GetTwitterFeed().execute();
	}

	private void initUserList() {

		mUsernames = new ArrayList<String>();
		mUsernames.add("cnn");
		mUsernames.add("yahoonews");
		mUsernames.add("abcnews");
		mUsernames.add("nytimes");
		mUsernames.add("cnetnews");
		mUsernames.add("engadget");
		mUsernames.add("reddit");
		mUsernames.add("rawstory");
		mUsernames.add("reuters");
		mUsernames.add("drudge_report");
		mUsernames.add("huffingtonpost");

		mUsernames.add("espn");
		mUsernames.add("yahoosports");
		mUsernames.add("cbssports");
		mUsernames.add("bleacherreport");
		mUsernames.add("sinow");

		mUsernames.add("youtube");
		mUsernames.add("collegehumor");
		mUsernames.add("forbes");
		mUsernames.add("9gag");
		mUsernames.add("funnyordie");
		mUsernames.add("ted_tweets");

	}

	private class GetTwitterFeed extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... arg0) {

			mTwitterFeed = new TwitterFeedParser();

			for (int i = 0; i < mUsernames.size(); i++) {

				try {
					mTwitterFeed.getLastTweet(mUsernames.get(i));

				} catch (ClientProtocolException e) {

					e.printStackTrace();
				} catch (IOException e) {

					e.printStackTrace();
				} catch (JSONException e) {

					e.printStackTrace();
				}

			}

			// mTwitterFeed.consumeContent();

			mTweets = mTwitterFeed.getTweets();
			mTimes = mTwitterFeed.getTimes();

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			dismissLoadingDialog();

			mTweetAdapter = new TweetAdapter(SocialMediaActivity.this, mTweets,
					mTimes);
			socialList.setAdapter(mTweetAdapter);

		}

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			showLoadingDialog();

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case android.R.id.home:
			super.onBackPressed();

			break;
		}
		return true;
	}

	private void showLoadingDialog() {

		pd.setTitle("Loading Tweets");
		pd.setMessage("Getting Twitter feed..");
		pd.show();

	}

	private void dismissLoadingDialog() {
		pd.dismiss();
	}

}

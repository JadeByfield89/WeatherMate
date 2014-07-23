package com.brightr.weathermate.fragments;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.brightr.weathermate.R;
import com.brightr.weathermate.activities.WebsiteViewActivity;
import com.brightr.weathermate.databases.NewsStorage;

public class GeneralNewsFragment extends Fragment implements
		OnItemClickListener {

	// Adapter for the gridview
	ImageAdapter gridAdapter;

	// GridView
	GridView gridView;

	final ArrayList<String> mUrls = new ArrayList<String>();

	private ArrayList<String> dbLabels = new ArrayList<String>();
	private ArrayList<String> dbUrls = new ArrayList<String>();
	private ArrayList<String> dbIcons = new ArrayList<String>();

	NewsStorage mStorage;

	View headerView;
	TextView headerText;

	private int menuItemIndex;

	private int context_saved_pos;

	private int database_position;

	private int grid_position;

	private ArrayList<Integer> icons = new ArrayList<Integer>();
	
	private SharedPreferences sharedPrefs;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.general_news_layout, container,
				false);

		gridView = (GridView) v.findViewById(R.id.gvGeneralNews);
		
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

		// Initialize database
		getSourcesFromDB();
		getWebsiteUrls();

		gridView.setOnItemClickListener(this);
		registerForContextMenu(gridView);

		headerView = (View) v.findViewById(R.id.NewsHeader);
		headerText = (TextView) headerView.findViewById(R.id.FlightNameHeader);
		headerText.setText("General");
		
		Log.d("GeneralNewsFragment", "OnCreate View");

		return v;
	}

	private void getSourcesFromDB() {
		mStorage = new NewsStorage(getActivity());
		mStorage.open();

		mStorage.getSources("general");
		dbLabels = mStorage.getLabels();
		Log.w("GeneralNewsFragment",
				"LABELS FROM DB --> " + dbLabels.toString());
		dbUrls = mStorage.getUrls();
		dbIcons = mStorage.getIcons();
		Log.w("GeneralNewsFragment", "ICONS FROM DB --> " + dbIcons.toString());
		gridAdapter = new ImageAdapter(getActivity(), dbLabels, dbUrls);
		gridView.setAdapter(gridAdapter);
		mStorage.close();

		// int current = dbIcons.get(2);

		// Convert the list of resourceIds from Strings stored in the DB to ints
		/*
		 * for(int i = 0; i < dbIcons.size(); i++){
		 * 
		 * String s = dbIcons.get(i);
		 * 
		 * 
		 * 
		 * String newS = s.replace("'", "");
		 * 
		 * 
		 * int current = Integer.valueOf(newS.toString()); icons.add(current);
		 * 
		 * 
		 * }
		 */

	}

	private void getWebsiteUrls() {

		mUrls.add("http://www.cnn.com");
		mUrls.add("http://news.yahoo.com/");
		mUrls.add("http://abcnews.go.com/");
		mUrls.add("http://www.nytimes.com/");
		mUrls.add("http://www.cnet.com/");
		mUrls.add("http://www.engadget.com/");
		mUrls.add("http://www.reddit.com/");

	}

	public class ImageAdapter extends BaseAdapter {

		private LayoutInflater inflater;
		private Context mContext;
		// private ArrayList<Integer> mIcons = new ArrayList<Integer>();
		private ArrayList<String> mLabels = new ArrayList<String>();
		private ArrayList<String> mUrls = new ArrayList<String>();

		public ImageAdapter(Context c, ArrayList<String> mLabels,
				ArrayList<String> mUrls) {
			this.mContext = c;
			this.mLabels = mLabels;
			this.mUrls = mUrls;

			/*
			 * mIcons.add(R.drawable.news_cnn);
			 * mIcons.add(R.drawable.news_yahoo);
			 * mIcons.add(R.drawable.news_abc);
			 * mIcons.add(R.drawable.news_nytimes);
			 * mIcons.add(R.drawable.news_cnet);
			 * mIcons.add(R.drawable.news_engadget);
			 * mIcons.add(R.drawable.news_reddit);
			 */

			/*
			 * mLabels.add("CNN"); mLabels.add("Yahoo News");
			 * mLabels.add("ABC News"); mLabels.add("NY Times");
			 * mLabels.add("CNET"); mLabels.add("Engadget");
			 * mLabels.add("Reddit");
			 */

			inflater = (LayoutInflater) c
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		}

		public int getCount() {
			return mLabels.size();
		}

		public Object getItem(int position) {
			return mLabels.get(position);
		}

		public long getItemId(int position) {
			return 0;
		}

		public class ViewHolder {

			ImageView icon;
			TextView label;
		}

		// create a new ImageView for each item referenced by the Adapter
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder;

			if (convertView == null) {

				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.news_grid_row, null);

				holder.icon = (ImageView) convertView
						.findViewById(R.id.ivNewsSiteImage);
				holder.label = (TextView) convertView
						.findViewById(R.id.tvNewsSiteName);

				convertView.setTag(holder);

			}

			else

				holder = (ViewHolder) convertView.getTag();
			// holder.icon.setBackgroundResource(mIcons.get(position));
			holder.label.setText(mLabels.get(position));
			
			int resId = getResources().getIdentifier(
					dbIcons.get(position), "drawable",
					getActivity().getPackageName());

			holder.icon.setImageResource(resId);
			
			//Toast.makeText(getActivity(), "resId is " + resId, Toast.LENGTH_SHORT).show();
			Log.w("GeneralNewsFragment", "ICON FROM DB IS --> " + dbIcons.get(position));

			return convertView;

		}

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {

		if (v.getId() == R.id.gvGeneralNews) {

			AdapterView.AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;

			menu.setHeaderTitle("Options");
			String[] menuItems = { "Share", "Delete" };
			for (int i = 0; i < menuItems.length; i++) {
				menu.add(Menu.NONE, i, i, menuItems[i]);
			}

		}
	}

	@Override
	public boolean onContextItemSelected(android.view.MenuItem item) {

		AdapterView.AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		menuItemIndex = item.getItemId();
		context_saved_pos = info.position;
		String[] menuItems = { "Share", "Delete" };
		String menuItemName = menuItems[menuItemIndex];
		// String

		switch (menuItemIndex) {

		case 0:

			shareWebsite();

			break;

		case 1:

			deleteWebsite();

			break;

		}

		return true;
	}

	// Add a new website source to this fragments gridview, also add it to the
	// database
	public void addNewSite(String label, String url) {

		final String category = "general";

		try {

			// Open the db
			mStorage.open();
			mStorage.insertData(category, label, url);
			dbLabels.add(label);
			dbUrls.add(url);
			dbIcons.add("news_newsite");
			gridAdapter.notifyDataSetChanged();

			mStorage.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void deleteWebsite() {
		// Delete this website from the gridview and from the database
		try {
			mStorage.open();
			mStorage.removeEntry(context_saved_pos + 1);
			this.dbLabels.remove(context_saved_pos);
			this.dbUrls.remove(context_saved_pos);
			this.dbIcons.remove(context_saved_pos);

			mStorage.close();

			gridAdapter.notifyDataSetChanged();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public ArrayList<String> getUrls() {

		return this.dbUrls;
	}

	private void shareWebsite() {

		try {
			// Share this website via the share intent

			String details = "Hey, check out this website!  "
					+ getUrls().get(grid_position);

			Intent shareIntent = new Intent(Intent.ACTION_SEND);
			shareIntent.setType("text/plain");
			shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, details);
			startActivity(Intent.createChooser(shareIntent, "Share Link"));
		} catch (Exception e) {
			Toast.makeText(getActivity(), "Error sharing url!",
					Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onItemClick(AdapterView<?> av, View v, int position, long id) {
		
		

		database_position = position + 1;
		grid_position = position;
		String url = dbUrls.get(position);
		
		boolean loadExternal = sharedPrefs.getBoolean("launchBrowser", false);
		
		if(loadExternal){
			
			//Start the external browser
			Intent external = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			startActivity(external);
		}
		
		else{
			
			Intent i = new Intent(getActivity(), WebsiteViewActivity.class);
			i.putExtra("key", url);
			startActivity(i);
			
		}
		

	}

}

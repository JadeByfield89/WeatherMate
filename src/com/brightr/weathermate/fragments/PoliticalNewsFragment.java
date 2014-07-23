package com.brightr.weathermate.fragments;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.brightr.weathermate.R;
import com.brightr.weathermate.activities.WebsiteViewActivity;
import com.brightr.weathermate.databases.NewsStorage;

public class PoliticalNewsFragment extends Fragment implements
		OnItemClickListener {

	private View headerView;
	private TextView headerText;

	// GridView
	GridView gridView;
	ImageAdapter gridAdapter;

	NewsStorage mStorage;
	private int menuItemIndex;
	private int context_saved_pos;
	private ArrayList<String> dbLabels;
	private ArrayList<String> dbUrls;
	private ArrayList<String> dbIcons;

	private SharedPreferences sharedPrefs;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.politics_news_layout, container,
				false);

		sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(getActivity());

		gridView = (GridView) v.findViewById(R.id.gvPoliticalNews);
		gridView.setOnItemClickListener(this);
		registerForContextMenu(gridView);
		// Initialize database
		getSourcesFromDB();

		headerView = (View) v.findViewById(R.id.NewsHeader);
		headerText = (TextView) headerView.findViewById(R.id.FlightNameHeader);
		headerText.setText("Politics");
		
		
		Log.d("PoliticalNewsFragment", "OnCreate View");
		return v;
	}

	private void getSourcesFromDB() {

		try {

			mStorage = new NewsStorage(getActivity());
			mStorage.open();

			mStorage.getSources("politics");
			dbLabels = mStorage.getLabels();
			dbUrls = mStorage.getUrls();
			dbIcons = mStorage.getIcons();

			gridAdapter = new ImageAdapter(getActivity(), dbLabels, dbUrls);
			gridView.setAdapter(gridAdapter);
			mStorage.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public class ImageAdapter extends BaseAdapter {

		private LayoutInflater inflater;
		private Context mContext;
		private ArrayList<Integer> mIcons = new ArrayList<Integer>();
		private ArrayList<String> mLabels = new ArrayList<String>();
		private ArrayList<String> mUrls = new ArrayList<String>();

		public ImageAdapter(Context c, ArrayList<String> dbLabels,
				ArrayList<String> dbUrls) {
			this.mContext = c;
			this.mLabels = dbLabels;
			this.mUrls = dbUrls;

			/*
			 * mIcons.add(R.drawable.news_politico);
			 * mIcons.add(R.drawable.news_rawstory);
			 * mIcons.add(R.drawable.news_reuters);
			 * mIcons.add(R.drawable.news_drudge);
			 * mIcons.add(R.drawable.news_huffingtonpost);
			 */

			inflater = (LayoutInflater) c
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		}

		public int getCount() {
			return mLabels.size();
		}

		public Object getItem(int position) {
			return mIcons.get(position);
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

			holder.label.setText(dbLabels.get(position));

			int resId = getResources().getIdentifier(dbIcons.get(position),
					"drawable", getActivity().getPackageName());

			holder.icon.setImageResource(resId);

			// Toast.makeText(getActivity(), "resId is " + resId,
			// Toast.LENGTH_SHORT).show();
			Log.w("PoliticsNewsFragment",
					"ICON FROM DB IS --> " + dbIcons.get(position));

			return convertView;

		}

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {

		if (v.getId() == R.id.gvPoliticalNews) {

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
			this.dbLabels.add(label);
			this.dbUrls.add(url);
			this.dbIcons.add("news_newsite");
			gridAdapter.notifyDataSetChanged();

			mStorage.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void deleteWebsite() {
		// Delete this website from the gridview and from the database
		// Delete this website from the gridview and from the database
		try {
			mStorage.open();
			mStorage.removeEntry(context_saved_pos + 1);
			this.dbLabels.remove(context_saved_pos);
			this.dbUrls.remove(context_saved_pos);
			this.dbIcons.remove(context_saved_pos);

			mStorage.close();

			Toast.makeText(getActivity(),
					" " + dbLabels.get(context_saved_pos), Toast.LENGTH_SHORT)
					.show();

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
					+ getUrls().get(context_saved_pos);

			Intent shareIntent = new Intent(Intent.ACTION_SEND);
			shareIntent.setType("text/plain");
			shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, details);
			startActivity(Intent.createChooser(shareIntent, "Share Link"));
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getActivity(), "Error sharing url!",
					Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onItemClick(AdapterView<?> av, View v, int position, long id) {

		String url = dbUrls.get(position);

		boolean loadExternal = sharedPrefs.getBoolean("launchBrowser", false);

		if (loadExternal) {

			// Start the external browser
			Intent external = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			startActivity(external);
		}

		else {

			Intent i = new Intent(getActivity(), WebsiteViewActivity.class);
			i.putExtra("key", url);
			startActivity(i);

		}
	}

}

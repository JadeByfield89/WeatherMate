package com.brightr.weathermate.activities;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.brightr.weathermate.R;

import android.graphics.Color;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.preference.PreferenceCategory;

public class SettingsActivity extends SherlockPreferenceActivity {

	ActionBar actionBar;
	private PreferenceCategory weather;
	private PreferenceCategory flights;
	private PreferenceCategory news;

	//PreferenceCategory weather = new PreferenceCategory(this);
	//PreferenceCategory flights = new PreferenceCategory(this);
	//PreferenceCategory news = new PreferenceCategory(this);
	
	DialogPreference dialogPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		weather = new PreferenceCategory(this);
		flights = new PreferenceCategory(this);
		news = new PreferenceCategory(this);

		actionBar = getSupportActionBar();
		actionBar.setTitle("Settings");
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setIcon(getResources().getDrawable(R.drawable.icon_settings));
		actionBar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.weathermate_actionbar_4));

		// Setting the list's background to be white
		findViewById(android.R.id.list).setBackgroundResource(
				R.drawable.weathermate_wp1);

		addPreferencesFromResource(R.xml.prerences);
		// getListView().setBackgroundResource(android.R.color.transparent);
		getListView().setCacheColorHint(Color.TRANSPARENT);
		getListView().setScrollingCacheEnabled(false);
		getListView().setSelector(R.drawable.grid_selector);
		getListView().setDivider(getResources().getDrawable(R.drawable.layout_color));
		getListView().setDividerHeight(10);
		
		dialogPref = (DialogPreference) findPreference("dialogPref");
		
		
		

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getSupportMenuInflater();
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		switch (item.getItemId()) {

		case android.R.id.home:
			super.onBackPressed();

			break;
		}

		return super.onOptionsItemSelected(item);
	}

}

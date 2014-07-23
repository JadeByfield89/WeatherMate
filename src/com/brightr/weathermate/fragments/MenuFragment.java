package com.brightr.weathermate.fragments;

import com.brightr.weathermate.R;
import com.brightr.weathermate.activities.LocationMapview;
import com.brightr.weathermate.activities.MyFlightsActivity;
import com.brightr.weathermate.adapters.MenuAdapter;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

public class MenuFragment extends Fragment implements OnItemClickListener {

	ListView menu;
	String[] dataSet = { "Menu Item 1", "Menu Item 2", "Menu Item 3",
			"Menu Item 4", "Menu Item 5" };

	static View v;
	GridView optionsGrid;
	MenuAdapter adapter;

	onMenuItemSelectedListener mListener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		v = inflater.inflate(R.layout.menu_fragment, container, false);
		optionsGrid = (GridView) v.findViewById(R.id.menuGridView);
		// centerGridView();
		adapter = new MenuAdapter(getActivity());

		optionsGrid.setAdapter(adapter);
		optionsGrid.setOnItemClickListener(this);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
		// setRetainInstance(true);
	}

	public MenuFragment() {

	}

	// Interface that will notify the Activity that a menu item has been
	// selected
	public interface onMenuItemSelectedListener {

		public void onMenuItemSelected(int position);

	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);

		try {
			mListener = (onMenuItemSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnMenuItemSelectedListener");
		}
	}

	@Override
	public void setRetainInstance(boolean retain) {
		// TODO Auto-generated method stub
		super.setRetainInstance(true);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		// setRetainInstance(true);
	}

	public View getRoot() {
		return v;
	}

	public GridView getGridView() {

		return optionsGrid;
	}

	@Override
	public void onItemClick(AdapterView<?> av, View v, int pos, long id) {

		//Notify the host Activity that a menu item was selected, passing in the position
		mListener.onMenuItemSelected(pos);

	}

}

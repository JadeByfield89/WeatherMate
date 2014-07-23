package com.brightr.weathermate.adapters;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

public class MyPagerAdapter extends FragmentStatePagerAdapter {

	List<Fragment> fragments;

	public MyPagerAdapter(FragmentManager fm, List<Fragment> fragments) {

		super(fm);
		this.fragments = fragments;

	}

	@Override
	public Fragment getItem(int position) {

		if (position == 0)
			return fragments.get(0);
		else if (position == 1)
			return fragments.get(1);
		else if (position == 2)
			return fragments.get(2);
		else if (position == 3)
			return fragments.get(3);

		return this.fragments.get(position);
	}

	@Override
	public int getCount() {

		return this.fragments.size();
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {

		super.destroyItem(container, position, object);
	}

}

package com.brightr.weathermate.views;

import com.brightr.weathermate.R;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceCategory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CustomPreferenceCategory extends PreferenceCategory {

	public CustomPreferenceCategory(Context context) {
		super(context);

	}

	public CustomPreferenceCategory(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public CustomPreferenceCategory(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);

	}

	@Override
	protected void onBindView(View view) {
		super.onBindView(view);
		TextView titleView = (TextView) view.findViewById(android.R.id.title);

		Drawable background = getContext().getResources().getDrawable(
				R.drawable.header_non_rounded);

		// titleView.setBackgroundResource(R.drawable.header_non_rounded);
		titleView.setBackgroundDrawable(background);
		// titleView.setBackgroundResource(R.drawable.color_yellow);
		titleView.setTextColor(Color.WHITE);
		

	}

	/*@Override
	protected View onCreateView(ViewGroup parent) {
		// And it's just a TextView!
		super.onCreateView(parent);

		TextView titleView = (TextView) parent.findViewById(android.R.id.title);

		titleView.setTextColor(Color.WHITE);

		return titleView;
	}*/

}

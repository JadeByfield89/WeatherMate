package com.brightr.weathermate.adapters;

import com.brightr.weathermate.R;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class NewsPagerAdapter extends PagerAdapter {

	@Override
	public int getCount() {
		
		return 4;
	}

	@Override
	public boolean isViewFromObject(View collection, Object arg1) {
		
		return collection == ((View)arg1);
	}

	
	@Override
	public Object instantiateItem(View v, int position) {
		
		 LayoutInflater inflater = (LayoutInflater) v.getContext()
                 .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		
		int resId = 0;
        switch (position) {
        case 0:
            resId = R.layout.general_news_layout;
            
            //inflate gridview and set its adapter
            break;
        case 1:
            resId = R.layout.politics_news_layout;
            break;
            
        case 2:
        	
        	resId = R.layout.sports_news_layout;
        	break;
        	
        case 3:
        	
        	resId = R.layout.entertainment_news_layout;
        	break;
        }
        View view = inflater.inflate(resId, null);
        
        
        if (resId == R.layout.general_news_layout) {
            View headerView = (View)view.findViewById(R.id.NewsHeader);
            TextView headerText = (TextView) headerView.findViewById(R.id.FlightNameHeader);
            headerText.setText("General");
            
        }
        
        if (resId == R.layout.politics_news_layout) {
            View headerView = (View)view.findViewById(R.id.NewsHeader);
            TextView headerText = (TextView) headerView.findViewById(R.id.FlightNameHeader);
            headerText.setText("Politics");
            
        }
        
        if (resId == R.layout.sports_news_layout) {
            View headerView = (View)view.findViewById(R.id.NewsHeader);
            TextView headerText = (TextView) headerView.findViewById(R.id.FlightNameHeader);
            headerText.setText("Sports");
            
        }
        
        if (resId == R.layout.entertainment_news_layout) {
            View headerView = (View)view.findViewById(R.id.NewsHeader);
            TextView headerText = (TextView) headerView.findViewById(R.id.FlightNameHeader);
            headerText.setText("Entertainment");
            
        }
        ((ViewPager) v).addView(view, 0);
        return view;
		
		
		
	}

	
	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		
		 ((ViewPager) arg0).removeView((View) arg2);
	}
	
	

}

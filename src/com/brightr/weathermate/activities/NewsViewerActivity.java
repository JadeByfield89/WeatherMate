
package com.brightr.weathermate.activities;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.brightr.weathermate.R;
import com.brightr.weathermate.adapters.MyPagerAdapter;
import com.brightr.weathermate.adapters.NewsPagerAdapter;
import com.brightr.weathermate.fragments.EntertainmentNewsFragment;
import com.brightr.weathermate.fragments.GeneralNewsFragment;
import com.brightr.weathermate.fragments.PoliticalNewsFragment;
import com.brightr.weathermate.fragments.SportsNewsFragment;
import com.viewpagerindicator.CirclePageIndicator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;
import java.util.Vector;

public class NewsViewerActivity extends SherlockFragmentActivity implements OnPageChangeListener {

    // ActionBar
    ActionBar actionBar;

    // ViewPager
    ViewPager pager;

    // Pager Adapter
    NewsPagerAdapter pagerAdapter;

    MyPagerAdapter mPagerAdapter;

    GeneralNewsFragment generalFrag = new GeneralNewsFragment();

    PoliticalNewsFragment politicalFrag = new PoliticalNewsFragment();

    SportsNewsFragment sportsFragment = new SportsNewsFragment();

    EntertainmentNewsFragment entertainFrag = new EntertainmentNewsFragment();

    private String label;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_viewer_layout);

        // Set up the action bar
        actionBar = getSupportActionBar();
        actionBar.setTitle("News");
        actionBar.setIcon(getResources().getDrawable(R.drawable.news));
        actionBar.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.weathermate_actionbar_4));

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // set up the viewpager
        /*
         * newsPager = (ViewPager)findViewById(R.id.vpNewsPager); pagerAdapter =
         * new NewsPagerAdapter(); newsPager.setAdapter(pagerAdapter);
         * newsPager.setCurrentItem(0);
         */

        initializePaging();

    }

    private void initializePaging() {

        List<Fragment> fragments = new Vector<Fragment>();
        /*
         * fragments.add(Fragment.instantiate(this,
         * GeneralNewsFragment.class.getName()));
         * fragments.add(Fragment.instantiate(this,
         * PoliticalNewsFragment.class.getName()));
         * fragments.add(Fragment.instantiate(this,
         * SportsNewsFragment.class.getName()));
         * fragments.add(Fragment.instantiate(this,
         * EntertainmentNewsFragment.class.getName()));
         */
        fragments.add(generalFrag);
        fragments.add(politicalFrag);
        fragments.add(sportsFragment);
        fragments.add(entertainFrag);

        this.mPagerAdapter = new MyPagerAdapter(super.getSupportFragmentManager(), fragments);
        //

        pager = (ViewPager)super.findViewById(R.id.vpNewsPager);
        pager.setAdapter(this.mPagerAdapter);
        pager.setCurrentItem(0, true);
        pager.setOnPageChangeListener(this);
        pager.setOffscreenPageLimit(4);

        CirclePageIndicator titleIndicator = (CirclePageIndicator)findViewById(R.id.titles);
        titleIndicator.setViewPager(pager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.news_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*
     * (non-Javadoc)
     * @see com.actionbarsherlock.app.SherlockActivity#onMenuItemSelected(int,
     * com.actionbarsherlock.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.add_site:

                // Show add site dialog
                showAddSiteDialog();

                break;

            case android.R.id.home:

                super.onBackPressed();

                break;

        }

        return true;

    }

    private void showAddSiteDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.new_source_dialog, null);

        final EditText siteLabel = (EditText)v.findViewById(R.id.etNewsLabel);
        final EditText siteUrl = (EditText)v.findViewById(R.id.etNewsUrl);
        builder.setView(v);
        builder.setTitle("Add Site");
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // add the site and display it in the proper panel
                label = siteLabel.getText().toString();
                url = siteUrl.getText().toString();

                // A little string checking to ensure proper input

                if (label.equals(" ")) {
                    Toast.makeText(NewsViewerActivity.this, "Label cannot be empty",
                            Toast.LENGTH_SHORT).show();
                }

                if (url.equals(" ")) {

                    Toast.makeText(NewsViewerActivity.this, "Url cannot be empty",
                            Toast.LENGTH_SHORT).show();

                }

                if (url.contains(" ")) {

                    Toast.makeText(NewsViewerActivity.this, "Url cannot contain any spaces",
                            Toast.LENGTH_SHORT).show();
                }

                else {

                    checkCurrentFragment();

                }
            }

        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        builder.show();

    }

    // Check which fragment is currently in view
    private void checkCurrentFragment() {
        switch (pager.getCurrentItem()) {

            case 0:

                generalFrag.addNewSite(label, url);

                break;

            case 1:

                // Toast.makeText(NewsViewerActivity.this,
                // "Not on main fragment!",
                // Toast.LENGTH_SHORT).show();

                politicalFrag.addNewSite(label, url);

                break;

            case 2:

                sportsFragment.addNewSite(label, url);

                break;

            case 3:

                entertainFrag.addNewSite(label, url);

                break;

        }

    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageSelected(int arg0) {
        // TODO Auto-generated method stub

    }

}

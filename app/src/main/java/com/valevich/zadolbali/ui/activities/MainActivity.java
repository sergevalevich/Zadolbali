package com.valevich.zadolbali.ui.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.valevich.zadolbali.R;
import com.valevich.zadolbali.adapters.SectionsPagerAdapter;


import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

@OptionsMenu(R.menu.main_menu)
@EActivity
public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String TOOLBAR_TITLE_KEY = "TOOLBAR_TITLE";
    private static final String TAG = MainActivity.class.getSimpleName();

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;
    @OptionsMenuItem(R.id.action_settings)
    MenuItem mSettingsMenuItem;

    @ViewById(R.id.container)
    ViewPager mViewPager;

    @ViewById(R.id.tabLayout)
    TabLayout mTabLayout;

    @StringRes(R.string.tab_stories)
    String mAllStoriesTabTitle;

    @StringRes(R.string.tab_favorite)
    String mFavoriteTabTitle;

    @StringRes(R.string.app_name)
    String mAppName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    private void setupTabs() {
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this);
        mViewPager.setAdapter(sectionsPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        setTitle(mAllStoriesTabTitle);
                        break;
                    case 1:
                        setTitle(mFavoriteTabTitle);
                        break;
                    default:
                        setTitle(mAppName);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @AfterViews
    void setupViews() {
        setupActionBar();
        setupTabs();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(TOOLBAR_TITLE_KEY, String.valueOf(getTitle()));
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String toolBarTitle = savedInstanceState.getString(TOOLBAR_TITLE_KEY,getString(R.string.app_name));
        setTitle(toolBarTitle);
    }

    @Override
    public void onBackPressed() {
        if(mViewPager.getCurrentItem() == 1) {
            mViewPager.setCurrentItem(0);
        } else {
            super.onBackPressed();
        }
    }

    private void setupActionBar() {
        setSupportActionBar(mToolbar);
        setTitle(mAllStoriesTabTitle);
    }

}

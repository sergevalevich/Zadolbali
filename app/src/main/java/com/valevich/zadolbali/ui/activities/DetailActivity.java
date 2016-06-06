package com.valevich.zadolbali.ui.activities;


import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Intent;
import android.content.Loader;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.valevich.zadolbali.R;
import com.valevich.zadolbali.adapters.StorySlidePagerAdapter;
import com.valevich.zadolbali.database.data.StoryEntry;
import com.valevich.zadolbali.network.RestClient;
import com.valevich.zadolbali.ui.StoryActionHandler;
import com.valevich.zadolbali.utils.StarActionProvider;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity(R.layout.activity_detail)
public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();
    private static final String KEY_CURRENT_PAGE = "PAGE";

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.pager)
    ViewPager mPager;

    private PagerAdapter mPagerAdapter;

    @Extra(value = "story_number")
    int mStoryNumber;

    @Extra(value = "stories_flag")
    int mFlag;

    private List<StoryEntry> mStories;

    @AfterViews
    void setupViews() {
        setupActionBar();
    }

    @AfterExtras
    void setupPage() {
        loadStories();
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    private void setupActionBar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            setTitle("");
        }
    }

    @UiThread
    void setupViewPager(List<StoryEntry> stories) {
        mPagerAdapter = new StorySlidePagerAdapter(getSupportFragmentManager(),stories);
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(mStoryNumber);
    }

    @Background
    void loadStories() {
        if(mFlag == StoryActionHandler.ONLY_FAVORITE_FLAG) {
            mStories = StoryEntry.getAllFavoriteStories("");
        } else {
            mStories = StoryEntry.getAllStories("");
        }
        setupViewPager(mStories);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt(KEY_CURRENT_PAGE,mPager.getCurrentItem());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        mStoryNumber = savedInstanceState.getInt(KEY_CURRENT_PAGE,0);
        mPager.setCurrentItem(mStoryNumber);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}

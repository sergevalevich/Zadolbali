package com.valevich.zadolbali.ui.activities;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.valevich.zadolbali.R;
import com.valevich.zadolbali.ui.fragments.FavoriteFragment;
import com.valevich.zadolbali.ui.fragments.FavoriteFragment_;
import com.valevich.zadolbali.ui.fragments.SettingsFragment_;
import com.valevich.zadolbali.ui.fragments.StoriesFragment;
import com.valevich.zadolbali.ui.fragments.StoriesFragment_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
@EActivity
public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String TOOLBAR_TITLE_KEY = "TOOLBAR_TITLE";

    @ViewById(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @ViewById(R.id.toolbar)
    Toolbar mToolbar;
    @ViewById(R.id.navigation_view)
    NavigationView mNavigationView;

    private ActionBarDrawerToggle mToggle;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null) {
            replaceFragment(new StoriesFragment_());
        }

    }

    @AfterViews
    void setupViews() {
        setupActionBar();
        setupDrawerLayout();
        setupFragmentManager();
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
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (mFragmentManager.getBackStackEntryCount() == 1) {
            finish();
        } else {
            super.onBackPressed();
        }

    }


    private void setupNavigationContent(final NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if(mDrawerLayout != null) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                }
                int itemId = item.getItemId();
                switch (itemId) {
                    case R.id.drawer_stories:
                        replaceFragment(new StoriesFragment_());
                        break;
                    case R.id.drawer_favorite:
                        replaceFragment(new FavoriteFragment_());
                        break;
                    case R.id.drawer_settings:
                        replaceFragment(new SettingsFragment_());
                        break;
                }
                return true;
            }
        });
    }


    private void changeToolbarTitle(String backStackEntryName) {
        if(backStackEntryName.equals(StoriesFragment_.class.getName())) {
            setTitle(getString(R.string.nav_drawer_stories));
            mNavigationView.setCheckedItem(R.id.drawer_stories);
        } else if(backStackEntryName.equals(FavoriteFragment_.class.getName())) {
            setTitle(getString(R.string.nav_drawer_favorite));
            mNavigationView.setCheckedItem(R.id.drawer_favorite);
        } else {
            setTitle(getString(R.string.nav_drawer_settings));
            mNavigationView.setCheckedItem(R.id.drawer_settings);
        }
    }


    private void setupDrawerLayout() {
        setupNavigationContent(mNavigationView);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout
                ,mToolbar
                ,R.string.navigation_drawer_open
                ,R.string.navigation_drawer_close);
        mToggle.syncState();
        mDrawerLayout.addDrawerListener(mToggle);
        setTitle(R.string.app_name);
    }

    private void setupActionBar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void replaceFragment(Fragment fragment) {
        String backStackName = fragment.getClass().getName();

        boolean isFragmentPopped = mFragmentManager.popBackStackImmediate(backStackName,0);

        if(!isFragmentPopped && mFragmentManager.findFragmentByTag(backStackName) == null) {

            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            transaction.replace(R.id.main_container,fragment,backStackName);
            transaction.addToBackStack(backStackName);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.commit();

        }
    }

    private void setupFragmentManager() {
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.addOnBackStackChangedListener(this);
    }

    @Override
    public void onBackStackChanged() {

        Fragment f = mFragmentManager
                .findFragmentById(R.id.main_container);

        if(f != null) {
            changeToolbarTitle(f.getClass().getName());
        }

    }

}

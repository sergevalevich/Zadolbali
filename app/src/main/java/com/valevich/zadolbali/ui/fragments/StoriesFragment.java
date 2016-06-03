package com.valevich.zadolbali.ui.fragments;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.valevich.zadolbali.R;
import com.valevich.zadolbali.adapters.StoryAdapter;
import com.valevich.zadolbali.database.data.StoryEntry;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.api.BackgroundExecutor;

import java.util.List;

@OptionsMenu(R.menu.search_menu)
@EFragment(R.layout.fragment_stories)
public class StoriesFragment extends Fragment {

    private static final String SEARCH_ID = "search_id";
    @ViewById(R.id.story_list)
    RecyclerView mStoryList;
    @ViewById(R.id.coordinator)
    CoordinatorLayout mRootLayout;
    @OptionsMenuItem(R.id.action_search)
    MenuItem mSearchMenuItem;
    @StringRes(R.string.search_hint)
    String mSearchHint;

    private static final int STORY_LOADER = 0;

    @AfterViews
    void setupViews() {
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        mStoryList.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onResume() {
        super.onResume();
        loadStories("");
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        SearchView searchView = (SearchView) mSearchMenuItem.getActionView();


        searchView.setQueryHint(mSearchHint);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                BackgroundExecutor.cancelAll(SEARCH_ID,true);
                queryStories(newText);
                return false;
            }
        });
    }

    private void loadStories(final String filter) {
        getLoaderManager().restartLoader(STORY_LOADER, null, new LoaderManager.LoaderCallbacks<List<StoryEntry>>() {

            @Override
            public Loader<List<StoryEntry>> onCreateLoader(int id, Bundle args) {
                final AsyncTaskLoader<List<StoryEntry>> loader = new AsyncTaskLoader<List<StoryEntry>>(getActivity()) {
                    @Override
                    public List<StoryEntry> loadInBackground() {
                        return StoryEntry.getAllStories(filter);
                    }
                };
                loader.forceLoad();
                return loader;
            }

            @Override
            public void onLoadFinished(Loader<List<StoryEntry>> loader, List<StoryEntry> data) {
                StoryAdapter adapter = (StoryAdapter) mStoryList.getAdapter();
                if(adapter == null) {
                    mStoryList.setAdapter(new StoryAdapter(data));
                } else {
                    adapter.refresh(data);
                }
            }

            @Override
            public void onLoaderReset(Loader<List<StoryEntry>> loader) {

            }
        });

    }

    @Background(delay = 700, id = SEARCH_ID)
    void queryStories(String filter) {
        loadStories(filter);
    }

}
/*
    @ColorRes(R.color.colorPrimary)
    int mPrimaryColor;

    private void customizeSearchViewOld(SearchView searchView) {
        int searchPlateId = searchView.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        View searchPlateView = searchView.findViewById(searchPlateId);

        if (searchPlateView != null) {
            searchPlateView.setBackgroundColor(mPrimaryColor);
        }

        int searchImgId = getResources().getIdentifier("android:id/search_button", null, null);
        ImageView search = (ImageView) searchView.findViewById(searchImgId);

        if(search != null) {
            search.setImageResource(R.drawable.ic_action_search);
        }

        int closeImgId = getResources().getIdentifier("android:id/search_close_btn", null, null);
        ImageView close = (ImageView) searchView.findViewById(closeImgId);

        if(close != null) {
            close.setImageResource(R.drawable.ic_clear);
            close.setAlpha(0.4f);
        }

    }

}
 */

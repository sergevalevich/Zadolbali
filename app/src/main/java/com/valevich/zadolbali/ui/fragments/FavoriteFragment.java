package com.valevich.zadolbali.ui.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import com.valevich.zadolbali.R;
import com.valevich.zadolbali.adapters.FavoriteStoryAdapter;
import com.valevich.zadolbali.database.data.StoryEntry;
import com.valevich.zadolbali.eventbus.RemovedStoryEvent;
import com.valevich.zadolbali.network.RestClient;
import com.valevich.zadolbali.utils.Constants;
import com.valevich.zadolbali.utils.IVisible;
import com.valevich.zadolbali.utils.StoryActionHandler;
import com.valevich.zadolbali.ui.activities.DetailActivity_;
import com.valevich.zadolbali.utils.StoryTouchHelper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorRes;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.api.BackgroundExecutor;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * Created by NotePad.by on 03.06.2016.
 */
@OptionsMenu(R.menu.search_menu)
@EFragment(R.layout.fragment_favorite)
public class FavoriteFragment extends Fragment implements StoryActionHandler,
        IVisible{

    private static final String TAG = FavoriteFragment.class.getSimpleName();

    @OptionsMenuItem(R.id.action_search)
    MenuItem mSearchMenuItem;

    @StringRes(R.string.search_hint)
    String mSearchHint;

    @StringRes(R.string.share_dialog_msg)
    String mShareDialogMessage;

    @ViewById(R.id.favorite_story_list)
    RecyclerView mStoryList;

    @ColorRes(R.color.colorPrimary)
    int mPrimaryColor;

    private StoryTouchHelper mStoryTouchHelper;

    private static final String SEARCH_ID = "search_id";

    private static final int FAVORITE_STORY_LOADER = 1;

    @AfterViews
    void setupViews() {
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        mStoryList.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        restartLoader();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        SearchView searchView = (SearchView) mSearchMenuItem.getActionView();

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            customizeSearchViewOld(searchView);
        }

        searchView.setQueryHint(mSearchHint);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                BackgroundExecutor.cancelAll(SEARCH_ID, true);
                queryStories(newText);
                return false;
            }
        });
    }

    @UiThread
    void restartLoader() {
        loadStories("");
    }

    @Background(delay = 700, id = SEARCH_ID)
    void queryStories(String filter) {
        loadStories(filter);
    }

    private void loadStories(final String filter) {
        getLoaderManager().restartLoader(FAVORITE_STORY_LOADER, null, new LoaderManager.LoaderCallbacks<List<StoryEntry>>() {

            @Override
            public Loader<List<StoryEntry>> onCreateLoader(int id, Bundle args) {

                final AsyncTaskLoader<List<StoryEntry>> loader = new AsyncTaskLoader<List<StoryEntry>>(getActivity()) {
                    @Override
                    public List<StoryEntry> loadInBackground() {
                        return StoryEntry.getAllFavoriteStories(filter);
                    }
                };
                loader.forceLoad();
                return loader;

            }

            @Override
            public void onLoadFinished(Loader<List<StoryEntry>> loader, List<StoryEntry> data) {
                FavoriteStoryAdapter adapter = (FavoriteStoryAdapter) mStoryList.getAdapter();
                if (adapter == null) {
                    FavoriteStoryAdapter storyAdapter = new FavoriteStoryAdapter(data,FavoriteFragment.this);
                    mStoryList.setAdapter(storyAdapter);
                    mStoryTouchHelper = new StoryTouchHelper(storyAdapter,getActivity());
                    ItemTouchHelper.Callback callback = mStoryTouchHelper;
                    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
                    itemTouchHelper.attachToRecyclerView(mStoryList);
                } else {
                    adapter.refresh(data);
                }
            }

            @Override
            public void onLoaderReset(Loader<List<StoryEntry>> loader) {

            }
        });
    }

    private void customizeSearchViewOld(SearchView searchView) {
        int searchPlateId = searchView.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        View searchPlateView = searchView.findViewById(searchPlateId);

        if (searchPlateView != null) {
            searchPlateView.setBackgroundColor(mPrimaryColor);
        }

        int searchImgId = getResources().getIdentifier("android:id/search_button", null, null);
        ImageView search = (ImageView) searchView.findViewById(searchImgId);

        if (search != null) {
            search.setImageResource(R.drawable.ic_action_search);
        }

        int closeImgId = getResources().getIdentifier("android:id/search_close_btn", null, null);
        ImageView close = (ImageView) searchView.findViewById(closeImgId);

        if (close != null) {
            close.setImageResource(R.drawable.ic_clear);
            close.setAlpha(0.4f);
        }

    }

    @Override
    public void share(StoryEntry story) {
        Intent shareIntent = createShareIntent(RestClient.BASE_URL + story.getLink());
        startActivity(Intent.createChooser(shareIntent,mShareDialogMessage));
    }

    @Override
    public void more(int position, int flag) {
        DetailActivity_.intent(getActivity())
                .extra(Constants.KEY_INTENT_STORY_NUMBER,position)
                .extra(Constants.KEY_INTENT_STORY_FLAG,flag)
                .start();
    }

    @Override
    public void addToFavorite(StoryEntry story) {

    }

    private Intent createShareIntent(String storyText) {
        Intent myShareIntent = new Intent(Intent.ACTION_SEND);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            myShareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        } else {
            myShareIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        }
        myShareIntent.setType("text/plain");
        myShareIntent.putExtra(Intent.EXTRA_TEXT,storyText);
        return myShareIntent;
    }

    @Subscribe
    public void onStoryRemoved(RemovedStoryEvent event) {
        restartLoader();
    }

    @Override
    public void onVisible() {
        restartLoader();
    }
}

package com.valevich.zadolbali.ui.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;
import com.valevich.zadolbali.R;
import com.valevich.zadolbali.adapters.StoryAdapter;
import com.valevich.zadolbali.database.ZadolbaliDatabase;
import com.valevich.zadolbali.database.data.StoryEntry;
import com.valevich.zadolbali.network.RestClient;
import com.valevich.zadolbali.network.RestService;
import com.valevich.zadolbali.network.model.Story;
import com.valevich.zadolbali.ui.StoryActionHandler;
import com.valevich.zadolbali.ui.activities.DetailActivity_;
import com.valevich.zadolbali.utils.NetworkStatusChecker;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorRes;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.api.BackgroundExecutor;

import java.util.List;

@OptionsMenu(R.menu.search_menu)
@EFragment(R.layout.fragment_stories)
public class StoriesFragment extends Fragment implements StoryActionHandler {

    @ViewById(R.id.coordinator)
    CoordinatorLayout mRootLayout;

    @OptionsMenuItem(R.id.action_search)
    MenuItem mSearchMenuItem;

    @StringRes(R.string.search_hint)
    String mSearchHint;

    @StringRes(R.string.network_unavailable_msg)
    String mNetworkUnavailableMessage;

    @StringRes(R.string.share_dialog_msg)
    String mShareDialogMessage;

    @ColorRes(R.color.colorPrimary)
    int mPrimaryColor;

    @Bean
    NetworkStatusChecker mNetworkStatusChecker;

    private static final String SEARCH_ID = "search_id";

    private static final int STORY_LOADER = 0;

    @ViewById(R.id.story_list)
    RecyclerView mStoryList;

    @ViewById(R.id.swiperefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bean
    RestService mRestService;

    @AfterViews
    void setupViews() {
        setUpRecyclerView();
        setUpRefresh();
    }

    private void setUpRecyclerView() {
        mStoryList.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void setUpRefresh() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshStories();
            }
        });
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.colorAccent);
    }

    @Override
    public void onResume() {
        super.onResume();
        restartLoader();
        refreshStories();
    }

    private void refreshStories() {
        if (mNetworkStatusChecker.isNetworkAvailable()) {
            downloadStories();
        } else {
            notifyUser(mNetworkUnavailableMessage);
            if(mSwipeRefreshLayout.isRefreshing()) mSwipeRefreshLayout.setRefreshing(false);
        }
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

    @Background
    void downloadStories() {
        List<Story> stories = mRestService.getStories();
        saveStories(stories);
    }

    @UiThread
    void restartLoader() {
        loadStories("");
    }

    @Background(delay = 700, id = SEARCH_ID)
    void queryStories(String filter) {
        loadStories(filter);
    }

    private void saveStories(final List<Story> downloadedStories) {
        StoryEntry[] stories = new StoryEntry[downloadedStories.size()];

        DatabaseDefinition database = FlowManager.getDatabase(ZadolbaliDatabase.class);

        ProcessModelTransaction<StoryEntry> processModelTransaction =
                new ProcessModelTransaction.Builder<>(new ProcessModelTransaction.ProcessModel<StoryEntry>() {
                    @Override
                    public void processModel(StoryEntry story) {

                    }
                }).processListener(new ProcessModelTransaction.OnModelProcessListener<StoryEntry>() {
                    @Override
                    public void onModelProcessed(long current, long total, StoryEntry story) {
                        story = new StoryEntry();
                        Story downloadedStory = downloadedStories.get((int) current);

                        String description = Html.fromHtml(downloadedStory.getElementPureHtml()).toString();
                        String link = downloadedStory.getLink();
                        String source = downloadedStory.getSite();

                        story.setDescription(description);
                        story.setIsFavourite(0);
                        story.setIsRead(0);
                        story.setLink(link);
                        story.setSource(source);

                        story.save();
                    }
                }).addAll(stories).build();

        Transaction transaction = database.beginTransactionAsync(processModelTransaction)
                .success(new Transaction.Success() {
                    @Override
                    public void onSuccess(Transaction transaction) {
                        restartLoader();
                    }
                })
                .error(new Transaction.Error() {
                    @Override
                    public void onError(Transaction transaction, Throwable error) {

                    }
                })
                .build();

        transaction.execute();

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
                if(mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                StoryAdapter adapter = (StoryAdapter) mStoryList.getAdapter();
                if (adapter == null) {
                    StoryAdapter storyAdapter = new StoryAdapter(data,StoriesFragment.this);
                    mStoryList.setAdapter(storyAdapter);
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
                .extra("story_number",position)
                .extra("stories_flag",flag)
                .start();
    }

    @Override
    public void addToFavorite(StoryEntry story) {

        DatabaseDefinition database = FlowManager.getDatabase(ZadolbaliDatabase.class);

        ProcessModelTransaction<StoryEntry> processModelTransaction =
                new ProcessModelTransaction.Builder<>(new ProcessModelTransaction.ProcessModel<StoryEntry>() {
                    @Override
                    public void processModel(StoryEntry story) {
                        if(story.getIsFavourite() == 0) story.setIsFavourite(1);
                        else story.setIsFavourite(0);
                        story.save();
                    }
                }).processListener(new ProcessModelTransaction.OnModelProcessListener<StoryEntry>() {
                    @Override
                    public void onModelProcessed(long current, long total, StoryEntry story) {

                    }
                }).addAll(story).build();

        Transaction transaction = database
                .beginTransactionAsync(processModelTransaction)
                .build();

        transaction.execute();

    }


    private Intent createShareIntent(String storyLink) {
        Intent myShareIntent = new Intent(Intent.ACTION_SEND);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            myShareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        } else {
            myShareIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        }
        myShareIntent.setType("text/plain");
        myShareIntent.putExtra(Intent.EXTRA_TEXT,storyLink);
        return myShareIntent;
    }

    public void notifyUser(String message) {
        Snackbar.make(mRootLayout,message,Snackbar.LENGTH_LONG)
                .show();
    }
}


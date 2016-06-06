package com.valevich.zadolbali.ui.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.valevich.zadolbali.R;
import com.valevich.zadolbali.database.data.StoryEntry;
import com.valevich.zadolbali.network.RestClient;
import com.valevich.zadolbali.utils.StarActionProvider;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.ViewById;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by NotePad.by on 05.06.2016.
 */
@EFragment(R.layout.fragment_story_slide)
public class StorySlideFragment extends Fragment{

    private StarActionProvider mStarActionProvider;

    @Bind(R.id.story_content)
    TextView mStoryTextView;

    private static final String ARGUMENT_STORY = "STORY";

    private static StoryEntry mStory;

    public StorySlideFragment() {}

    public static StorySlideFragment newInstance(StoryEntry story) {
        StorySlideFragment pageFragment = new StorySlideFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(ARGUMENT_STORY,story);
        pageFragment.setArguments(arguments);
        return pageFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mStory = (StoryEntry) getArguments().getSerializable(ARGUMENT_STORY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_story_slide, container, false);
        ButterKnife.bind(this,view);
        mStoryTextView.setText(mStory.getDescription());
        return view;
    }

    private Intent createShareIntent() {
        Intent myShareIntent = new Intent(Intent.ACTION_SEND);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            myShareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        } else {
            myShareIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        }
        myShareIntent.setType("text/plain");
        myShareIntent.putExtra(Intent.EXTRA_TEXT, RestClient.BASE_URL + getCurrentStoryLink());
        return myShareIntent;
    }

    private String getCurrentStoryLink() {
        return mStory.getLink();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detail_menu,menu);
        MenuItem shareItem = menu.findItem(R.id.action_share);
        ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        Intent shareIntent = createShareIntent();
        if(shareActionProvider != null && shareIntent != null) {
            shareActionProvider.setShareIntent(shareIntent);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem starItem = menu.findItem(R.id.action_favorite);
        mStarActionProvider = new StarActionProvider(getActivity(),mStory);
        MenuItemCompat.setActionProvider(starItem,mStarActionProvider);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
}

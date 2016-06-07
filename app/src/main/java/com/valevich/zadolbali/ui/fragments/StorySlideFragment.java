package com.valevich.zadolbali.ui.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
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

import org.androidannotations.annotations.EFragment;


import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by NotePad.by on 05.06.2016.
 */
@EFragment(R.layout.fragment_story_slide)
public class StorySlideFragment extends Fragment{

    @BindView(R.id.story_content)
    TextView mStoryTextView;

    private static final String ARGUMENT_STORY = "STORY";

    private String mStory;

    public StorySlideFragment() {}

    public static StorySlideFragment newInstance(String story) {
        StorySlideFragment pageFragment = new StorySlideFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_STORY,story);
        pageFragment.setArguments(arguments);
        return pageFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mStory = getArguments().getString(ARGUMENT_STORY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_story_slide, container, false);
        ButterKnife.bind(this,view);
        mStoryTextView.setText(mStory);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}

package com.valevich.zadolbali.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.valevich.zadolbali.database.data.StoryEntry;
import com.valevich.zadolbali.ui.fragments.StorySlideFragment;

import java.util.List;

/**
 * Created by NotePad.by on 05.06.2016.
 */
public class StorySlidePagerAdapter extends FragmentStatePagerAdapter {

    private List<StoryEntry> mStories;

    public StorySlidePagerAdapter(FragmentManager fm, List<StoryEntry> stories) {
        super(fm);
        mStories = stories;
    }

    @Override
    public Fragment getItem(int position) {
        return StorySlideFragment.newInstance(mStories.get(position));
    }

    @Override
    public int getCount() {
        return mStories.size();
    }

}


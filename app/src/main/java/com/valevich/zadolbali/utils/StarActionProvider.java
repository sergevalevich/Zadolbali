package com.valevich.zadolbali.utils;

import android.content.Context;
import android.support.v4.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;
import com.valevich.zadolbali.R;
import com.valevich.zadolbali.database.ZadolbaliDatabase;
import com.valevich.zadolbali.database.data.StoryEntry;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by NotePad.by on 05.06.2016.
 */
public class StarActionProvider extends ActionProvider {

    @BindView(R.id.star)
    CheckBox mCheckBox;

    private Context mContext;

    private StoryEntry mStory;

    public StarActionProvider(Context context,StoryEntry story) {
        super(context);
        mContext = context;
        mStory = story;
    }

    @Override
    public View onCreateActionView() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View providerView =
                layoutInflater.inflate(R.layout.provider_action_star, null);
        ButterKnife.bind(this,providerView);
        if(mStory.getIsFavourite() == 1) mCheckBox.setChecked(true);
        else mCheckBox.setChecked(false);
        return providerView;

    }

    @OnClick(R.id.star)
    void toggleCheckBox() {
        addToFavorite();
    }

    @Override
    public boolean onPerformDefaultAction() {
        return super.onPerformDefaultAction();
    }

    public void addToFavorite() {

        StoryEntry.editStories(new StoryEntry[]{mStory}, new StoryEditor() {
            @Override
            public void editStory(StoryEntry story) {
                if(story.getIsFavourite() == 0) story.setIsFavourite(1);
                else story.setIsFavourite(0);
                story.save();
            }

            @Override
            public void onStoryEdited(long current, long total, StoryEntry story) {

            }

            @Override
            public void onEditedSuccess() {

            }

            @Override
            public void onEditedError() {

            }
        });

    }
}

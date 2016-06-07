package com.valevich.zadolbali.utils;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;
import com.valevich.zadolbali.R;
import com.valevich.zadolbali.adapters.FavoriteStoryAdapter;
import com.valevich.zadolbali.adapters.StoryAdapter;
import com.valevich.zadolbali.database.ZadolbaliDatabase;
import com.valevich.zadolbali.database.data.StoryEntry;
import com.valevich.zadolbali.eventbus.RemovedStoryEvent;
import com.valevich.zadolbali.network.model.Story;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by NotePad.by on 05.06.2016.
 */
public class StoryTouchHelper extends ItemTouchHelper.SimpleCallback  {

    private FavoriteStoryAdapter mStoryAdapter;

    private View mRoot;

    private Context mContext;

    public StoryTouchHelper(FavoriteStoryAdapter storyAdapter, Context context){
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT);
        mStoryAdapter = storyAdapter;
        mContext = context;
        mRoot = ((Activity) mContext).findViewById(R.id.root_main);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        //TODO: Not implemented here
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        //Remove item
        final int itemCount = viewHolder.getAdapterPosition();
        final StoryEntry storyToRemove = mStoryAdapter.getStories().get(itemCount);
        mStoryAdapter.remove(itemCount);

        Snackbar snackbar = Snackbar.make(mRoot, mContext.getString(R.string.story_removed_from_fav_msg), Snackbar.LENGTH_LONG)
                .setAction(mContext.getString(R.string.undo_delete_action_msg), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mStoryAdapter.add(itemCount, storyToRemove);
                    }
                });

        snackbar.setCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                if(event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                    deleteStoryFromFavorite(storyToRemove);
                }
            }
        });

        snackbar.show();

    }

    private void deleteStoryFromFavorite(StoryEntry storyToRemove) {

        StoryEntry.editStories(new StoryEntry[]{storyToRemove}, new StoryEditor() {
            @Override
            public void editStory(StoryEntry story) {
                story.setIsFavourite(0);
                story.save();
            }

            @Override
            public void onStoryEdited(long current, long total, StoryEntry story) {

            }

            @Override
            public void onEditedSuccess() {
                EventBus.getDefault().post(new RemovedStoryEvent());
            }

            @Override
            public void onEditedError() {

            }
        });

    }

}


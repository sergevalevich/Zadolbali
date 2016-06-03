package com.valevich.zadolbali.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.valevich.zadolbali.R;
import com.valevich.zadolbali.database.data.StoryEntry;

import org.androidannotations.annotations.ViewById;

import java.util.List;
import java.util.Locale;

/**
 * Created by NotePad.by on 03.06.2016.
 */
public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.StoryViewHolder>{

    private List<StoryEntry> mStories;

    public StoryAdapter (List<StoryEntry> stories) {
        mStories = stories;
    }

    @Override
    public StoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.story_list_item,parent,false);
        return new StoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StoryViewHolder holder, int position) {
        holder.bindStory(mStories.get(position));
    }

    @Override
    public int getItemCount() {
        return mStories.size();
    }

    class StoryViewHolder extends RecyclerView.ViewHolder {

        @ViewById
        TextView description;
        @ViewById
        ImageView share;
        @ViewById
        Button more;
        @ViewById
        ImageView favorite;

        public StoryViewHolder(View itemView) {
            super(itemView);
        }

        public void bindStory(StoryEntry story) {
            description.setText(story.getDescription());
        }
    }

    public void refresh(List<StoryEntry> expenses) {
        mStories.clear();
        mStories.addAll(expenses);
        notifyDataSetChanged();
    }

}


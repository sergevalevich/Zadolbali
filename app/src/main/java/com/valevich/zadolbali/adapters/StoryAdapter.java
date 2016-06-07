package com.valevich.zadolbali.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.valevich.zadolbali.R;
import com.valevich.zadolbali.database.data.StoryEntry;
import com.valevich.zadolbali.utils.StoryActionHandler;

import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by NotePad.by on 03.06.2016.
 */
public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.StoryViewHolder>{

    private List<StoryEntry> mStories;

    private StoryActionHandler mStoryActionHandler;

    public StoryAdapter (List<StoryEntry> stories, StoryActionHandler storyActionHandler) {
        mStories = stories;
        mStoryActionHandler = storyActionHandler;
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

        @BindView(R.id.description)
        TextView description;
        @BindView(R.id.share)
        ImageView share;
        @BindView(R.id.more)
        Button more;
        @BindView(R.id.favorite)
        CheckBox favorite;

        @OnClick(R.id.share)
        void share() {
            mStoryActionHandler.share(mStories.get(getAdapterPosition()));
        }
        @OnClick(R.id.favorite)
        void addToFavorite() {
            mStoryActionHandler.addToFavorite(mStories.get(getAdapterPosition()));
        }
        @OnClick(R.id.more)
        void readMore() {
            mStoryActionHandler.more(getAdapterPosition(),StoryActionHandler.ALL_STORIES_FLAG);
        }

        public StoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void bindStory(StoryEntry story) {
            description.setText(story.getDescription());
            if(story.getIsFavourite() == 1) {
                favorite.setChecked(true);
            } else {
                favorite.setChecked(false);
            }
        }

    }

    public void refresh(List<StoryEntry> expenses) {
        mStories.clear();
        mStories.addAll(expenses);
        notifyDataSetChanged();
    }

}


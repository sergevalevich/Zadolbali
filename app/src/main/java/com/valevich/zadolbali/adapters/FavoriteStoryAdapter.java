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
import com.valevich.zadolbali.ui.StoryActionHandler;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by NotePad.by on 05.06.2016.
 */
public class FavoriteStoryAdapter extends
        RecyclerView.Adapter<FavoriteStoryAdapter.FavoriteStoryHolder> {

    private List<StoryEntry> mStories;

    private StoryActionHandler mStoryActionHandler;

    public FavoriteStoryAdapter(List<StoryEntry> stories, StoryActionHandler storyActionHandler) {
        mStories = stories;
        mStoryActionHandler = storyActionHandler;
    }

    @Override
    public FavoriteStoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.favorite_story_list_item,parent,false);
        return new FavoriteStoryHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteStoryHolder holder, int position) {
        holder.bindStory(mStories.get(position));
    }

    @Override
    public int getItemCount() {
        return mStories.size();
    }

     class FavoriteStoryHolder extends RecyclerView.ViewHolder {

         @Bind(R.id.fav_description)
         TextView description;

         @Bind(R.id.fav_share)
         Button share;

         @Bind(R.id.fav_more)
         Button more;

         @OnClick(R.id.fav_share)
         void share() {
            mStoryActionHandler.share(mStories.get(getAdapterPosition()));
         }

         @OnClick(R.id.fav_more)
         void readMore() {
            mStoryActionHandler.more(mStories.get(getAdapterPosition()));
         }

         public FavoriteStoryHolder(View itemView) {
             super(itemView);
             ButterKnife.bind(this,itemView);
         }

         public void bindStory (StoryEntry story) {
            description.setText(story.getDescription());
         }

     }

    public void refresh(List<StoryEntry> expenses) {
        mStories.clear();
        mStories.addAll(expenses);
        notifyDataSetChanged();
    }
}

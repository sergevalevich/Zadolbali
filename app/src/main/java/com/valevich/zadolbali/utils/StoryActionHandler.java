package com.valevich.zadolbali.utils;

import com.valevich.zadolbali.database.data.StoryEntry;

/**
 * Created by NotePad.by on 04.06.2016.
 */
public interface StoryActionHandler {
    public static final int ALL_STORIES_FLAG = 0;
    public static final int ONLY_FAVORITE_FLAG = 1;
    void share(StoryEntry story);
    void more(int position, int flag);
    void addToFavorite(StoryEntry story);
}

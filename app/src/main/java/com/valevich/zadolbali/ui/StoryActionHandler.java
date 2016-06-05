package com.valevich.zadolbali.ui;

import com.valevich.zadolbali.database.data.StoryEntry;

/**
 * Created by NotePad.by on 04.06.2016.
 */
public interface StoryActionHandler {
    void share(StoryEntry story);
    void more(StoryEntry story);
    void addToFavorite(StoryEntry story);
}

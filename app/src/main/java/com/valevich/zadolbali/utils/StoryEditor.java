package com.valevich.zadolbali.utils;

import com.valevich.zadolbali.database.data.StoryEntry;

/**
 * Created by NotePad.by on 07.06.2016.
 */
public interface StoryEditor {
    void editStory(StoryEntry story);
    void onStoryEdited(long current, long total, StoryEntry story);
    void onEditedSuccess();
    void onEditedError();
}

package com.valevich.zadolbali.database.data;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.valevich.zadolbali.database.ZadolbaliDatabase;


import java.util.List;

/**
 * Created by NotePad.by on 03.06.2016.
 */
@Table(database = ZadolbaliDatabase.class)
public class StoryEntry extends BaseModel {

    @PrimaryKey(autoincrement = true)
    long id;

    @Column//story text
    private String description;

    @Column//story source website. In the future this app will get stories not only from Zadolbali
    private String source;

    @Column//needed to mark the story as favorite
    private int favourite;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getFavourite() {
        return favourite;
    }

    public void setFavourite(int favourite) {
        this.favourite = favourite;
    }

    public static List<StoryEntry> getAllStories(String filter) {//// TODO: 03.06.2016 search by description
        return SQLite.select()
                .from(StoryEntry.class)
                .where(StoryEntry_Table.description.like("%" + filter + "%"))
                .queryList();
    }
}


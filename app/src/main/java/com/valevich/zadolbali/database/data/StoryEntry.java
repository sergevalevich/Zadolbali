package com.valevich.zadolbali.database.data;

import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.annotation.UniqueGroup;
import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.valevich.zadolbali.database.ZadolbaliDatabase;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by NotePad.by on 03.06.2016.
 */
@Table(database = ZadolbaliDatabase.class,
        uniqueColumnGroups = {@UniqueGroup(groupNumber = 1, uniqueConflict = ConflictAction.IGNORE)})
public class StoryEntry extends BaseModel {

    @PrimaryKey(autoincrement = true)
    long id;

    @Column//story text
    private String description;

    @Column//story source website. In the future this app will get stories not only from Zadolbali
    private String source;

    @Column//needed to mark the story as favorite
    private int isFavourite;

    @Column
    private int isRead;

    @Column
    @Unique(unique = false, uniqueGroups = 1)
    private String link;

    private static List<StoryEntry> mStories;

    private static List<StoryEntry> mFavoriteStories;

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

    public int getIsFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(int favourite) {
        this.isFavourite = favourite;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public static List<StoryEntry> getAllStories(String filter) {

//        SQLite.select()
//                .from(StoryEntry.class)
//                .where(StoryEntry_Table.description.like("%" + filter + "%"))
//                .async()
//                .queryResultCallback(new QueryTransaction.QueryResultCallback<StoryEntry>() {
//                    @Override
//                    public void onQueryResult(QueryTransaction transaction, @NonNull CursorResult<StoryEntry> result) {
//                        mStories = result.toList();
//                    }
//                }).execute();

        return SQLite.select()
                .from(StoryEntry.class)
                .where(StoryEntry_Table.description.like("%" + filter + "%"))
                .queryList();
    }

    public static List<StoryEntry> getAllFavoriteStories(String filter) {
//        SQLite.select()
//                .from(StoryEntry.class)
//                .where(StoryEntry_Table.isFavourite.eq(1))
//                .and(StoryEntry_Table.description.like("%" + filter + "%"))
//                .async()
//                .queryResultCallback(new QueryTransaction.QueryResultCallback<StoryEntry>() {
//                    @Override
//                    public void onQueryResult(QueryTransaction transaction, @NonNull CursorResult<StoryEntry> result) {
//                        mFavoriteStories = result.toList();
//                    }
//                }).execute();
//
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        return mFavoriteStories;
        return SQLite.select()
                .from(StoryEntry.class)
                .where(StoryEntry_Table.isFavourite.eq(1))
                .and(StoryEntry_Table.description.like("%" + filter + "%"))
                .queryList();
    }

}


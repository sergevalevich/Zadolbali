package com.valevich.zadolbali.database.data;

import android.support.annotation.NonNull;
import android.text.Html;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.annotation.UniqueGroup;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;
import com.valevich.zadolbali.database.ZadolbaliDatabase;
import com.valevich.zadolbali.network.model.Story;
import com.valevich.zadolbali.utils.StoryEditor;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by NotePad.by on 03.06.2016.
 */
@Table(database = ZadolbaliDatabase.class,
        uniqueColumnGroups = {@UniqueGroup(groupNumber = 1, uniqueConflict = ConflictAction.IGNORE)})
public class StoryEntry extends BaseModel implements Serializable{

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
    private int date;

    @Column
    @Unique(unique = false, uniqueGroups = 1)
    private String link;

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

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public static List<StoryEntry> getAllStories(String filter) {
        return SQLite.select()
                .from(StoryEntry.class)
                .where(StoryEntry_Table.description.like("%" + filter + "%"))
                .orderBy(StoryEntry_Table.date,false)
                .queryList();
    }

    public static List<StoryEntry> getAllFavoriteStories(String filter) {
        return SQLite.select()
                .from(StoryEntry.class)
                .where(StoryEntry_Table.isFavourite.eq(1))
                .and(StoryEntry_Table.description.like("%" + filter + "%"))
                .orderBy(StoryEntry_Table.date,false)
                .queryList();
    }

    public static void editStories(StoryEntry[] stories, final StoryEditor storyEditor) {

        DatabaseDefinition database = FlowManager.getDatabase(ZadolbaliDatabase.class);

        ProcessModelTransaction<StoryEntry> processModelTransaction =
                new ProcessModelTransaction.Builder<>(new ProcessModelTransaction.ProcessModel<StoryEntry>() {
                    @Override
                    public void processModel(StoryEntry story) {
                        storyEditor.editStory(story);
                    }
                }).processListener(new ProcessModelTransaction.OnModelProcessListener<StoryEntry>() {
                    @Override
                    public void onModelProcessed(long current, long total, StoryEntry story) {
                        storyEditor.onStoryEdited(current,total,story);
                    }
                }).addAll(stories).build();

        Transaction transaction = database.beginTransactionAsync(processModelTransaction)
                .success(new Transaction.Success() {
                    @Override
                    public void onSuccess(Transaction transaction) {
                        storyEditor.onEditedSuccess();
                    }
                })
                .error(new Transaction.Error() {
                    @Override
                    public void onError(Transaction transaction, Throwable error) {
                        storyEditor.onEditedError();
                    }
                })
                .build();

        transaction.execute();

    }

}


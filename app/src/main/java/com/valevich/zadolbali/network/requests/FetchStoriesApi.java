package com.valevich.zadolbali.network.requests;

import com.valevich.zadolbali.network.model.Story;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by NotePad.by on 03.06.2016.
 */
public interface FetchStoriesApi {
    @GET("/api/get")
    List<Story> getStories(@Query("site") String site, @Query("name") String name, @Query("num") int num);
}

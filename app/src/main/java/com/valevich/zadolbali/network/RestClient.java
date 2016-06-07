package com.valevich.zadolbali.network;

import com.valevich.zadolbali.network.requests.FetchStoriesApi;

import org.androidannotations.annotations.EBean;

import retrofit.RestAdapter;

/**
 * Created by NotePad.by on 03.06.2016.
 */
@EBean
public class RestClient {
    public static final String BASE_URL = "http://www.umori.li";

    private FetchStoriesApi mFetchStoriesApi;

    public RestClient() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)//logging request details
                .build();
        mFetchStoriesApi = restAdapter.create(FetchStoriesApi.class);
    }

    public FetchStoriesApi getFetchStoriesApi() {
        return mFetchStoriesApi;
    }

}

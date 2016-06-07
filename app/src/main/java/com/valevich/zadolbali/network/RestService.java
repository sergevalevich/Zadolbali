package com.valevich.zadolbali.network;

import com.valevich.zadolbali.network.model.Story;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.List;

/**
 * Created by NotePad.by on 03.06.2016.
 */
@EBean
public class RestService {

    private static final String DEFAULT_NAME = "zadolbali"; // TODO: 04.06.2016  change to different names in the future

    private static final String DEFAULT_SITE = "zadolba.li";

    private static final int DEFAULT_STORY_COUNT = 50;

    @Bean RestClient mRestClient;

    public List<Story> getStories() {
        return mRestClient.getFetchStoriesApi().getStories(DEFAULT_SITE,DEFAULT_NAME,DEFAULT_STORY_COUNT);
    }
}

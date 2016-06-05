package com.valevich.zadolbali;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import org.androidannotations.annotations.EApplication;

/**
 * Created by NotePad.by on 03.06.2016.
 */
@EApplication
public class ZadolbaliApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(new FlowConfig.Builder(this)
                .openDatabasesOnInit(true).build());
    }
}

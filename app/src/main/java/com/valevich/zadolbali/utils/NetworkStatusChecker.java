package com.valevich.zadolbali.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.SystemService;

/**
 * Created by NotePad.by on 20.05.2016.
 */
@EBean
public class NetworkStatusChecker {
    @RootContext
    Context mContext;
    @SystemService
    ConnectivityManager mConnectivityManager;
    public boolean isNetworkAvailable() {
        NetworkInfo activeNetwork = mConnectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}

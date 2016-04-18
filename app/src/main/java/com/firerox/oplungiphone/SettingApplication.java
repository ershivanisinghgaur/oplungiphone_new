package com.firerox.oplungiphone;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

public class SettingApplication extends Application {
    private Tracker mTracker;

    public synchronized Tracker getTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            analytics.setDryRun(!SettingConfig.ANALYTICS);
            mTracker = analytics.newTracker(com.firerox.oplungiphone.R.xml.analytics_app_tracker);
        }
        return mTracker;
    }
}
package com.sfexpress.sharedemo;

import android.app.Application;

public class ShareDemoApp extends Application {
    private static ShareDemoApp app;

    @Override
    public void onCreate() {
        super.onCreate();
        new HHT5UncaughtExceptionHandler().init();
        app = this;
    }

    public static ShareDemoApp getInstance(){
        return app;
    }
}

package com.alibaba.ailab.ffmpegso;

import android.app.Application;


public class FFmpegApplication extends Application {

    private static FFmpegApplication context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        AppFileHelper.INSTANCE.initStoragePathInternal();
    }

    public static FFmpegApplication getInstance() {
        return context;
    }

}

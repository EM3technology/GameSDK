package com.em3.example;

import android.app.Application;

import com.em3.gamesdk.GameSDK;

/**
 * Created by MaFanwei on 2020/3/17.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        GameSDK.init(this);
    }
}

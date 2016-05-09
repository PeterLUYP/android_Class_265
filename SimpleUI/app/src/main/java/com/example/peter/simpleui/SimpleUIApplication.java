package com.example.peter.simpleui;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by Peter on 2016/5/5.
 */
public class SimpleUIApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                        .applicationId("1IIbdKr6rgMlclbPtrTcibogTq4wst4GVJC2dOX2")
                        .clientKey("lYpFFD6Bz8mendveOW91UvjypoGruuaaQPc4EUyR")
                        .server("https://parseapi.back4app.com/")
                        .build()
        );
    }



}

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
                        .applicationId("OQ1RRBOzQ2uCHmZe2TJgew9IKmk4LS4vH3ncPknw")
                        .clientKey("8bpAbLRlC9IelY83Y97Yhq9gkZ7vr5el0rrk8JtS")
                        .server("https://parseapi.back4app.com/")
                        .build()
        );
    }



}

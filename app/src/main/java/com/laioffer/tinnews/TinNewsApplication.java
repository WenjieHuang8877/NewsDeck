package com.laioffer.tinnews;

import android.app.Application;

import androidx.room.Room;

import com.laioffer.tinnews.database.TinNewsDatabase;
// application level customization
public class TinNewsApplication extends Application {

    private  static TinNewsDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        //Create the database in the TinNewsApplication
        database = Room.databaseBuilder(this, TinNewsDatabase.class,
                "tinnews_db").build();
    }
    // and add an accessor
    public static TinNewsDatabase getDatabase(){
        return database;
    }
}
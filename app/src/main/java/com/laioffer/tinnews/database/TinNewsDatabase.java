package com.laioffer.tinnews.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.laioffer.tinnews.model.Article;

//Make sure both the class and the methods are abstract.
// Why? We do not implement it the Room annotation processor will.
@Database(entities = {Article.class}, version = 1, exportSchema = false)
public abstract  class TinNewsDatabase extends RoomDatabase {

    public abstract ArticleDao articleDao();
}

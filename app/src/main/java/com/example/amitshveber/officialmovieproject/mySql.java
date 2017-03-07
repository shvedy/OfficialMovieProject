package com.example.amitshveber.officialmovieproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by amit shveber on 20/02/2017.
 */

public class mySql extends SQLiteOpenHelper {
    public mySql(Context context) {
        super(context, "Movies.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQLCreate = "CREATE TABLE " + DBContants.tableName + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + DBContants.titeColumm + " TEXT , " + DBContants.descriptionColumm + " TEXT , " + DBContants.ImgColumm + " TEXT , " + DBContants.yearColumm + " TEXT , " + DBContants.rateColumm + " TEXT , " + DBContants.RuntimeColumm + " TEXT , " + DBContants.actorsColumm + " TEXT)";

        db.execSQL(SQLCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

package com.nappingpirate.weeklyrecipe.Databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nic on 10/2/2015.
 */
public class RecipesDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "recipes.db";
    private static final int DATABASE_VERSION = 1;

    //Table Name
    public static final String TABLE_RECIPES = "recipes";

    //Table Column names
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_DIFFICULTY = "difficulty";
    public static final String KEY_RATING = "rating";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_TIME = "time";
    public static final String KEY_INGREDIENTS = "ingredients";
    public static final String KEY_DATE_ADDED = "date_added";
    public static final String KEY_LAST_DATE = "last_date";
    public static final String KEY_MAIN_INGREDIENT = "main_ingredient";
    public static final String KEY_COMMENT = "comment";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_FAVORITE = "favorite";

    public static final String DATABASE_CREATE = "CREATE TABLE "+TABLE_RECIPES +"("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_NAME + " TEXT,"
            + KEY_DIFFICULTY + " INTEGER,"
            + KEY_RATING + " INTEGER,"
            + KEY_DESCRIPTION + " TEXT,"
            + KEY_TIME + " TEXT,"
            + KEY_INGREDIENTS + " BLOB,"
            + KEY_DATE_ADDED + " TEXT,"
            + KEY_LAST_DATE + " TEXT,"
            + KEY_MAIN_INGREDIENT + " TEXT,"
            + KEY_COMMENT + " TEXT,"
            + KEY_IMAGE + " BLOB,"
            + KEY_FAVORITE + " INTEGER);";

    public RecipesDB(Context context) {
        super(context, DATABASE_NAME,   null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_RECIPES);

        //create fresh table
        this.onCreate(db);
    }
}

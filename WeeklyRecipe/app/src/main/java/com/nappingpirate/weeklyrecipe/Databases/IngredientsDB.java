package com.nappingpirate.weeklyrecipe.Databases;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nappingpirate.weeklyrecipe.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.FileHandler;

/**
 * Created by Nic on 10/2/2015.
 */
public class IngredientsDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "recipes.db";
    private static final int DATABASE_VERSION = 2;
    private final Context myContext;

    //Table Name
    public static final String TABLE_INGREDIENTS = "ingredients";

    //Table Column names
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_FOOD_GROUP = "food_group";
    public static final String KEY_LIQUID_SOLID = "liquid_solid";
    public static final String KEY_SHOPPING_LIST = "shopping_list";
    public static final String KEY_PANTRY = "pantry";
    public static final String KEY_MEASUREMENT = "measurement";
    public static final String KEY_QUANTITY = "quantity";
    public static final String KEY_DESCRIPTION = "description";

    public static final String DATABASE_CREATE = "CREATE TABLE "+TABLE_INGREDIENTS +"("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_NAME + " TEXT,"
            + KEY_FOOD_GROUP + " TEXT,"
            + KEY_LIQUID_SOLID + " INTEGER,"
            + KEY_SHOPPING_LIST + " INTEGER,"
            + KEY_PANTRY + " INTEGER,"
            + KEY_MEASUREMENT + " INT,"
            + KEY_QUANTITY + " INT,"
            + KEY_DESCRIPTION + " TEXT);";

    public IngredientsDB(Context context) {
        super(context, DATABASE_NAME,   null, DATABASE_VERSION);
        this.myContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_INGREDIENTS);

        //create fresh table
        this.onCreate(db);
    }

    public void copyDataBaseFromAsset() throws IOException{
        InputStream inputStream =  myContext.getAssets().open("ingredientsdb");
        OutputStream outputStream = new FileOutputStream("assets/");
        byte[] buffer = new byte[4096];
        int length;
        while ((length = inputStream.read(buffer)) > 0){
            outputStream.write(buffer, 0, length);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }
}

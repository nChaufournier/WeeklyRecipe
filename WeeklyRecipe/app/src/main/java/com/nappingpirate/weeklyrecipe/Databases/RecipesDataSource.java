package com.nappingpirate.weeklyrecipe.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nappingpirate.weeklyrecipe.Recipe;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nic on 10/2/2015.
 */
public class RecipesDataSource {
    private SQLiteDatabase database;
    private RecipesDB dbHelper;
    private String[] allColumns = { RecipesDB.KEY_ID,
            RecipesDB.KEY_NAME,
            RecipesDB.KEY_DIFFICULTY,
            RecipesDB.KEY_RATING,
            RecipesDB.KEY_DESCRIPTION,
            RecipesDB.KEY_TIME,
            RecipesDB.KEY_INGREDIENTS,
            RecipesDB.KEY_DATE_ADDED,
            RecipesDB.KEY_LAST_DATE,
            RecipesDB.KEY_MAIN_INGREDIENT,
            RecipesDB.KEY_COMMENT,
            RecipesDB.KEY_IMAGE,
            RecipesDB.KEY_FAVORITE};
    private String[] showRecipe = { RecipesDB.KEY_ID, RecipesDB.KEY_NAME, RecipesDB.KEY_DESCRIPTION};

    public RecipesDataSource(Context context) {
        dbHelper = new RecipesDB(context);
    }

    public void open() throws SQLException{
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Recipe createRecipe(Recipe recipe){
        ContentValues values = new ContentValues();
        values.put(RecipesDB.KEY_NAME, recipe.getName());
        values.put(RecipesDB.KEY_DIFFICULTY, recipe.getDifficulty());
        values.put(RecipesDB.KEY_RATING, recipe.getRating());
        values.put(RecipesDB.KEY_DESCRIPTION, recipe.getDescription());
        values.put(RecipesDB.KEY_TIME, recipe.getTime());
        values.put(RecipesDB.KEY_INGREDIENTS, recipe.getIngredients());
        values.put(RecipesDB.KEY_DATE_ADDED, recipe.getDateAdded());
        values.put(RecipesDB.KEY_LAST_DATE, recipe.getLastDateMade());
        values.put(RecipesDB.KEY_MAIN_INGREDIENT, recipe.getMainIngredient());
        values.put(RecipesDB.KEY_COMMENT, recipe.getComment());
        values.put(RecipesDB.KEY_IMAGE, recipe.getImage());
        values.put(RecipesDB.KEY_FAVORITE, recipe.getFavorite());

        long insertId = database.insert(RecipesDB.TABLE_RECIPES, null, values);
        Cursor cursor = database.query(RecipesDB.TABLE_RECIPES,
                allColumns, RecipesDB.KEY_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Recipe newRecipe = cursorToRecipe(cursor);
        cursor.close();
        return newRecipe;

    }

    private Recipe cursorToRecipe(Cursor cursor){
        Recipe recipe = new Recipe();
        recipe.set_id(cursor.getLong(0));
        recipe.setName(cursor.getString(1));
        recipe.setDifficulty(cursor.getInt(2));
        recipe.setRating(cursor.getInt(3));
        recipe.setDescription(cursor.getString(4));
        recipe.setTime(cursor.getString(5));
        recipe.setIngredients(cursor.getString(6));
        recipe.setDateAdded(cursor.getString(7));
        recipe.setLastDateMade(cursor.getString(8));
        recipe.setMainIngredient(cursor.getString(9));
        recipe.setComment(cursor.getString(10));
        recipe.setImage(cursor.getString(11));
        recipe.setFavorite(cursor.getInt(12));
        return recipe;
    }

    public List<Recipe> getAllRecipes(){
        List<Recipe> recipes = new ArrayList<>();

        Cursor cursor = database.query(RecipesDB.TABLE_RECIPES, allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Recipe recipe = cursorToRecipe(cursor);
            recipes.add(recipe);
            cursor.moveToNext();
        }
        cursor.close();
        return recipes;
    }

    public Recipe getSingleRecipe(long id){
        String selectId = RecipesDB.KEY_ID + " = '" + id + "'";
        Recipe recipe = new Recipe();
        Cursor cursor = database.query(RecipesDB.TABLE_RECIPES, allColumns, selectId, null, null, null, null);
        cursor.moveToFirst();
        recipe.set_id(cursor.getLong(0));
        recipe.setName(cursor.getString(1));
        recipe.setDifficulty(cursor.getInt(2));
        recipe.setRating(cursor.getInt(3));
        recipe.setDescription(cursor.getString(4));
        recipe.setTime(cursor.getString(5));
        recipe.setIngredients(cursor.getString(6));
        recipe.setDateAdded(cursor.getString(7));
        recipe.setLastDateMade(cursor.getString(8));
        recipe.setMainIngredient(cursor.getString(9));
        recipe.setComment(cursor.getString(10));
        recipe.setImage(cursor.getString(11));
        recipe.setFavorite(cursor.getInt(12));
        cursor.moveToNext();
        cursor.close();
        return recipe;
    }
}

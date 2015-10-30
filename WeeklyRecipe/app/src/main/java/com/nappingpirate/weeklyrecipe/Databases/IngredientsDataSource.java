package com.nappingpirate.weeklyrecipe.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nappingpirate.weeklyrecipe.Ingredient;
import com.nappingpirate.weeklyrecipe.Ingredient;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nic on 10/2/2015.
 */
public class IngredientsDataSource {
    private SQLiteDatabase database;
    private RecipesDB dbHelper;
    private String[] allColumns = { RecipesDB.KEY_ID,
            RecipesDB.KEY_NAME,
            RecipesDB.KEY_FOOD_GROUP,
            RecipesDB.KEY_LIQUID_SOLID,
            RecipesDB.KEY_SHOPPING_LIST,
            RecipesDB.KEY_PANTRY,
            RecipesDB.KEY_MEASUREMENT,
            RecipesDB.KEY_QUANTITY,
            RecipesDB.KEY_DESCRIPTION};
    private String[] showIngredient = { RecipesDB.KEY_ID, RecipesDB.KEY_NAME, RecipesDB.KEY_FOOD_GROUP};

    public IngredientsDataSource(Context context) {
        dbHelper = new RecipesDB(context);
    }

    public void open() throws SQLException{
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Ingredient createIngredient(Ingredient ingredient){
        ContentValues values = new ContentValues();
        values.put(RecipesDB.KEY_NAME, ingredient.getName());
        values.put(RecipesDB.KEY_FOOD_GROUP, ingredient.getFoodGroup());
        values.put(RecipesDB.KEY_LIQUID_SOLID, ingredient.getLiquidSolid());
        values.put(RecipesDB.KEY_SHOPPING_LIST, ingredient.getShoppingList());
        values.put(RecipesDB.KEY_PANTRY, ingredient.getPantry());
        values.put(RecipesDB.KEY_MEASUREMENT, ingredient.getMeasurement());
        values.put(RecipesDB.KEY_QUANTITY, ingredient.getQuantity());
        values.put(RecipesDB.KEY_DESCRIPTION, ingredient.getDescription());

        long insertId = database.insert(RecipesDB.TABLE_INGREDIENTS, null, values);
        Cursor cursor = database.query(RecipesDB.TABLE_INGREDIENTS,
                allColumns, RecipesDB.KEY_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Ingredient newIngredient = cursorToIngredient(cursor);
        cursor.close();
        return newIngredient;

    }

    private Ingredient cursorToIngredient(Cursor cursor){
        Ingredient Ingredient = new Ingredient();
        Ingredient.set_id(cursor.getLong(0));
        Ingredient.setName(cursor.getString(1));
        Ingredient.setFoodGroup(cursor.getString(2));
        Ingredient.setLiquidSolid(cursor.getInt(3));
        Ingredient.setShoppingList(cursor.getInt(4));
        Ingredient.setPantry(cursor.getInt(5));
        Ingredient.setMeasurement(cursor.getInt(6));
        Ingredient.setQuantity(cursor.getInt(7));
        Ingredient.setDescription(cursor.getString(8));
        return Ingredient;
    }

    public List<Ingredient> getAllIngredients(){
        List<Ingredient> Ingredients = new ArrayList<>();

        Cursor cursor = database.query(RecipesDB.TABLE_INGREDIENTS, allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Ingredient Ingredient = cursorToIngredient(cursor);
            Ingredients.add(Ingredient);
            cursor.moveToNext();
        }
        cursor.close();
        return Ingredients;
    }

    public List<Ingredient> getIngredientsByFoodGroup(String foodGroup){
        String fGroup = RecipesDB.KEY_FOOD_GROUP + " = '" + foodGroup +"'";
        List<Ingredient> ingredients = new ArrayList<>();

        Cursor cursor = database.query(RecipesDB.TABLE_INGREDIENTS, allColumns, fGroup, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Ingredient ingredient = new Ingredient();
            ingredient.set_id(cursor.getLong(0));
            ingredient.setName(cursor.getString(1));
            ingredient.setFoodGroup(cursor.getString(2));
            ingredient.setLiquidSolid(cursor.getInt(3));
            ingredient.setShoppingList(cursor.getInt(4));
            ingredient.setPantry(cursor.getInt(5));
            ingredient.setMeasurement(cursor.getInt(6));
            ingredient.setQuantity(cursor.getInt(7));
            ingredient.setDescription(cursor.getString(8));
            ingredients.add(ingredient);
            cursor.moveToNext();
        }
        cursor.close();
        return ingredients;
    }



    public Ingredient getSingleIngredient(long id){
        String selectId = RecipesDB.KEY_ID + " = '" + id + "'";
        Ingredient Ingredient = new Ingredient();
        Cursor cursor = database.query(RecipesDB.TABLE_INGREDIENTS, allColumns, selectId, null, null, null, null);
        cursor.moveToFirst();
        Ingredient.set_id(cursor.getLong(0));
        Ingredient.setName(cursor.getString(1));
        Ingredient.setFoodGroup(cursor.getString(2));
        Ingredient.setLiquidSolid(cursor.getInt(3));
        Ingredient.setShoppingList(cursor.getInt(4));
        Ingredient.setPantry(cursor.getInt(5));
        Ingredient.setMeasurement(cursor.getInt(6));
        Ingredient.setQuantity(cursor.getInt(7));
        Ingredient.setDescription(cursor.getString(8));
        cursor.moveToNext();
        cursor.close();
        return Ingredient;
    }

    public Ingredient getIngredientByName(String name){
        String selectName = RecipesDB.KEY_NAME + " = '" + name + "'";
        Ingredient Ingredient = new Ingredient();
        Cursor cursor = database.query(RecipesDB.TABLE_INGREDIENTS, allColumns, selectName, null, null, null, null);
        cursor.moveToFirst();
        Ingredient.set_id(cursor.getLong(0));
        Ingredient.setName(cursor.getString(1));
        Ingredient.setFoodGroup(cursor.getString(2));
        Ingredient.setLiquidSolid(cursor.getInt(3));
        Ingredient.setShoppingList(cursor.getInt(4));
        Ingredient.setPantry(cursor.getInt(5));
        Ingredient.setMeasurement(cursor.getInt(6));
        Ingredient.setQuantity(cursor.getInt(7));
        Ingredient.setDescription(cursor.getString(8));
        cursor.moveToNext();
        cursor.close();
        return Ingredient;
    }

    public void editIngredient(Ingredient Ingredient){
        ContentValues values = new ContentValues();
        values.put(RecipesDB.KEY_ID, Ingredient.get_id());
        values.put(RecipesDB.KEY_NAME, Ingredient.getName());
        values.put(RecipesDB.KEY_FOOD_GROUP, Ingredient.getFoodGroup());
        values.put(RecipesDB.KEY_LIQUID_SOLID, Ingredient.getLiquidSolid());
        values.put(RecipesDB.KEY_SHOPPING_LIST, Ingredient.getShoppingList());
        values.put(RecipesDB.KEY_PANTRY, Ingredient.getPantry());
        values.put(RecipesDB.KEY_MEASUREMENT, Ingredient.getMeasurement());
        values.put(RecipesDB.KEY_QUANTITY, Ingredient.getQuantity());
        values.put(RecipesDB.KEY_DESCRIPTION, Ingredient.getDescription());

        long insertId = Ingredient.get_id();
        database.update(RecipesDB.TABLE_INGREDIENTS,
                values, RecipesDB.KEY_ID + " = " + insertId, null);
    }

    public void deleteIngredient(long id){
        database.delete(RecipesDB.TABLE_INGREDIENTS, RecipesDB.KEY_ID + " = " + id, null);
    }


}

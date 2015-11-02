package com.nappingpirate.weeklyrecipe;

import android.util.Log;

import com.nappingpirate.weeklyrecipe.RecipeFiles.F2fRecipe;
import com.nappingpirate.weeklyrecipe.RecipeFiles.F2fRecipeListItem;
import com.nappingpirate.weeklyrecipe.RecipeFiles.Ingredient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nic on 10/31/2015.
 */
public class JsonRead {
    String jsonString;

    public JsonRead(String json) throws IOException{
        this.jsonString = json;
    }

    /**Used for the Nutrients Database**/
    public ArrayList getIngredientList(){
        ArrayList<Ingredient> ingreList = new ArrayList<>();
        try {

            JSONObject jsonRoot = new JSONObject(jsonString);

            JSONObject jsonList = jsonRoot.getJSONObject("list");
            //List items
            String q = jsonList.getString("q");
            String sr = jsonList.getString("sr");
            int start = jsonList.getInt("start");
            int end = jsonList.getInt("start");
            int total = jsonList.getInt("start");
            String group = jsonList.getString("group");
            String sort = jsonList.getString("sort");

            JSONArray jsonItemArray = jsonList.getJSONArray("item");
            for(int i = 0; i < jsonItemArray.length(); i++){
                Ingredient jsIngredient = new Ingredient();
                jsIngredient.setFoodGroup(jsonItemArray.getJSONObject(i).getString("group"));
                jsIngredient.setName(jsonItemArray.getJSONObject(i).getString("name"));
                ingreList.add(jsIngredient);
                Log.v("Json Array", jsonItemArray.getJSONObject(i).getString("name"));
                Log.v("Json Array", jsonItemArray.getJSONObject(i).getString("group"));
            }

        }catch (JSONException e){
            //Toast.makeText(MainActivity.this, "Broke at JSON", Toast.LENGTH_SHORT).show();
            Log.e("Result", "Broke at Json", e);
            e.printStackTrace();
        }

        return ingreList;
    }

    /**Used for the Food2Fork Api
     *
     * **/
    public ArrayList getF2fRecipeList(){
        ArrayList<F2fRecipeListItem> fRecipe = new ArrayList<>();
        try {
            JSONObject jsonRoot = new JSONObject(jsonString);
            String count = jsonRoot.getString("count");
            JSONArray jRecipes = jsonRoot.getJSONArray("recipes");

            for (int i=0; i<jRecipes.length(); i++){
                F2fRecipeListItem recipe = new F2fRecipeListItem();
                recipe.setPublisher(jRecipes.getJSONObject(i).getString("publisher"));
                recipe.setUrl(jRecipes.getJSONObject(i).getString("f2f_url"));
                recipe.setTitle(jRecipes.getJSONObject(i).getString("title"));
                recipe.setSource_url(jRecipes.getJSONObject(i).getString("source_url"));
                recipe.setRecipe_id(jRecipes.getJSONObject(i).getString("recipe_id"));
                recipe.setImage_url(jRecipes.getJSONObject(i).getString("image_url"));
                recipe.setSocial_rank(jRecipes.getJSONObject(i).getDouble("social_rank"));
                recipe.setPublisher_url(jRecipes.getJSONObject(i).getString("publisher_url"));
                fRecipe.add(recipe);
                Log.v("F2f", "Recipe: "+i+" "+recipe.toString());
            }

        }catch (JSONException e){
            Log.e("Result", "F2F get Recipe Broke", e);
        }

        return fRecipe;
    }

    public F2fRecipe getF2fRecipe(){
        F2fRecipe fRecipe = new F2fRecipe();

        try {
            JSONObject jsonRoot = new JSONObject(jsonString);
            JSONObject jRecipe = jsonRoot.getJSONObject("recipe");

            fRecipe.setPublisher(jRecipe.getString("publisher"));
            fRecipe.setUrl(jRecipe.getString("f2f_url"));
            fRecipe.setTitle(jRecipe.getString("title"));
            fRecipe.setSource_url(jRecipe.getString("source_url"));
            fRecipe.setRecipe_id(jRecipe.getString("recipe_id"));
            fRecipe.setImage_url(jRecipe.getString("image_url"));
            fRecipe.setSocial_rank(jRecipe.getDouble("social_rank"));
            fRecipe.setPublisher_url(jRecipe.getString("publisher_url"));
            JSONArray jIngredients = jRecipe.getJSONArray("ingredients");
            String[] tempArray = new String[jIngredients.length()];
            for (int i=0; i <jIngredients.length(); i++){
                tempArray[i] = jIngredients.getString(i);
                //Log.v("F2fArray", tempArray[i]);
            }
            fRecipe.setIngredients(tempArray);
            //Log.v("F2fArray", tempArray.toString());

        }catch (JSONException e){
            Log.e("Result", "F2F get Recipe Broke", e);
        }

        return fRecipe;
    }



}

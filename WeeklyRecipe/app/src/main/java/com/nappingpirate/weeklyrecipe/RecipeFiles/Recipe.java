package com.nappingpirate.weeklyrecipe.RecipeFiles;

import android.util.Log;

import com.nappingpirate.weeklyrecipe.RecipeFiles.Ingredient;

import java.util.ArrayList;

/**
 * Created by Nic on 10/2/2015.
 */
public class Recipe {
    private long _id;
    String name;
    private Integer difficulty;
    private Integer rating;
    private String description;
    private String time;
    private ArrayList<Ingredient> ingredientArrayList;
    private String ingredients;
    private String lastDateMade;
    private String dateAdded;
    private String mainIngredient;
    private String image;
    private String comment;
    private Integer favorite;

    public Recipe() {
    }

    public Recipe(long _id, String description, String name) {
        this._id = _id;
        this.description = description;
        this.name = name;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDifficulty() {
        return difficulty;
    }
    public String getDifficultyString(){
        if (difficulty == 0){
            return "Easy";
        }else if(difficulty == 1){
            return "Medium";
        }else if (difficulty == 2){
            return "Difficult";
        }else{
            return "Not Set";
        }
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public String getRating() {
        return rating+"/5";
    }

    public int getRatingInt() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }
    //Parses the string and pulls out the hour
    public int getHour(){
        String tmp = "";
        char c;
        int j = 0;
        for (int i = 0; i < time.length(); i++){
            c = time.charAt(j);
            if (!Character.toString(c).contentEquals("h")) {
                tmp = tmp + c;
                j++;
            }else{
                i = time.length();
                j++;
            }
            //Log.v("Loop", tmp+" "+j);
        }
        return Integer.parseInt(tmp);
    }

    public int getMinutes(){
        String tmp = "";
        char c;
        int j = 3;
        for (int i = 0; i < time.length(); i++){
            c = time.charAt(j);
            if (Character.toString(c).contentEquals("h") || Character.toString(c).contentEquals("r")){
                j++;
            }else if (!Character.toString(c).contentEquals("m")) {
                tmp = tmp + c;
                j++;
            }else{
                i = time.length();
                j++;
            }
            //Log.v("Loop", tmp+" "+j);
        }
        return Integer.parseInt(tmp);
    }

    public int getSeconds(){
        String tmp = "";
        char c;
        int j = 5;
        for (int i = 0; i < time.length(); i++){
            c = time.charAt(j);
            if (Character.toString(c).contentEquals("m") || Character.toString(c).contentEquals("i") || Character.toString(c).contentEquals("n")){
                j++;
            }else if (!Character.toString(c).contentEquals("s")) {
                tmp = tmp + c;
                j++;
            }else{
                i = time.length();
                j++;
            }
            //Log.v("Loop", tmp+" "+j);
        }
        return Integer.parseInt(tmp);
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getIngredients(){
        ingredients = toListString(ingredientArrayList);
        return ingredients;
    }

    public ArrayList<String> getIngredientArrayString() {
        ArrayList<String> ingArray = new ArrayList<>();
        //ingArray.add(ingredients);
        String tmp = "";
        char c;
        int j = 0;
        for (int i = 0; i < ingredients.length(); i++){
                c = ingredients.charAt(j);
                if (!Character.toString(c).contentEquals(",")) {
                    tmp = tmp + c;
                    j++;
                }else{
                    ingArray.add(tmp);
                    tmp ="";
                    j++;
                }
                Log.v("Loop", tmp+" "+j);
        }
        return ingArray;


    }
    public ArrayList<Ingredient> getIngredientArrayList() {
        return ingredientArrayList;

    }

    public void setIngredientArrayList(ArrayList<Ingredient> ingredientArrayList) {
        this.ingredientArrayList = ingredientArrayList;
    }

    public String toListString(ArrayList<Ingredient> ingredArray){
        String builder="";
        for(int i=0; i< ingredArray.size(); i++){

            builder = builder + ingredArray.get(i).getName() + ",";
        }

        return builder;

    }



    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getLastDateMade() {
        return lastDateMade;
    }

    public void setLastDateMade(String lastDateMade) {
        this.lastDateMade = lastDateMade;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getMainIngredient() {
        return mainIngredient;
    }

    public void setMainIngredient(String mainIngredient) {
        this.mainIngredient = mainIngredient;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getFavorite() {
        return favorite;
    }

    public void setFavorite(Integer favorite) {
        this.favorite = favorite;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "_id=" + _id +
                ", name='" + name + '\'' +
                ", difficulty=" + difficulty +
                ", rating=" + rating +
                ", description='" + description + '\'' +
                ", time='" + time + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", lastDateMade='" + lastDateMade + '\'' +
                ", dateAdded='" + dateAdded + '\'' +
                ", mainIngredient='" + mainIngredient + '\'' +
                ", image='" + image + '\'' +
                ", comment='" + comment + '\'' +
                ", favorite=" + favorite +
                '}';
    }
}
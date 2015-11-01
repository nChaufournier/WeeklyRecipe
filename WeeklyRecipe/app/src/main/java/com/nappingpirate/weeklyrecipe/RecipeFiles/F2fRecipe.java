package com.nappingpirate.weeklyrecipe.RecipeFiles;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.Arrays;

/**
 * Created by Nic on 11/1/2015.
 */
public class F2fRecipe implements Parcelable{
    String publisher;
    String url;
    String title;
    String source_url;
    String recipe_id;
    String image_url;
    double social_rank;
    String publisher_url;
    String[] ingredients;

    public F2fRecipe() {
    }

    public F2fRecipe(String publisher, String url, String title, String source_url, String recipe_id, String image_url, double social_rank, String publisher_url, String[] ingredients) {
        this.publisher = publisher;
        this.url = url;
        this.title = title;
        this.source_url = source_url;
        this.recipe_id = recipe_id;
        this.image_url = image_url;
        this.social_rank = social_rank;
        this.publisher_url = publisher_url;
        this.ingredients = ingredients;
    }

    public F2fRecipe(Parcel in){
        //String[] data = new String[9];
        //in.readStringArray(data);
        this.publisher = in.readString();
        Log.v("Result", this.publisher);
        this.url = in.readString();
        Log.e("Result", "url: "+ this.url);
        this.title = in.readString();
        Log.e("Result", "title: "+ this.title);
        this.source_url = in.readString();
        Log.e("Result", "source_url: "+ this.source_url);
        this.recipe_id = in.readString();
        Log.e("Result", "recipe_id: "+ this.recipe_id);
        this.image_url = in.readString();
        Log.e("Result", "image_url: "+ this.url);
        this.social_rank = in.readDouble();
        Log.v("Result", "social_rank: " + this.social_rank);
        this.publisher_url = in.readString();
        Log.v("Result", "publisher_url: " + this.publisher_url);
        this.ingredients = in.readString().split("#");

        /*Log.e("Result", "publisher: "+ this.publisher);
        this.publisher = data[0];
        Log.e("Result", "url: "+ this.url);
        this.url = data[1];
        Log.e("Result", "title: "+ this.title);
        this.title = data[2];
        Log.e("Result", "source: "+ this.source_url);
        this.source_url = data[3];
        Log.e("Result", "Recipe Id: "+ this.recipe_id);
        this.recipe_id = data[4];
        Log.e("Result", "image: "+ this.image_url);
        this.image_url = data[5];
        Log.e("Result", "Social Rank: "+ this.social_rank);
        this.social_rank = Integer.parseInt(data[6]);
        Log.v("Result", "Publisher Url: " + this.publisher_url);
        this.publisher_url = data[7];
        Log.v("Result", "Ingredients: " + this.ingredients);
        this.ingredients = data[8].split("#");


        */
        /*for (int i=0; i< ingredients.length; i++) {
            Log.v("Result", this.ingredients[i]);
        }*/

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        StringBuilder builder = new StringBuilder();
        String ingredientsString;
        for (String s : this.ingredients){
            builder.append(s).append("#");
        }
        ingredientsString = builder.toString();
        Log.v("Result", ingredientsString);
        Log.v("Result", "Social Rank: " + String.valueOf(this.social_rank));
        Log.v("Result", "Parcel Publisher: " + this.publisher + "");
        parcel.writeString(this.publisher);
        parcel.writeString(this.url);
        parcel.writeString(this.title);
        parcel.writeString(this.source_url);
        parcel.writeString(this.recipe_id);
        parcel.writeString(this.image_url);
        parcel.writeDouble(this.social_rank);
        parcel.writeString(this.publisher_url);
        parcel.writeString(ingredientsString);
        Log.v("Result", "Parcel: " + parcel.readString());
    }

    public static final Parcelable.Creator<F2fRecipe> CREATOR = new Parcelable.Creator<F2fRecipe>(){
        @Override
        public F2fRecipe createFromParcel(Parcel parcel) {
            return new F2fRecipe(parcel);
        }

        @Override
        public F2fRecipe[] newArray(int i) {
            return new F2fRecipe[i];
        }
    };

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource_url() {
        return source_url;
    }

    public void setSource_url(String source_url) {
        this.source_url = source_url;
    }

    public String getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(String recipe_id) {
        this.recipe_id = recipe_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public double getSocial_rank() {
        return social_rank;
    }

    public void setSocial_rank(double social_rank) {
        this.social_rank = social_rank;
    }

    public String getPublisher_url() {
        return publisher_url;
    }

    public void setPublisher_url(String publisher_url) {
        this.publisher_url = publisher_url;
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public String toString() {
        return "F2fRecipe{" +
                "publisher='" + publisher + '\'' +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", source_url='" + source_url + '\'' +
                ", recipe_id='" + recipe_id + '\'' +
                ", image_url='" + image_url + '\'' +
                ", social_rank=" + social_rank +
                ", publisher_url='" + publisher_url + '\'' +
                ", ingredients=" + Arrays.toString(ingredients) +
                '}';
    }
}

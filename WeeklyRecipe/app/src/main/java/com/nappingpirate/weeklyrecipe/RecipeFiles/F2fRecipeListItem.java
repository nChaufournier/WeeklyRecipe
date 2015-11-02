package com.nappingpirate.weeklyrecipe.RecipeFiles;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Nic on 10/31/2015.
 */
public class F2fRecipeListItem implements Parcelable{
    String publisher;
    String url;
    String title;
    String source_url;
    String recipe_id;
    String image_url;
    double social_rank;
    String publisher_url;
    Bitmap thumbnail;


    public F2fRecipeListItem() {
    }

    public F2fRecipeListItem(Parcel in){
        String[] data = new String[8];
        in.readStringArray(data);
        this.publisher = data[0];
        this.url = data[1];
        this.title = data[2];
        this.source_url = data[3];
        this.recipe_id = data[4];
        this.image_url = data[5];
        this.social_rank = Integer.parseInt(data[6]);
        this.publisher_url = data[7];
    }

    public F2fRecipeListItem(String publisher, String url, String title, String source_url, String recipe_id, String image_url, double social_rank, String publisher_url) {

        this.publisher = publisher;
        this.url = url;
        this.title = title;
        this.source_url = source_url;
        this.recipe_id = recipe_id;
        this.image_url = image_url;
        this.social_rank = social_rank;
        this.publisher_url = publisher_url;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

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

    @Override
    public String toString() {
        return "F2fRecipeListItem{" +
                "publisher='" + publisher + '\'' +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", source_url='" + source_url + '\'' +
                ", recipe_id=" + recipe_id +
                ", image_url='" + image_url + '\'' +
                ", social_rank=" + social_rank +
                ", publisher_url='" + publisher_url + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[]{
                this.publisher, this.url, this.title, this.source_url, this.recipe_id, this.image_url, String.valueOf(this.social_rank), this.publisher_url
        });
    }

    public static final Parcelable.Creator<F2fRecipeListItem> CREATOR= new Parcelable.Creator<F2fRecipeListItem>(){
        @Override
        public F2fRecipeListItem createFromParcel(Parcel parcel) {
            return new F2fRecipeListItem(parcel);
        }

        @Override
        public F2fRecipeListItem[] newArray(int i) {
            return new F2fRecipeListItem[i];
        }
    };



}

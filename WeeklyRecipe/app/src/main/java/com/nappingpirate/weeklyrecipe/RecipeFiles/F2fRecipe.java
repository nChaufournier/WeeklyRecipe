package com.nappingpirate.weeklyrecipe.RecipeFiles;

/**
 * Created by Nic on 10/31/2015.
 */
public class F2fRecipe {
    String publisher;
    String url;
    String title;
    String source_url;
    int recipe_id;
    String image_url;
    double social_rank;
    String publisher_url;


    public F2fRecipe() {
    }

    public F2fRecipe(String publisher, String url, String title, String source_url, int recipe_id, String image_url, double social_rank, String publisher_url) {

        this.publisher = publisher;
        this.url = url;
        this.title = title;
        this.source_url = source_url;
        this.recipe_id = recipe_id;
        this.image_url = image_url;
        this.social_rank = social_rank;
        this.publisher_url = publisher_url;
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

    public int getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(int recipe_id) {
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
        return "F2fRecipe{" +
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
}

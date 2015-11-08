package com.nappingpirate.weeklyrecipe.RecipeFiles;

/**
 * Created by Nic on 10/8/2015.
 */
public class Ingredient {
    private long _id;
    private String name;
    private String foodGroup;
    private Boolean liquidSolid;
    private Boolean shoppingList;
    private Boolean pantry;
    private Integer measurement;
    private Integer quantity;
    private String description;


    public Ingredient() {
    }

    public Ingredient(long _id, String name, String foodGroup, Boolean liquidSolid, Boolean shoppingList, Boolean pantry, Integer measurement, Integer quantity, String description) {
        this._id = _id;
        this.name = name;
        this.foodGroup = foodGroup;
        this.liquidSolid = liquidSolid;
        this.shoppingList = shoppingList;
        this.pantry = pantry;
        this.measurement = measurement;
        this.quantity = quantity;
        this.description = description;
    }

    public Ingredient(String name, String description) {
        this.name = name;
        this.description = description;
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

    public String getFoodGroup() {
        return foodGroup;
    }

    public void setFoodGroup(String foodGroup) {
        this.foodGroup = foodGroup;
    }

    public Boolean getLiquidSolid() {
        return liquidSolid;

    }

    public void setLiquidSolid(Integer liquidSolid) {
        if (liquidSolid == 0){
            this.liquidSolid = false;
        }else{
            this.liquidSolid = true;

        }
    }

    public Boolean getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(Integer shoppingList) {
        if (shoppingList == 0){
            this.shoppingList = false;
        }else{
            this.shoppingList = true;

        }
    }

    public Boolean getPantry() {
        return pantry;
    }

    public void setPantry(Integer pantry) {
        if (pantry == 0){
            this.pantry = false;
        }else{
            this.pantry = true;

        }
    }

    public Integer getMeasurement() {
        return measurement;
    }

    public void setMeasurement(Integer measurement) {
        this.measurement = measurement;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public String toString() {
        return  "Name='" + name + '\'' + "\n" +
                "_id=" + _id +  "\n"
                ;/*"FoodGroup='" + foodGroup + '\'' + "\n" +
                "LiquidSolid=" + liquidSolid + "\n" +
                "ShoppingList=" + shoppingList + "\n" +
                "Pantry=" + pantry + "\n" +
                "Measurement=" + measurement + "\n" +
                "Quantity=" + quantity + "\n" +
                "Description='" + description + '\'' + "\n";*/
    }
}

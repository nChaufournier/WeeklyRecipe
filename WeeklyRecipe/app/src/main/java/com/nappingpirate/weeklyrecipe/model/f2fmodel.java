package com.nappingpirate.weeklyrecipe.model;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class F2fmodel {

    private Integer count;
    private List<F2fRecipe> recipes = new ArrayList<F2fRecipe>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The count
     */
    public Integer getCount() {
        return count;
    }

    /**
     *
     * @param count
     * The count
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     *
     * @return
     * The recipes
     */
    public List<F2fRecipe> getRecipes() {
        return recipes;
    }

    /**
     *
     * @param recipes
     * The recipes
     */
    public void setRecipes(List<F2fRecipe> recipes) {
        this.recipes = recipes;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}


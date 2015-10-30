package com.nappingpirate.weeklyrecipe;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nappingpirate.weeklyrecipe.Databases.RecipesDB;
import com.nappingpirate.weeklyrecipe.Databases.RecipesDataSource;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Nic on 10/3/2015.
 */
public class ViewRecipe extends Activity {

    TextView tv_recipeName;
    TextView tv_recipeRating;
    TextView tv_recipeDifficulty;
    TextView tv_recipeDescription;
    ImageView iv_recipeImage;
    LinearLayout rl_ingredients;
    Bundle extras;
    Recipe recipe;
    RecipesDataSource db;
    ListView lv_ingredients;
    ArrayAdapter<String> mArrayAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_recipe);

        db = new RecipesDataSource(this);
        try{
            db.open();
        }catch (SQLException e){
            e.printStackTrace();
        }

        tv_recipeName = (TextView) findViewById(R.id.tv_recipeName);
        tv_recipeRating = (TextView) findViewById(R.id.tv_rating);
        tv_recipeDifficulty = (TextView) findViewById(R.id.tv_difficulty);
        tv_recipeDescription = (TextView) findViewById(R.id.tv_description);
        lv_ingredients = (ListView)findViewById(R.id.lv_ingredients);
        iv_recipeImage = (ImageView) findViewById(R.id.iv_recipeImage);
        rl_ingredients = (LinearLayout) findViewById(R.id.rl_ingredients);


        extras = getIntent().getExtras();
        //Toast.makeText(getApplicationContext(), extras + " ", Toast.LENGTH_SHORT).show();
        //recipe = db.getSingleRecipe(extras.getLong("id"));
        if(extras != null){
            if (extras.containsKey("id")){
                //Toast.makeText(getApplicationContext(), extras.toString(), Toast.LENGTH_LONG).show();
                Long id = extras.getLong("id");
                //Toast.makeText(getApplicationContext(), id + " ", Toast.LENGTH_LONG).show();
                recipe = db.getSingleRecipe(id);
                tv_recipeName.setText(recipe.getName());
                tv_recipeRating.setText((recipe.getRating()));
                tv_recipeDifficulty.setText(recipe.getDifficultyString());
                //Loops through creating more ingredients as they are needed.
                for (int i=0; i < recipe.getIngredientArrayString().size(); i++){
                    listIngredient(recipe.getIngredientArrayString().get(i));
                }
                tv_recipeDescription.setText(recipe.getDescription());

                iv_recipeImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(ViewRecipe.this, recipe.getIngredientArrayString()+"", Toast.LENGTH_SHORT).show();
                        Toast.makeText(ViewRecipe.this, recipe.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                Toast.makeText(getApplicationContext(), "Key not found", Toast.LENGTH_LONG).show();
            }

            //et_recipeName.setText(recipe.getName());
        }else{
            Toast.makeText(getApplicationContext(), "Didn't Work", Toast.LENGTH_LONG).show();
        }

        /*
        recipe = db.getSingleRecipe(extras.getLong("id"));
        if(recipe != null){
            Toast.makeText(getApplicationContext(), recipe.toString(), Toast.LENGTH_LONG).show();
            et_recipeName.setText(recipe.getName());
        }*/



    }
    public void listIngredient(String ingredient){
        CheckBox et_ingredient = new CheckBox(getApplicationContext());
        rl_ingredients.addView(et_ingredient);
        et_ingredient.setText(ingredient);
        et_ingredient.setTextColor(Color.BLACK);
        et_ingredient.setMaxWidth(100);
        et_ingredient.setMaxHeight(100);
        et_ingredient.setTextSize(18);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) et_ingredient.getLayoutParams();
        layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        et_ingredient.setLayoutParams(layoutParams);
        et_ingredient.setTag("Edit Text");
    }
}

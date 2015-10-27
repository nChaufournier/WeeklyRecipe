package com.nappingpirate.weeklyrecipe;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nappingpirate.weeklyrecipe.Databases.RecipesDB;
import com.nappingpirate.weeklyrecipe.Databases.RecipesDataSource;

import java.sql.SQLException;

/**
 * Created by Nic on 10/3/2015.
 */
public class ViewRecipe extends Activity {

    TextView tv_recipeName;
    TextView tv_recipeRating;
    TextView tv_recipeDifficulty;
    TextView tv_recipeDescription;
    Bundle extras;
    Recipe recipe;
    RecipesDataSource db;

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


        extras = getIntent().getExtras();
        //Toast.makeText(getApplicationContext(), extras + " ", Toast.LENGTH_SHORT).show();
        //recipe = db.getSingleRecipe(extras.getLong("id"));
        if(extras != null){
            if (extras.containsKey("id")){
                Toast.makeText(getApplicationContext(), extras.toString(), Toast.LENGTH_LONG).show();
                Long id = extras.getLong("id");
                Toast.makeText(getApplicationContext(), id + " ", Toast.LENGTH_LONG).show();
                recipe = db.getSingleRecipe(id);
                tv_recipeName.setText(recipe.getName());
                tv_recipeRating.setText((recipe.getRating()));
                tv_recipeDifficulty.setText(recipe.getDifficultyString());

                /*tv_recipeDifficulty.setText(recipe.getDifficulty());*/
                tv_recipeDescription.setText(recipe.getDescription());
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
}

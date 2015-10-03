package com.nappingpirate.weeklyrecipe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.nappingpirate.weeklyrecipe.Databases.RecipesDataSource;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Nic on 10/1/2015.
 */
public class AddRecipe extends Activity {
    ImageButton btn_close;
    Button btn_save;
    Button btn_view;
    EditText et_name;
    EditText et_difficulty;
    EditText et_rating;
    EditText et_description;
    EditText et_time;
    EditText et_ingredients;
    EditText et_lastDateMade;
    EditText et_dateAdded;
    EditText et_mainIngredient;
    EditText et_image;
    EditText et_comment;
    EditText et_favorite;
    String date;

    private RecipesDataSource db;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_recipe);
        getActionBar().hide();

        db = new RecipesDataSource(this);
        try {
            db.open();
        }catch (SQLException e){
            e.printStackTrace();
        }

        //Current date
        date = new SimpleDateFormat("MM-dd-yyyy").format(new Date());

        //Buttons
        btn_close = (ImageButton) findViewById(R.id.btn_close);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_view = (Button) findViewById(R.id.btn_view);

        //Input Fields
        et_name = (EditText) findViewById(R.id.et_recipeName);
        et_difficulty = (EditText) findViewById(R.id.et_difficulty);
        et_rating = (EditText) findViewById(R.id.et_rating);
        et_description = (EditText) findViewById(R.id.et_description);
        et_time = (EditText) findViewById(R.id.et_time);
        et_ingredients = (EditText) findViewById(R.id.et_ingredients);
        et_mainIngredient = (EditText) findViewById(R.id.et_mainIngredient);
        et_image = (EditText) findViewById(R.id.et_image);
        et_comment = (EditText) findViewById(R.id.et_comment);
        et_favorite = (EditText) findViewById(R.id.et_favorite);






        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_name != null){
                    Recipe newRecipe = new Recipe();
                    newRecipe.setName(et_name.getText().toString());
                    newRecipe.setDifficulty(Integer.parseInt(et_difficulty.getText().toString()));
                    newRecipe.setRating(Integer.parseInt(et_rating.getText().toString()));
                    newRecipe.setDescription(et_description.getText().toString());
                    newRecipe.setTime(et_time.getText().toString());
                    newRecipe.setLastDateMade(null);
                    newRecipe.setIngredients(et_ingredients.getText().toString());
                    newRecipe.setDateAdded(date);
                    newRecipe.setMainIngredient(et_mainIngredient.getText().toString());
                    newRecipe.setImage(null);
                    newRecipe.setComment(et_comment.getText().toString());
                    if (et_favorite.getText().toString().equals("0") || et_favorite.getText().toString().equals("1")){
                        newRecipe.setFavorite(Integer.parseInt(et_favorite.getText().toString()));
                    }else{
                        newRecipe.setFavorite(0);
                    }

                    //showMessage("Recipe", newRecipe.toString());
                    db.createRecipe(newRecipe);

                    Toast.makeText(getApplicationContext(), "Recipe Added!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(v.getContext(), MainActivity.class);
                    startActivity(i);
                }

            }
        });




        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), MainActivity.class);
                startActivity(i);
            }
        });

        btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), db.getAllEvents().toString(), Toast.LENGTH_LONG).show();
                showMessage("All Items", db.getAllRecipes().toString());
            }
        });

        /*getActionBar().setDisplayHomeAsUpEnabled(true);*/
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);

    }*/

    public void showMessage(String title,String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}



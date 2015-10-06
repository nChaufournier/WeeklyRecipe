package com.nappingpirate.weeklyrecipe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nappingpirate.weeklyrecipe.Databases.RecipesDataSource;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Nic on 10/1/2015.
 */
public class AddRecipe extends Activity {
    private ImageButton btn_close;
    private Button btn_save;
    private Button btn_view;
    private Button btn_addIngredients;
    private EditText et_name;
    private EditText et_difficulty;
    private TextView et_rating;
    private RatingBar rb_rating;
    private EditText et_description;
    private EditText et_time;
    private TextView et_ingredients;
    private EditText et_lastDateMade;
    private EditText et_dateAdded;
    private EditText et_mainIngredient;
    private EditText et_image;
    private EditText et_comment;
    private EditText et_favorite;
    private String date;
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
        btn_addIngredients = (Button) findViewById(R.id.btn_addIngredient);

        //Input Fields
        et_name = (EditText) findViewById(R.id.et_recipeName);
        et_difficulty = (EditText) findViewById(R.id.et_difficulty);
        et_rating = (TextView) findViewById(R.id.et_rating);
        rb_rating = (RatingBar) findViewById(R.id.rb_rating);
        et_description = (EditText) findViewById(R.id.et_description);
        et_time = (EditText) findViewById(R.id.et_time);
        et_ingredients = (TextView) findViewById(R.id.tv_label_ingredients);
        et_mainIngredient = (EditText) findViewById(R.id.et_mainIngredient);
        et_image = (EditText) findViewById(R.id.et_image);
        et_comment = (EditText) findViewById(R.id.et_comment);
        et_favorite = (EditText) findViewById(R.id.et_favorite);



        btn_addIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addIngredientMenu("Add Ingredient", "Ingredients Go Here");
            }
        });



        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddRecipe.this, et_name.getText(), Toast.LENGTH_SHORT).show();

                Toast.makeText(AddRecipe.this, rb_rating.getRating() + "", Toast.LENGTH_SHORT).show();
                /**/
                if(!et_name.getText().toString().equals("")){
                    Recipe newRecipe = new Recipe();
                    newRecipe.setName(et_name.getText().toString());
                    newRecipe.setDifficulty(Integer.parseInt(et_difficulty.getText().toString()));
                    newRecipe.setRating((int)rb_rating.getRating());
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
                }else {
                    Toast.makeText(AddRecipe.this, "Please add Content", Toast.LENGTH_SHORT).show();
                }/**/

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

    public void addIngredientMenu(String title,String message)
    {
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LayoutInflater inflater = getLayoutInflater();
                View dialogLayout = inflater.inflate(R.layout.ingredients_dialog, null);
                AlertDialog.Builder b = new AlertDialog.Builder(getApplicationContext());
                b.setView(dialogLayout);
                b.show();
            }
        });
        builder.show();
    }
}



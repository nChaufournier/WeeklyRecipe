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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.nappingpirate.weeklyrecipe.Databases.RecipesDataSource;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Nic on 10/1/2015.
 */
public class AddRecipe extends Activity {
    private ImageButton btn_close;
    private Button btn_save;
    private Button btn_view;
    private Button btn_addIngredients;
    private ToggleButton btn_easy;
    private ToggleButton btn_medium;
    private ToggleButton btn_hard;
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
    private String date;
    private RecipesDataSource db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_recipe);
        getActionBar().hide();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        db = new RecipesDataSource(this);
        try {
            db.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Current date
        date = new SimpleDateFormat("MM-dd-yyyy", Locale.US).format(new Date());

        //Buttons
        btn_close = (ImageButton) findViewById(R.id.btn_close);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_view = (Button) findViewById(R.id.btn_view);
        btn_addIngredients = (Button) findViewById(R.id.btn_addIngredient);
        btn_easy = (ToggleButton)findViewById(R.id.tb_easy);
        btn_medium = (ToggleButton)findViewById(R.id.tb_medium);
        btn_hard = (ToggleButton)findViewById(R.id.tb_hard);

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


        btn_addIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addIngredientMenu("Add Ingredient", "Ingredients Go Here");
            }
        });
        //Makes it so only one option is selected for the description buttons
        btn_easy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    btn_medium.setChecked(false);
                    btn_hard.setChecked(false);
                }else{
                    btn_easy.setChecked(false);
                }
            }
        });
        btn_medium.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    btn_easy.setChecked(false);
                    btn_hard.setChecked(false);
                }else{
                    btn_medium.setChecked(false);
                }
            }
        });
        btn_hard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    btn_medium.setChecked(false);
                    btn_easy.setChecked(false);
                }else{
                    btn_hard.setChecked(false);
                }
            }
        });



        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddRecipe.this, et_name.getText(), Toast.LENGTH_SHORT).show();

                Toast.makeText(AddRecipe.this, rb_rating.getRating() + "", Toast.LENGTH_SHORT).show();
                /**/
                if (!et_name.getText().toString().equals("")) {
                    Recipe newRecipe = new Recipe();
                    newRecipe.setName(et_name.getText().toString());
                    newRecipe.setDifficulty(Integer.parseInt(et_difficulty.getText().toString()));
                    newRecipe.setRating((int) rb_rating.getRating());
                    newRecipe.setDescription(et_description.getText().toString());
                    newRecipe.setTime(et_time.getText().toString());
                    newRecipe.setLastDateMade(null);
                    newRecipe.setIngredients(et_ingredients.getText().toString());
                    newRecipe.setDateAdded(date);
                    newRecipe.setMainIngredient(et_mainIngredient.getText().toString());
                    newRecipe.setImage(null);
                    newRecipe.setComment(et_comment.getText().toString());

                    //showMessage("Recipe", newRecipe.toString());
                    db.createRecipe(newRecipe);

                    Toast.makeText(getApplicationContext(), "Recipe Added!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(v.getContext(), MainActivity.class);
                    startActivity(i);
                } else {
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
    }
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
        final String[] ingredient = getResources().getStringArray(R.array.ingredientGroups_Array);
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setItems(R.array.ingredientGroups_Array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {
                showArray(ingredient[position], getResources().getStringArray(R.array.Grains));
                Toast.makeText(AddRecipe.this, ingredient[position], Toast.LENGTH_SHORT).show();
            }
        });
        /*builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });*/
        builder.show();
    }

    public void showArray(String title, String[] message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setItems(message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(AddRecipe.this, "Ingredient", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(AddRecipe.this, "Add New Ingredient", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        //builder.setMessage(message.toString());
        builder.show();
    }
}



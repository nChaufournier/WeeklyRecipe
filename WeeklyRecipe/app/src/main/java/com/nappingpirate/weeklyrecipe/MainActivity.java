package com.nappingpirate.weeklyrecipe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.nappingpirate.weeklyrecipe.Databases.RecipesDataSource;

import java.security.PrivateKey;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainActivity extends Activity {
    ImageButton fabMenu;
    ImageButton fabAdd;
    ImageButton fabRandom;
    ListView lv_recipes;
    RecipeCardAdapter recipeCardAdapter;
    private ArrayAdapter mArrayAdapter;
    ArrayList recipes =  new ArrayList();
    RecipesDataSource db;
    //ShowMessage showMessage;

            /*"Recipe 2","Recipe 3","Recipe 4","Recipe 5","Recipe 6","Recipe 7","Recipe 8","Recipe 9","Recipe 10","Recipe 11","Recipe 12","Recipe 13","Recipe 14","Recipe 15","Recipe 16","Recipe 17","Recipe 18","Recipe 19","Recipe 20");*/

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new RecipesDataSource(this);
        try{
            db.open();
        } catch (SQLException e){
            e.printStackTrace();
        }
        recipes.add("Recipe 1");
        recipes.add("Recipe 2");recipes.add("Recipe 3");recipes.add("Recipe 4");recipes.add("Recipe 5");


        fabMenu = (ImageButton) findViewById(R.id.fab_menu);
        fabAdd = (ImageButton) findViewById(R.id.fab_add);
        fabRandom = (ImageButton) findViewById(R.id.fab_random);
        lv_recipes = (ListView) findViewById(R.id.lv_recipes);


        //Makes the fab buttons visible to invisible depending on click
        fabMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fabAdd.getVisibility() == View.VISIBLE){
                    fabAdd.setVisibility(View.GONE);
                    fabRandom.setVisibility(View.GONE);
                }else {
                    fabAdd.setVisibility(View.VISIBLE);
                    fabRandom.setVisibility(View.VISIBLE);
                }
            }
        });

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), AddRecipe.class);
                startActivity(i);
                recipes.add("Recipe " + recipes.size() + 1);

            }
        });


        fabRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), db.getAllRecipes().toString(), Toast.LENGTH_LONG).show();
                showMessage("Recipes", db.getAllRecipes().toString());
            }
        });
        recipeCardAdapter = new RecipeCardAdapter(getApplicationContext(), R.layout.recipe_card, db.getAllRecipes());
        if (lv_recipes != null){
            lv_recipes.setAdapter(recipeCardAdapter);
        }
        lv_recipes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Recipe Should Show", Toast.LENGTH_SHORT).show();
                /*Intent i = new Intent(view.getContext(), ViewRecipe.class);
                i.putExtra("id", position);
                startActivity(i);*/

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void showMessage(String title,String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}

package com.nappingpirate.weeklyrecipe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.nappingpirate.weeklyrecipe.Databases.RecipesDataSource;
import com.nappingpirate.weeklyrecipe.RecipeFiles.AddRecipe;
import com.nappingpirate.weeklyrecipe.RecipeFiles.RVAdapter;
import com.nappingpirate.weeklyrecipe.RecipeFiles.ViewRecipe;

import java.sql.SQLException;
import java.util.Random;

public class MainActivity extends Activity {
    RVAdapter rvAdapter;
    private RecyclerView rv;
    private com.github.clans.fab.FloatingActionButton fabAdd;
    private com.github.clans.fab.FloatingActionButton fabRandom;
    FloatingActionMenu fabMenu;
    View background;
    RecipesDataSource db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Connect to Database
        db = new RecipesDataSource(this);
        try{
            db.open();
        } catch (SQLException e){
            e.printStackTrace();
        }

        rv = (RecyclerView) findViewById(R.id.rv_recipes);
        rvAdapter = new RVAdapter(this, db.getAllRecipes());
        rv.setAdapter(rvAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        background = (View) findViewById(R.id.greyBackground);
        fabMenu = (FloatingActionMenu) findViewById(R.id.fab_menu);
        fabMenu.showMenu(true);

        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dx < dy) {
                    fabMenu.hideMenu(true);
                } else if (dx > dy) {
                    fabMenu.showMenu(true);
                }
            }
        });
        fabMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (background.getVisibility() == View.VISIBLE){
                    background.setVisibility(View.GONE);
                }else {
                    background.setVisibility(View.VISIBLE);
                }
            }
        });
        fabAdd = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_add);
        fabRandom = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_random);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AddRecipe.class);
                startActivity(i);
            }
        });
        fabRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (db.getAllRecipes().size() >1) {

                    Random r = new Random();
                    //long r1 = r.nextInt((db.getAllRecipes().size()));
                    long id = db.getRandomRecipe();
                    Toast.makeText(MainActivity.this, "Random Recipe #" + id + "!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), ViewRecipe.class);
                    i.putExtra("id", id);
                    //i.putExtra("random", "random");
                    startActivity(i);
                }else{
                    showMessage("Random Recipe", "Please add a few more recipes before using the random feature");
                    //Toast.makeText(MainActivity.this, "Please Add a few more recipes before using the random feature", Toast.LENGTH_SHORT).show();
                }
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


    /*@Override
    public void itemClicked(View view, int position) {
        startActivity(new Intent(getApplicationContext(), ViewRecipe.class));
    }*/
}

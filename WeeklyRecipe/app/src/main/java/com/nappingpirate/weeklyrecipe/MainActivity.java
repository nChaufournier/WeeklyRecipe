package com.nappingpirate.weeklyrecipe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.nappingpirate.weeklyrecipe.Databases.RecipesDataSource;
import com.nappingpirate.weeklyrecipe.RecipeFiles.AddRecipe;
import com.nappingpirate.weeklyrecipe.RecipeFiles.F2fRecipe;
import com.nappingpirate.weeklyrecipe.RecipeFiles.F2fRecipeListItem;
import com.nappingpirate.weeklyrecipe.RecipeFiles.Ingredient;
import com.nappingpirate.weeklyrecipe.RecipeFiles.RVAdapter;
import com.nappingpirate.weeklyrecipe.RecipeFiles.ViewRecipe;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends Activity {
    RVAdapter rvAdapter;
    FloatingActionMenu fabMenu;
    RecipesDataSource db;
    TextView noRecipes;

    String[] f2fId = {"One", "Two", "Three"};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Connect to Database
        /*StringBuilder builder = new StringBuilder();
        for (String s : f2fId){
            builder.append(s).append("#");
        }
        builder.toString().split("");
        Log.v("Result", builder.toString());
        for (int i=0; i <builder.toString().split("#").length; i++){
            Log.v("Result", builder.toString().split("#")[i]);
        }*/

        db = new RecipesDataSource(this);
        try{
            db.open();
        } catch (SQLException e){
            e.printStackTrace();
        }

        RecyclerView rv = findViewById(R.id.rv_recipes);
        rvAdapter = new RVAdapter(this, db.getAllRecipes());
        rv.setAdapter(rvAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        fabMenu = findViewById(R.id.fab_menu);
        fabMenu.showMenu(true);

        //Shows a message if there are no recipes
        noRecipes =  findViewById(R.id.noRecipes);
        if(db.getAllRecipes() != null){
            noRecipes.setVisibility(View.GONE);
        }

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
        FloatingActionButton fabAdd = findViewById(R.id.fab_add);
        FloatingActionButton fabRandom =  findViewById(R.id.fab_random);
        FloatingActionButton fabNetwork =  findViewById(R.id.fab_network);
        FloatingActionButton fabSearch =  findViewById(R.id.fab_search);
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
                if (db.getAllRecipes().size() > 1) {

                    Random r = new Random();
                    //long r1 = r.nextInt((db.getAllRecipes().size()));
                    long id = db.getRandomRecipe();
                    Toast.makeText(MainActivity.this, "Random Recipe #" + id + "!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), ViewRecipe.class);
                    i.putExtra("id", id);
                    //i.putExtra("random", "random");
                    startActivity(i);
                } else {
                    showMessage("Random Recipe", "Please add a few more recipes before using the random feature");
                    //Toast.makeText(MainActivity.this, "Please Add a few more recipes before using the random feature", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fabNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    Toast.makeText(MainActivity.this, "Connected!", Toast.LENGTH_SHORT).show();
                    String apikey = getResources().getString(R.string.nal_api_key);
                    String apiurl = getResources().getString(R.string.nal_api_url);
                    //new DownloadWebpageTask().execute(apiurl+"format=json&lt=f&sort=n&max=10&api_key=" + apikey);
                    new DownloadWebpageTask().execute("http://food2fork.com/api/search?key=4ab22a0b23d28f4634e677a7856a9763&q=shredded%20chicken");

                } else {
                    Toast.makeText(MainActivity.this, "Not Connected!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), SearchForRecipe.class);
                startActivity(i);
            }
        });

    }
    private class DownloadWebpageTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... urls) {
            try{
                Log.e("result", urls[0].toString());
                return downloadUrl(urls[0]);
            }catch (IOException e){
                return "Unable to retrieve web page. URL may be invalid";
            }
        }

        //onPostExecute displays the results of the AsynchTask
        @Override
        protected void onPostExecute(String result) {
            Log.v("result", result);
            JsonRead jRead;
            try {
                //Where The display is changed
                jRead = new JsonRead(result);
                //showArray("Ingredient", jRead.getIngredientList());
                showF2fArray("F2F", jRead.getF2fRecipeList());
            }catch (IOException e){
                Log.e("Result", "Something is Wrong", e);
            }

        }
    }

    private class DownloadF2fJson extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... urls) {
            try{
                Log.e("result", urls[0].toString());
                return downloadUrl(urls[0]);
            }catch (IOException e){
                return "Unable to retrieve web page. URL may be invalid";
            }
        }

        //onPostExecute displays the results of the AsynchTask
        @Override
        protected void onPostExecute(String result) {
            Log.v("result", result);
            JsonRead jRead;
            try {
                //Where The display is changed
                jRead = new JsonRead(result);
                Log.e("Result", "Result: "+ result);
                Log.e("Result", "View Item: "+ jRead.getF2fRecipe().toString());
                //showArray("Ingredient", jRead.getIngredientList());
                //showF2fArray("F2F", jRead.getF2fRecipe());
                Intent intent = new Intent(getApplicationContext(), ViewRecipe.class);
                intent.putExtra("f2f", jRead.getF2fRecipe());
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "View Recipe", Toast.LENGTH_SHORT).show();
            }catch (IOException e){
                Log.e("Result", "Something is Wrong", e);
            }

        }
    }

    private String downloadUrl(String myUrl) throws IOException{
        InputStream is = null;
        //Only display the first 500 characters of the webpage
        int len = 2000000;

        try{
            URL url = new URL(myUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* millisceconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            //Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("Response", "The Response is: "+ response);
            is = conn.getInputStream();

            //Convert the inputStream into a string
            String contentAsString = readIt(is, len);

            return contentAsString;

        } finally {
            if (is != null){
                is.close();
            }
        }

    }

    public String readIt(InputStream stream, int len)throws IOException, UnsupportedEncodingException{
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //MenuInflater inflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
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

    public void showArray(String title, final ArrayList<Ingredient> message)
    {
        //final ArrayList<Ingredient> ingredientList = message;
        String[] ar = new String[message.size()];
        for (int i=0; i < message.size(); i++){
            ar[i] = message.get(i).getName();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setItems(ar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String name = message.get(i).getName();
                showMessage(name, name + "\nFood Group: " + message.get(i).getFoodGroup());


            }
        });
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    public void showF2fArray(String title, final ArrayList<F2fRecipeListItem> message)
    {
        //final ArrayList<Ingredient> ingredientList = message;
        String[] ar = new String[message.size()];
        for (int i=0; i < message.size(); i++){
            ar[i] = message.get(i).getTitle();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setItems(ar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //String name = message.get(i).getTitle();
                //showMessage(name, name+"\nFood Group: "+ message.get(i).getPublisher() );
                //Sends you to F2F Website to view Recipe
                /*
                String url = message.get(i).getUrl();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);*/

                Intent intent = new Intent(getApplicationContext(), ViewRecipe.class);
                String id = message.get(i).getRecipe_id();
                intent.putExtra("f2f", id);
                startActivity(intent);


                //new DownloadF2fJson().execute("http://food2fork.com/api/get?key=4ab22a0b23d28f4634e677a7856a9763&rId=" + id);

            }
        });
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    private boolean shouldAskPermission(){

        return(Build.VERSION.SDK_INT> Build.VERSION_CODES.LOLLIPOP_MR1);

    }



    /*@Override
    public void itemClicked(View view, int position) {
        startActivity(new Intent(getApplicationContext(), ViewRecipe.class));
    }*/
}

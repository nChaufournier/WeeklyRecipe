package com.nappingpirate.weeklyrecipe.RecipeFiles;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nappingpirate.weeklyrecipe.Databases.RecipesDataSource;
import com.nappingpirate.weeklyrecipe.JsonRead;
import com.nappingpirate.weeklyrecipe.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;

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
    CheckBox cb_ingredients;
    ProgressBar progressBar;
    F2fRecipe f2fRecipe;
    JsonRead jRead;
    View view;
    int saved = 0;
    private Menu saveMenu;
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_recipe);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        db = new RecipesDataSource(this);
        try {
            db.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        view = (View) findViewById(R.id.blankView);
        tv_recipeName = (TextView) findViewById(R.id.tv_recipeName);
        tv_recipeRating = (TextView) findViewById(R.id.tv_rating);
        tv_recipeDifficulty = (TextView) findViewById(R.id.tv_difficulty);
        tv_recipeDescription = (TextView) findViewById(R.id.tv_description);
        lv_ingredients = (ListView) findViewById(R.id.lv_ingredients);
        iv_recipeImage = (ImageView) findViewById(R.id.iv_recipeImage);
        rl_ingredients = (LinearLayout) findViewById(R.id.rl_ingredients);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        cb_ingredients = (CheckBox) findViewById(R.id.cb_ingredients);

        extras = getIntent().getExtras();
        //Toast.makeText(getApplicationContext(), extras + " ", Toast.LENGTH_SHORT).show();
        //recipe = db.getSingleRecipe(extras.getLong("id"));
        if (extras != null) {

            if (extras.containsKey("id")) {
                view.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                //Toast.makeText(getApplicationContext(), extras.toString(), Toast.LENGTH_LONG).show();
                Long id = extras.getLong("id");
                //Toast.makeText(getApplicationContext(), id + " ", Toast.LENGTH_LONG).show();
                recipe = db.getSingleRecipe(id);
                tv_recipeName.setText(recipe.getName());
                tv_recipeRating.setText((recipe.getRating()));
                tv_recipeDifficulty.setText(recipe.getDifficultyString());
                //Loops through creating more ingredients as they are needed.
                if (recipe.getIngredientArrayString() != null) {
                    for (int i = 0; i < recipe.getIngredientArrayString().size(); i++) {
                        listIngredient(recipe.getIngredientArrayString().get(i));
                    }
                }
                tv_recipeDescription.setText(recipe.getDescription());
                try {

                    iv_recipeImage.setImageBitmap(BitmapFactory.decodeFile(recipe.getImage()));
                    iv_recipeImage.setBackgroundColor(setPallete(BitmapFactory.decodeFile(recipe.getImage())));
                }catch(Exception e){
                    Log.v("Image", "Image: ", e);
                }
                iv_recipeImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //Toast.makeText(ViewRecipe.this, recipe.getIngredientArrayList()+"", Toast.LENGTH_SHORT).show();
                        Toast.makeText(ViewRecipe.this, recipe.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }
            if (extras.containsKey("f2f")) {

                /**Takes in Id an finds Recipe**/
                String f2fId = extras.getString("f2f");
                new DownloadF2fJson().execute("http://food2fork.com/api/get?key=4ab22a0b23d28f4634e677a7856a9763&rId=" + f2fId);


//                /**
//                 * Takes passed in F2fRecipe object and applys it to View Recipe
//                 **/
//                F2fRecipe f2FRecipe;
//                //Toast.makeText(getApplicationContext(), extras.toString(), Toast.LENGTH_LONG).show();
//                Long id = extras.getLong("f2f");
//                //Toast.makeText(getApplicationContext(), id + " ", Toast.LENGTH_LONG).show();
//                f2FRecipe = getIntent().getParcelableExtra("f2f");
//                tv_recipeName.setText(f2FRecipe.getTitle());
//                tv_recipeRating.setText(f2FRecipe.getPublisher());
//                tv_recipeDifficulty.setText("Working on it");
//                //cb_ingredients.setText(f2FRecipe.getIngredients());
//
//                /**
//                 * Loops through creating more ingredients as they are needed.
//                 **/
//                if (f2FRecipe != null) {
//                    for (int i = 0; i < f2FRecipe.getIngredients().length; i++) {
//                        listIngredient(f2FRecipe.getIngredients()[i]);
//                    }
//                }
//                try{
//                    InputStream is = (InputStream) new URL(f2FRecipe.getImage_url()).getContent();
//                    Drawable d = Drawable.createFromStream(is, f2FRecipe.getImage_url());
//                    iv_recipeImage.setImageDrawable(d);
//                }catch (Exception e){
//                    Log.e("Image", "Image Problem", e);
//                }
//                //tv_recipeDescription.setText(recipe.getDescription());
//
//                iv_recipeImage.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        //Toast.makeText(ViewRecipe.this, recipe.getIngredientArrayList()+"", Toast.LENGTH_SHORT).show();
//                        //Toast.makeText(ViewRecipe.this, recipe.toString(), Toast.LENGTH_LONG).show();
//                    }
//                });
            }

            //et_recipeName.setText(recipe.getName());
        } else {
            Toast.makeText(getApplicationContext(), "Didn't Work", Toast.LENGTH_LONG).show();
        }


    }

    public void listIngredient(String ingredient) {
        CheckBox et_ingredient = new CheckBox(getApplicationContext());
        rl_ingredients.addView(et_ingredient);
        et_ingredient.setText(ingredient);
        et_ingredient.setTextColor(Color.BLACK);
        et_ingredient.setMaxWidth(100);
        //et_ingredient.setMaxHeight(100);
        et_ingredient.setTextSize(18);

        et_ingredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) et_ingredient.getLayoutParams();
        layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        layoutParams.topMargin = 20;
        et_ingredient.setLayoutParams(layoutParams);
        et_ingredient.setTag("Edit Text");
    }

    public void showMessage() {

    }

    private class DownloadF2fJson extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                Log.e("result", urls[0]);
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid";
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }

        //onPostExecute displays the results of the AsynchTask
        @Override
        protected void onPostExecute(String result) {

            Log.v("result", result);

            //Where The display is changed

            view.setVisibility(View.GONE);
            tv_recipeName.setText(f2fRecipe.getTitle());
            tv_recipeDescription.setText(f2fRecipe.getPublisher());
            tv_recipeDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showAlert(f2fRecipe.getSource_url());
                }
            });
            tv_recipeDescription.setTextSize(24);
            Double socialRank = Math.floor(f2fRecipe.getSocial_rank() * 100) / 100;
            tv_recipeRating.setText("Food 2 Fork Rating: " + socialRank);
            tv_recipeRating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showAlert(f2fRecipe.getUrl());
                }
            });
            tv_recipeDifficulty.setVisibility(View.GONE);

            if (f2fRecipe != null) {
                for (int i = 0; i < f2fRecipe.getIngredients().length; i++) {
                    listIngredient(f2fRecipe.getIngredients()[i]);
                }
            }
            /**Get Image form URL and sets it to ImageView**/
            try {
                InputStream is = (InputStream) new URL(f2fRecipe.getImage_url()).getContent();
                //Drawable d = Drawable.createFromStream(is, f2fRecipe.getImage_url());
                //iv_recipeImage.setImageDrawable(d);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                iv_recipeImage.setImageBitmap(bitmap);

                Palette palette = Palette.generate(bitmap);
                int vibrant = palette.getVibrantColor(0x000000);
                int vibrantDark = palette.getDarkVibrantColor(0x000000);
                int vibrantLight = palette.getLightVibrantColor(0x000000);
                int muted = palette.getMutedColor(0x000000);
                int mutedDark = palette.getDarkMutedColor(0x000000);
                int mutedLight = palette.getLightMutedColor(0x000000);
                iv_recipeImage.setBackgroundColor(vibrantLight);
            } catch (Exception e) {
                Log.e("Image", "Image Problem", e);
            }

            /**
             * Changes Labels to fit F2f Data
             **/
            TextView label_difficulty = (TextView) findViewById(R.id.tv_label_difficulty);
            label_difficulty.setVisibility(View.GONE);
            TextView label_rating = (TextView) findViewById(R.id.tv_label_rating);
            label_rating.setText("Website");
            TextView label_description = (TextView) findViewById(R.id.tv_label_description);
            label_description.setText("Publisher");

            Log.e("Result", "Result: " + result);
            Log.e("Result", "View Item: " + jRead.getF2fRecipe().toString());
            Toast.makeText(getApplicationContext(), "View Recipe", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);


        }
    }

    private String downloadUrl(String myUrl) throws IOException {
        InputStream is = null;
        int count;
        //Only display the first 500 characters of the webpage
        int len = 2000000;

        try {
            URL url = new URL(myUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* millisceconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            //Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("Response", "The Response is: " + response);
            is = conn.getInputStream();

            byte data[] = new byte[1024];
            long total = 0;


            //Convert the inputStream into a string
            String contentAsString = readIt(is, len);
            jRead = new JsonRead(contentAsString);
            f2fRecipe = jRead.getF2fRecipe();
            return contentAsString;

        } finally {
            if (is != null) {
                is.close();
            }
        }

    }

    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    public void showAlert(final String url) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Leaving Weekly Recipe");
        builder.setMessage("You are leaving the application to go to the recipes website:\n" +
                url +
                "\n\n" +
                "Do you want to continue to website?");

        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    //TODO: Fix favorite Failing for some reason
//        saveMenu = menu;
//        getMenuInflater().inflate(R.menu.menu_save, menu);
//        if (recipe.getFavorite() == 0) {
//            menu.findItem(R.id.mbtn_save).setIcon(R.drawable.ic_action_nonfavorite);
//        }else{
//            menu.findItem(R.id.mbtn_save).setIcon(R.drawable.ic_action_action_favorite);
//        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mbtn_save:
                Toast.makeText(ViewRecipe.this, "Save Recipe!", Toast.LENGTH_SHORT).show();
                //Save/Favorite
                if (recipe.getFavorite() == 0) {
                    item.setIcon(R.drawable.ic_action_action_favorite);
                    saved = 1;
                    recipe.setFavorite(1);
                    db.editRecipe(recipe);
                }else{ //Delete/Remove from Favorite
                    item.setIcon(R.drawable.ic_action_nonfavorite);
                    saved = 0;
                    recipe.setFavorite(0);
                    db.editRecipe(recipe);
                }
                return true;
            case R.id.mbtn_edit:
                Intent editInt = new Intent(this, AddRecipe.class);
                editInt.putExtra("edit", recipe.get_id());
                startActivity(editInt);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    public int setPallete(Bitmap bitmap){
        Palette palette = Palette.generate(bitmap);
        int vibrant = palette.getVibrantColor(0x000000);
        int vibrantDark = palette.getDarkVibrantColor(0x000000);
        int vibrantLight = palette.getLightVibrantColor(0x000000);
        int muted = palette.getMutedColor(0x000000);
        int mutedDark = palette.getDarkMutedColor(0x000000);
        int mutedLight = palette.getLightMutedColor(0x000000);
        return vibrantLight;
        //iv_recipeImage.setBackgroundColor(vibrantLight);
    }
}

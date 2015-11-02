package com.nappingpirate.weeklyrecipe;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.SearchView;
import android.widget.Toast;

import com.nappingpirate.weeklyrecipe.RecipeFiles.RVAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Nic on 11/1/2015.
 */
public class SearchForRecipe extends Activity {
    private SearchView sv_searchRecipe;
    private RVSearchAdapter rvAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_recipe);
        /**Allows Asynch task to run on this activity**/
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        /**Connects and displays Recycler View**/


        sv_searchRecipe = (SearchView) findViewById(R.id.sv_search);
        sv_searchRecipe.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ConnectivityManager connMngr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMngr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()){
                    Toast.makeText(SearchForRecipe.this, "Connected!", Toast.LENGTH_SHORT).show();
                    new DownloadWebpageTask().execute("http://food2fork.com/api/search?key=4ab22a0b23d28f4634e677a7856a9763&q=" + query);
                }else{
                    Toast.makeText(SearchForRecipe.this, "Not Connected!", Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //newText.replaceAll(" ", "%");

                return false;
            }
        });


    }
    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
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
                Log.v("Result", "Get recipes: " + jRead.getF2fRecipeList().toString());
                recyclerView = (RecyclerView) findViewById(R.id.rv_recipes);
                rvAdapter = new RVSearchAdapter(getApplicationContext(), jRead.getF2fRecipeList());
                recyclerView.setAdapter(rvAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                //showF2fArray("F2F", jRead.getF2fRecipeList());
            }catch (IOException e){
                Log.e("Result", "Something is Wrong", e);
            }catch (Exception e){
                Log.e("Result Error", "Recycler View:", e);
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

        public String readIt(InputStream stream, int len)throws IOException, UnsupportedEncodingException {
            Reader reader = null;
            reader = new InputStreamReader(stream, "UTF-8");
            char[] buffer = new char[len];
            reader.read(buffer);
            return new String(buffer);
        }
    }
}

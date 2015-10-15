package com.nappingpirate.weeklyrecipe;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nic on 10/7/2015.
 */
public class GetIngredients extends Activity {

    ListView lv_ingredients;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_ingredient);

        String data="";
        lv_ingredients = (ListView) findViewById(R.id.lv_ingredients);
        String s = "";
        String myDataArray[] = {};

        try {
            JSONObject rootObject = new JSONObject(s);
            JSONArray itemJSON = rootObject.getJSONArray("item");

            myDataArray = new String[itemJSON.length()];

            for (int i = 0; i < itemJSON.length(); i++){
                JSONObject jsonObject = itemJSON.getJSONObject(i);
                myDataArray[i] = jsonObject.getString("name");
            }


        }catch (JSONException e){
            e.printStackTrace();
        }

    }
}

package com.nappingpirate.weeklyrecipe.RecipeFiles;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.nappingpirate.weeklyrecipe.Databases.IngredientsDataSource;
import com.nappingpirate.weeklyrecipe.Databases.RecipesDataSource;
import com.nappingpirate.weeklyrecipe.MainActivity;
import com.nappingpirate.weeklyrecipe.R;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Nic on 10/1/2015. This is an activity that lets you create new recipes
 */
public class AddRecipe extends Activity {

    private ToggleButton btn_easy;
    private ToggleButton btn_medium;
    private ToggleButton btn_hard;
    private EditText et_name;
    private EditText et_difficulty;
    private RatingBar rb_rating;
    private EditText et_description;
    //private EditText et_time;
    private EditText et_lastDateMade;
    private EditText et_dateAdded;
    private EditText et_mainIngredient;
    private EditText et_image;
    private EditText et_comment;
    private String date;
    LinearLayout rl_ingredients;
    ListView lv_ingredient;
    private RecipesDataSource db;
    private IngredientsDataSource ingDb;
    private Bundle extras;
    Recipe editRecipe;
    String[] sampleArray = {
            "Meat", "Onions", "Peppers"
    };
    ArrayList<Ingredient> ingredientsArray = new ArrayList<>();


    //For Dialog
    EditText et_ing_name;
    TextView tv_foodGroup;
    ToggleButton tb_liqsol;
    EditText et_ing_description;

    NumberPicker numberPickerHour;
    NumberPicker numberPickerMin;
    NumberPicker numberPickerSec;
    String timeTaken;
    int hour, min, sec;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_recipe);
        getActionBar().hide();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        db = new RecipesDataSource(this);
        ingDb = new IngredientsDataSource(this);
        try {
            db.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            ingDb.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Ingredient addDb = new Ingredient();
        addDb.setName("");



        //Get and set extras from last event
        extras = getIntent().getExtras();

        //Current date
        date = new SimpleDateFormat("MM-dd-yyyy", Locale.US).format(new Date());

        rl_ingredients = (LinearLayout) findViewById(R.id.rl_ingredientsLayout);

        //Buttons
        ImageButton btn_close = (ImageButton) findViewById(R.id.btn_close);
        Button btn_save = (Button) findViewById(R.id.btn_save);
        Button btn_view = (Button) findViewById(R.id.btn_view);
        ImageButton btn_delete = (ImageButton) findViewById(R.id.btn_delete);
        if (extras!= null){
            //Do Nothing
            btn_delete.setVisibility(View.VISIBLE);
        }else{
            btn_delete.setVisibility(View.GONE);
        }
        Button btn_addIngredients = (Button) findViewById(R.id.btn_addIngredient);
        btn_easy = (ToggleButton)findViewById(R.id.tb_easy);
        btn_medium = (ToggleButton)findViewById(R.id.tb_medium);
        btn_hard = (ToggleButton)findViewById(R.id.tb_hard);

        //Input Fields
        et_name = (EditText) findViewById(R.id.et_recipeName);
        et_difficulty = (EditText) findViewById(R.id.et_difficulty);
        rb_rating = (RatingBar) findViewById(R.id.rb_rating);
        //Sets Rating bar stars to yellow ..or not.
        /*
        Drawable progress = rb_rating.getProgressDrawable();
        DrawableCompat.setTint(progress, R..colorPrimaryLight);*/

        et_description = (EditText) findViewById(R.id.et_description);
        //et_time = (EditText) findViewById(R.id.et_time);
        et_mainIngredient = (EditText) findViewById(R.id.et_mainIngredient);
        et_mainIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopUp2(view, ingredientsArray);
            }
        });

        et_image = (EditText) findViewById(R.id.et_image);
        et_comment = (EditText) findViewById(R.id.et_comment);

        //Other Fields
        lv_ingredient = (ListView) findViewById(R.id.lv_ingredients);
        //Time Picker
        numberPickerHour = (NumberPicker)findViewById(R.id.np_hour);
        numberPickerHour.setMinValue(0);
        numberPickerHour.setMaxValue(60);
        numberPickerHour.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                hour = i1;
                //timeTaken = timeTaken + hour + "hr";
                timeTaken = hour+"hr"+min+"min"+sec+"sec";
            }
        });
        numberPickerMin = (NumberPicker)findViewById(R.id.np_minutes);
        numberPickerMin.setMinValue(0);
        numberPickerMin.setMaxValue(60);
        numberPickerMin.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                min = i1;
                //timeTaken = timeTaken + hour+"hr";
                timeTaken = hour+"hr"+min+"min"+sec+"sec";
            }
        });
        numberPickerSec = (NumberPicker)findViewById(R.id.np_seconds);
        numberPickerSec.setMinValue(0);
        numberPickerSec.setMaxValue(60);
        numberPickerSec.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                sec = i1;
                //timeTaken = timeTaken+hour+"hr";
                timeTaken = hour + "hr" + min + "min" + sec + "sec";
            }
        });


        timeTaken = hour+"hr"+min+"min"+sec+"sec";




        //If extras set items to correct text
        if (extras!=null){
            //Toast.makeText(AddRecipe.this, extras.toString(), Toast.LENGTH_SHORT).show();
            //et_name.setText(extras.);
            if(extras.containsKey("edit")){
                Long id = extras.getLong("edit");
                editRecipe = db.getSingleRecipe(id);
                et_name.setText(editRecipe.getName());
                et_difficulty.setText(editRecipe.getDifficultyString());
                if (editRecipe.getDifficulty()== 0){
                    btn_easy.setChecked(true);
                    et_difficulty.setText("0");
                }else if (editRecipe.getDifficulty() == 1){
                    btn_medium.setChecked(true);
                    et_difficulty.setText("1");
                }else if (editRecipe.getDifficulty() == 2){
                    btn_hard.setChecked(true);
                    et_difficulty.setText("2");
                }
                rb_rating.setRating(editRecipe.getRatingInt());
                et_description.setText(editRecipe.getDescription());
                numberPickerHour.setValue(editRecipe.getHour());
                hour = editRecipe.getHour();
                numberPickerMin.setValue(editRecipe.getMinutes());
                min = editRecipe.getMinutes();
                numberPickerSec.setValue(editRecipe.getSeconds());
                sec = editRecipe.getSeconds();
                timeTaken = hour + "hr" + min + "min" + sec + "sec";
                try {
                    Log.v("Loop1", editRecipe.getIngredientArrayString().toString());
                    for (int i = 0; i < editRecipe.getIngredientArrayString().size(); i++) {
                        Log.v("Loop2", editRecipe.getIngredientArrayString().get(i));
                        //editRecipe.getIngredientArrayString().get(i);

                        listIngredient(ingDb.getIngredientByName(editRecipe.getIngredientArrayString().get(i)));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(AddRecipe.this, "Nope", Toast.LENGTH_SHORT).show();
                }
                //et_time.setText(editRecipe.getTime());
                et_mainIngredient.setText(editRecipe.getMainIngredient());
                et_image.setText(editRecipe.getImage());
                et_comment.setText(editRecipe.getComment());


            }
        }

        btn_addIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addIngredientMenu();

            }
        });
        //Makes it so only one option is selected for the description buttons
        btn_easy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    btn_medium.setChecked(false);
                    btn_hard.setChecked(false);
                    et_difficulty.setText("0");
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
                    et_difficulty.setText("1");
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
                    et_difficulty.setText("2");
                }else{
                    btn_hard.setChecked(false);
                }
            }
        });



        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(AddRecipe.this, et_name.getText(), Toast.LENGTH_SHORT).show();

                //Toast.makeText(AddRecipe.this, rb_rating.getRating() + "", Toast.LENGTH_SHORT).show();

                if (!et_name.getText().toString().equals("")) {
                    Recipe newRecipe = new Recipe();
                    if (extras != null) {
                        Long value = extras.getLong("edit");
                        newRecipe.set_id(value);
                    }
                    newRecipe.setName(et_name.getText().toString());
                    if (btn_easy.isChecked() || btn_medium.isChecked() || btn_hard.isChecked()) {
                        newRecipe.setDifficulty(Integer.parseInt(et_difficulty.getText().toString()));
                    }
                    newRecipe.setRating((int) rb_rating.getRating());
                    newRecipe.setDescription(et_description.getText().toString());
                    newRecipe.setTime(timeTaken);
                    newRecipe.setLastDateMade(null);
                    newRecipe.setIngredientArrayList(ingredientsArray);
                    newRecipe.setDateAdded(date);
                    newRecipe.setMainIngredient(et_mainIngredient.getText().toString());
                    newRecipe.setImage(null);
                    newRecipe.setComment(et_comment.getText().toString());

                    //showMessage("Recipe", newRecipe.toString());
                    if (extras != null) {//Check if editing or adding
                        db.editRecipe(newRecipe);
                    } else {
                        db.createRecipe(newRecipe);
                    }


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
                /*ingDaBa = new IngredientsDB(getApplicationContext());
                try {
                    ingDaBa.copyDataBaseFromAsset();
                    Toast.makeText(AddRecipe.this, "Success!", Toast.LENGTH_SHORT).show();
                }catch (IOException e){
                    e.printStackTrace();
                    Toast.makeText(AddRecipe.this, "Failure!", Toast.LENGTH_SHORT).show();
                }*/
                if (!ingredientsArray.isEmpty()) {
                    Toast.makeText(AddRecipe.this, "Ingredients:" + ingredientsArray + "\n", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(AddRecipe.this, "Hour: "+ hour +"\n" +
                                                "Minutes: " + min +"\n" +
                                                "Seconds: " + sec +"\n" +
                                                "Total: " + timeTaken +"\n", Toast.LENGTH_SHORT).show();
                //showMessage("All Items", ingDb.().toString());
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteMessage(editRecipe.getName());
                /*db.deleteRecipe(extras.getLong("edit"));
                Intent i = new Intent(view.getContext(), MainActivity.class);
                startActivity(i);
                Toast.makeText(AddRecipe.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();*/
            }
        });
    }

    public Dialog addIngredient() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final LayoutInflater inflater = this.getLayoutInflater();
        View vInflate = inflater.inflate(R.layout.add_ingredient, null);


        builder.setView(vInflate);
        builder.setTitle("Create Ingredient");

        et_ing_name = (EditText)  vInflate.findViewById(R.id.et_ingredientName);
        tv_foodGroup = (TextView) vInflate.findViewById(R.id.tv_foodGroup);
        tb_liqsol = (ToggleButton) vInflate.findViewById(R.id.tb_liqsol);
        et_ing_description = (EditText) vInflate.findViewById(R.id.et_ing_description);

        tv_foodGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(AddRecipe.this, "Food Groups", Toast.LENGTH_SHORT).show();
                showPopUp(view);

            }
        });

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Ingredient ingredient = new Ingredient();
                ingredient.setName(et_ing_name.getText().toString());
                ingredient.setFoodGroup(tv_foodGroup.getText().toString());//needs to create a new dialog menu to select from
                if (tb_liqsol.isChecked()) {
                    ingredient.setLiquidSolid(0);
                } else {
                    ingredient.setLiquidSolid(1);
                }
                ingredient.setDescription(et_ing_description.getText().toString());
                ingDb.createIngredient(ingredient);
                listIngredient(ingredient);
                ingredientsArray.add(ingredient);
                Toast.makeText(AddRecipe.this, ingredient.getName()+" Added!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
        return builder.create();
    }



    public void deleteMessage(String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure you want to delete recipe \"" + message + "\"?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                db.deleteRecipe(extras.getLong("edit"));
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                Toast.makeText(AddRecipe.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    public void addIngredientMenu()
    {
        final String[] ingredient = getResources().getStringArray(R.array.ingredientGroups_Array);
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Add Ingredient");
        builder.setItems(R.array.ingredientGroups_Array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {
                showArray(ingredient[position], ingDb.getIngredientsByFoodGroup(ingredient[position]));
                //Toast.makeText(AddRecipe.this, ingredient[position], Toast.LENGTH_SHORT).show();
            }
        });
        /*builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });*/
        builder.show();
    }

    public void showArray(String title, final List<Ingredient> message)
    {
        final List<Ingredient> ingredientList = message;
        String[] ar = new String[ingredientList.size()];
        for (int i=0; i < ingredientList.size(); i++){
            ar[i] = ingredientList.get(i).getName();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);

        builder.setItems(ar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listIngredient(ingredientList.get(i));
                //ingredientsArray.add(ingredientList.get(i));
            }
        });
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Toast.makeText(AddRecipe.this, "Add New Ingredient", Toast.LENGTH_SHORT).show();
                addIngredient();

                //CreateIngredientFragment newFragment = new CreateIngredientFragment();
                //newFragment.show(getFragmentManager(), "dialog");
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


    public void showPopUp(View view){
        PopupMenu popupMenu = new PopupMenu(this,view);
        String[] array = getResources().getStringArray(R.array.ingredientGroups_Array);
        for (int i=0; i < getResources().getStringArray(R.array.ingredientGroups_Array).length; i++){
            popupMenu.getMenu().add(Menu.NONE, i + 1, Menu.NONE, array[i]);

        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                //Toast.makeText(AddRecipe.this, menuItem + "", Toast.LENGTH_SHORT).show();
                tv_foodGroup.setText(menuItem.toString());
                return false;
            }
        });
        popupMenu.show();

    }

    public void showPopUp2(View view, ArrayList<Ingredient> arrayList){
        PopupMenu popupMenu = new PopupMenu(this,view);
        arrayList.size();
        for (int i=0; i < arrayList.size(); i++){
            String temp = arrayList.get(i).getName();
            popupMenu.getMenu().add(Menu.NONE, i + 1, Menu.NONE, temp);

        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                //Toast.makeText(AddRecipe.this, menuItem + "", Toast.LENGTH_SHORT).show();
                et_mainIngredient.setText(menuItem.toString());
                return false;
            }
        });
        popupMenu.show();

    }

    public void listIngredient(final Ingredient ingredient){

        EditText et_ingredient = new EditText(getApplicationContext());
        rl_ingredients.addView(et_ingredient);
        et_ingredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert(ingredient.getName(), "Food Group: " + ingredient.getFoodGroup() + "\n" +
                        "Description: " + ingredient.getDescription(), view);
            }
        });
        et_ingredient.setId(et_ingredient.generateViewId());
        et_ingredient.setFocusable(false);
        et_ingredient.setText(ingredient.getName());
        et_ingredient.setTextColor(Color.BLACK);
        et_ingredient.setMaxWidth(100);
        et_ingredient.setMaxHeight(100);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) et_ingredient.getLayoutParams();
        layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        et_ingredient.setLayoutParams(layoutParams);
        et_ingredient.setTag("Edit Text");
        ingredientsArray.add(ingredient);
    }

    public void showAlert(String title,String message, View v)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                     addIngredientMenu();
            }
        });
        builder.setNegativeButton("Remove", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(AddRecipe.this, "Item Removed", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setMessage(message);
        builder.show();
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



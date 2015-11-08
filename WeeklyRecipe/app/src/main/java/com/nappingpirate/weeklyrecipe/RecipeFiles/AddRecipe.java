package com.nappingpirate.weeklyrecipe.RecipeFiles;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.Toolbar;

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
    private TextView tv_time;
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
    AlertDialog alert;


    ArrayList<Ingredient> ingredientsArray = new ArrayList<>();

    //For Camera
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int LOAD_IMAGE_ACTIVITY_REQUEST_CODE = 200;
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
        //getActionBar().hide();

        //Get and set extras from last event
        extras = getIntent().getExtras();
        try {
            getActionBar().setIcon(R.drawable.ic_action_cancel);
            if (extras != null) {
                getActionBar().setTitle("Edit Recipe");

            }else {
                getActionBar().setTitle("Create Recipe");
            }
            getActionBar().show();
        }catch (Exception e){
            Log.e("AddRecipe", "Action bar failed", e);
        }
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





        //Current date
        date = new SimpleDateFormat("MM-dd-yyyy", Locale.US).format(new Date());

        rl_ingredients = (LinearLayout) findViewById(R.id.rl_ingredientsLayout);

        //Buttons
        Button btn_view = (Button) findViewById(R.id.btn_view);
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

        /**
         * Time Picker
         * **/
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder timeDialog = new AlertDialog.Builder(AddRecipe.this);

                LayoutInflater inflater = AddRecipe.this.getLayoutInflater();
                final View timeView = inflater.inflate(R.layout.time_dialog, null);
                timeDialog.setTitle("Average Recipe Time");
                timeDialog.setView(timeView);



                numberPickerHour = (NumberPicker) timeView.findViewById(R.id.np_hour);
                numberPickerHour.setMinValue(0);
                numberPickerHour.setMaxValue(60);
                numberPickerHour.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                        hour = i1;
                        //timeTaken = timeTaken + hour + "hr";
                        timeTaken = hour + "hr " + min + "min " + sec + "sec";
                    }
                });

                numberPickerMin = (NumberPicker) timeView.findViewById(R.id.np_minutes);
                numberPickerMin.setMinValue(0);
                numberPickerMin.setMaxValue(60);
                numberPickerMin.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                        min = i1;
                        //timeTaken = timeTaken + hour+"hr";
                        timeTaken = hour + "hr " + min + "min " + sec + "sec";
                    }
                });

                numberPickerSec = (NumberPicker) timeView.findViewById(R.id.np_seconds);
                numberPickerSec.setMinValue(0);
                numberPickerSec.setMaxValue(60);
                numberPickerSec.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                        sec = i1;
                        //timeTaken = timeTaken+hour+"hr";
                        timeTaken = hour + "hr " + min + "min " + sec + "sec";
                    }
                });

                timeDialog.setPositiveButton("Set Time", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tv_time.setText(timeTaken);
                    }
                });

                timeDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                timeDialog.show();
            }
        });
        /*
        timeTaken = hour+"hr"+min+"min"+sec+"sec";
        */
        /**End Time Picker**/

        et_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder camera = new AlertDialog.Builder(AddRecipe.this);
                LayoutInflater inflater = AddRecipe.this.getLayoutInflater();
                final View cameraView = inflater.inflate(R.layout.camera_dialog, null);
                camera.setView(cameraView);
                camera.setTitle("Select Image");
                ImageView fileSelect = (ImageView) cameraView.findViewById(R.id.iv_select_file);
                fileSelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(cameraIntent, LOAD_IMAGE_ACTIVITY_REQUEST_CODE);
                        Toast.makeText(AddRecipe.this, "File", Toast.LENGTH_SHORT).show();

                    }
                });

                ImageView takePicture = (ImageView) cameraView.findViewById(R.id.iv_take_picture);
                takePicture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                        Toast.makeText(AddRecipe.this, "Camera", Toast.LENGTH_SHORT).show();
                    }
                });
                /*camera.setItems(new CharSequence[]{"Camera", "File"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                            Toast.makeText(AddRecipe.this, "Camera", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(cameraIntent, LOAD_IMAGE_ACTIVITY_REQUEST_CODE);
                            Toast.makeText(AddRecipe.this, "File", Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/
                alert = camera.create();
                camera.show();
                /**/
            }
        });






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
                try {
                    //numberPickerHour.setValue(editRecipe.getHour());
                    hour = editRecipe.getHour();
                    //numberPickerMin.setValue(editRecipe.getMinutes());
                    min = editRecipe.getMinutes();
                    //numberPickerSec.setValue(editRecipe.getSeconds());
                    sec = editRecipe.getSeconds();
                }catch (Exception e){
                    Log.e("Time", "Time is Broken", e);
                }
                tv_time.setText(editRecipe.getTime());
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
                if (b) {
                    btn_medium.setChecked(false);
                    btn_hard.setChecked(false);
                    et_difficulty.setText("0");
                } else {
                    btn_easy.setChecked(false);
                }
            }
        });
        btn_medium.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    btn_easy.setChecked(false);
                    btn_hard.setChecked(false);
                    et_difficulty.setText("1");
                } else {
                    btn_medium.setChecked(false);
                }
            }
        });
        btn_hard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    btn_medium.setChecked(false);
                    btn_easy.setChecked(false);
                    et_difficulty.setText("2");
                } else {
                    btn_hard.setChecked(false);
                }
            }
        });



        btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(AddRecipe.this, "Debug Button", Toast.LENGTH_SHORT).show();
                Toast.makeText(AddRecipe.this, ingredientsArray.toString(), Toast.LENGTH_LONG).show();
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
                //ingredientsArray.add(ingredient);
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
        CheckBox et_ingredient = new CheckBox(getApplicationContext());
        rl_ingredients.addView(et_ingredient);
        et_ingredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert(ingredient.getName(), ingredient, view);
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

    public void showAlert(String title, final Ingredient ingredient, final View v)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage("Food Group: " + ingredient.getFoodGroup());
        builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                CheckBox chxBox = (CheckBox) findViewById(v.getId());
                chxBox.setVisibility(View.GONE);
                ingredientsArray.remove(ingredient);
                addIngredientMenu();

            }
        });
        builder.setNegativeButton("Remove", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (ingredientsArray.contains(ingredient)) {
                    CheckBox chxBox = (CheckBox) findViewById(v.getId());
                    chxBox.setVisibility(View.GONE);
                    ingredientsArray.remove(ingredient);
                    Toast.makeText(AddRecipe.this, "Delete", Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(AddRecipe.this, "Item Removed: " + ingredientsArray.get(i).getName(), Toast.LENGTH_SHORT).show();
            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        alert.cancel();
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE || requestCode == LOAD_IMAGE_ACTIVITY_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                Toast.makeText(this, "Image Saves to " + data.getData(), Toast.LENGTH_LONG).show();
                //String image = data.getDataString().replace("content://", "");
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                try {
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null,null,null);

                    cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                et_image.setText(picturePath);
                }catch (NullPointerException e){
                    Log.e("Error", "Null Pointer: ", e);
                }
            }else if (resultCode == RESULT_CANCELED){
                Toast.makeText(this, "Camera Canceled", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Image Capture Failed", Toast.LENGTH_SHORT).show();
            }
        }else{

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recipe_add_edit, menu);
        if (extras != null){
            menu.findItem(R.id.mbtn_delete).setVisible(true);
        }else{
            menu.findItem(R.id.mbtn_delete).setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mbtn_save:
                Toast.makeText(AddRecipe.this, "Save Recipe", Toast.LENGTH_SHORT).show();
                /**Create a recipe from filled out fields and then adds it to the Database**/
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
                    newRecipe.setImage(et_image.getText().toString());
                    newRecipe.setComment(et_comment.getText().toString());

                    //showMessage("Recipe", newRecipe.toString());
                    if (extras != null) {//Check if editing or adding
                        db.editRecipe(newRecipe);
                    } else {
                        db.createRecipe(newRecipe);
                    }


                    Toast.makeText(getApplicationContext(), "Recipe Added!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(AddRecipe.this, "Please add Content", Toast.LENGTH_SHORT).show();
                }


                return true;
            case R.id.mbtn_delete:
                deleteMessage(editRecipe.getName());
                Toast.makeText(AddRecipe.this, "Delete Recipe", Toast.LENGTH_SHORT).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}



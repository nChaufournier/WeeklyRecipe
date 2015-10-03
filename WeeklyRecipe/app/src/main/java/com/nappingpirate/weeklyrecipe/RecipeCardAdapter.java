package com.nappingpirate.weeklyrecipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;


/**
 * Created by Nic on 10/2/2015.
 */
public class RecipeCardAdapter extends ArrayAdapter<Recipe> {
    Context mContext;
    int mLayoutResourceId;
    List<Recipe> mData = null;

    public RecipeCardAdapter(Context context, int resource, List<Recipe> data) {
        super(context, resource, data);
        this.mContext = context;
        this.mLayoutResourceId = resource;
        this.mData = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        //Inflate the layout for a single row
        LayoutInflater inflater = LayoutInflater.from(mContext);
        row = inflater.inflate(mLayoutResourceId, parent, false);

        //Get a reference to different view elements I want to update
        TextView recipeNameView = (TextView) row.findViewById(R.id.tv_recipeName);
        TextView ratingView = (TextView) row.findViewById(R.id.tv_rating);
        final TextView descriptionView = (TextView) row.findViewById(R.id.tv_description);
        ImageButton expandDescription = (ImageButton) row.findViewById(R.id.btn_more);

        expandDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (descriptionView.getVisibility() == View.VISIBLE) {
                    descriptionView.setVisibility(View.GONE);
                } else {
                    descriptionView.setVisibility(View.VISIBLE);
                }
            }
        });

        //Get Data from data array
        Recipe recipe = mData.get(position);

        //Setting the view to refelect the data we need to display
        recipeNameView.setText(recipe.getName());
        ratingView.setText(recipe.getRating().toString()+ "/10");
        descriptionView.setText(recipe.getDescription());

        return row;
    }
}

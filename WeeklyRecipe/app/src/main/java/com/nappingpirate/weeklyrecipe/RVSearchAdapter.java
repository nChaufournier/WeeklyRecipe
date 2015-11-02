package com.nappingpirate.weeklyrecipe;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nappingpirate.weeklyrecipe.RecipeFiles.F2fRecipe;
import com.nappingpirate.weeklyrecipe.RecipeFiles.F2fRecipeListItem;
import com.nappingpirate.weeklyrecipe.RecipeFiles.ViewRecipe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Nic on 11/1/2015.
 */
public class RVSearchAdapter extends RecyclerView.Adapter<RVSearchAdapter.RecipeViewHolder> {
    List<F2fRecipeListItem> recipes = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;

    public RVSearchAdapter(Context context, ArrayList<F2fRecipeListItem> recipes) {
        this.recipes = recipes;
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.f2f_recipe_card, parent, false);
        RecipeViewHolder rvh = new RecipeViewHolder(v);
        return rvh;
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        final F2fRecipeListItem currentRecipe = recipes.get(position);
        holder.recipeName.setText(currentRecipe.getTitle());
        holder.btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewRecipe.class);
                String id = currentRecipe.getRecipe_id();
                intent.putExtra("f2f", id);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                Log.v("Btn", currentRecipe.getTitle());
            }
        });
        holder.thumbnail.setImageBitmap(currentRecipe.getThumbnail());
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView cv;
        TextView recipeName;
        Button btn_view;
        ImageView thumbnail;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.card_view);
            recipeName = (TextView) itemView.findViewById(R.id.tv_recipeName);
            btn_view = (Button) itemView.findViewById(R.id.btn_view);
            thumbnail = (ImageView) itemView.findViewById(R.id.iv_mainImage);
        }

        @Override
        public void onClick(View view) {

        }
    }
}



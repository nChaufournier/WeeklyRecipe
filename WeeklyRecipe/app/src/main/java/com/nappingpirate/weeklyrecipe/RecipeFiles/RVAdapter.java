package com.nappingpirate.weeklyrecipe.RecipeFiles;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nappingpirate.weeklyrecipe.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by Nic on 10/6/2015.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.RecipeViewHolder> {

    List<Recipe> recipes = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;
    private int pos;
    //private ClickListener clickListener;
    public RVAdapter(Context context, List<Recipe> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.recipes = data;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.recipe_card, parent, false);
        RecipeViewHolder rvh = new RecipeViewHolder(v);
        return rvh;
    }

    @Override
    public void onBindViewHolder(final RecipeViewHolder holder, final int position) {
        final Recipe currentRecipe = recipes.get(position);
        holder.recipeName.setText(currentRecipe.getName());
        holder.recipeRating.setText(currentRecipe.getRating());
        holder.recipeDescription.setText(currentRecipe.getDescription());
        holder.btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "" + pos, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(context, ViewRecipe.class);
                i.putExtra("id", currentRecipe.get_id());
                context.startActivity(i);
            }
        });
        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, AddRecipe.class);
                i.putExtra("edit", currentRecipe.get_id());
                context.startActivity(i);
            }
        });
        holder.btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.recipeDescription.getVisibility() == View.VISIBLE){
                    holder.recipeDescription.setVisibility(View.GONE);
                }else{
                    holder.recipeDescription.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    /*public void setClickListener(ClickListener clickListener){
        this.clickListener=clickListener;
    }*/

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cv;
        TextView recipeName;
        TextView recipeRating;
        TextView recipeDescription;
        Button btn_view;
        Button btn_edit;
        ImageButton btn_more;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.card_view);

            recipeName = (TextView) itemView.findViewById(R.id.tv_recipeName);
            recipeRating = (TextView) itemView.findViewById(R.id.tv_rating);
            recipeDescription = (TextView) itemView.findViewById(R.id.tv_description);
            btn_view = (Button) itemView.findViewById(R.id.btn_view);
            btn_edit = (Button) itemView.findViewById(R.id.btn_edit);
            btn_more = (ImageButton) itemView.findViewById(R.id.btn_more);
        }

        @Override
        public void onClick(View view) {

        }
    }
}

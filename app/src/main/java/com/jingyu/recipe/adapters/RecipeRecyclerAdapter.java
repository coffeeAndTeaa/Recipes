package com.jingyu.recipe.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.jingyu.recipe.R;
import com.jingyu.recipe.models.Recipe;
import com.jingyu.recipe.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int RECIPE_TYPE = 1;
    private static final int LOADING_TYPE = 2;
    private static final int CATEGORY_TYPE = 3;
    private static final int EXHAUSTED_TYPE = 4;

    private List<Recipe> mRecipes;
    private OnRecipeListener mOnRecipeListener;
    private RequestManager requestManager;

    public RecipeRecyclerAdapter(OnRecipeListener mOnRecipeListener, RequestManager requestManager) {
        this.mOnRecipeListener = mOnRecipeListener;
        this.requestManager = requestManager;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        switch(viewType) {

            case RECIPE_TYPE:{
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recipe_list_item, parent,false);
                return new RecipeViewHolder(view, mOnRecipeListener, requestManager);
            }

            case LOADING_TYPE:{
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_loading_list_item, parent, false);
                return new LoadingViewHolder(view);
            }

            case CATEGORY_TYPE:{
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_category_list_item, parent, false);
                return new CategoryViewHolder(view, mOnRecipeListener, requestManager);
            }

            case EXHAUSTED_TYPE:{
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_search_exhausted, parent, false);
                return new SearchExhaustedViewHolder(view);
            }

            default:{
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recipe_list_item, parent, false);
                return new RecipeViewHolder(view, mOnRecipeListener, requestManager);
            }
        }



    }

    public void setQueryExhausted(){
        hideLoading();
        Recipe exhaustedRecipe = new Recipe();
        exhaustedRecipe.setTitle("EXHAUSTED...");
        mRecipes.add(exhaustedRecipe);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        int itemViewType = getItemViewType(position);
        if (itemViewType == RECIPE_TYPE) {
            // set the image
            ((RecipeViewHolder) holder).onBind(mRecipes.get(position));
        }
        else if (itemViewType == CATEGORY_TYPE) {
            ((CategoryViewHolder)holder).onBind(mRecipes.get(position));
        }

    }

    @Override
    public int getItemCount() {
        if (mRecipes != null) {
            return mRecipes.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if(mRecipes.get(position).getSocialUrl() == -1){
            return CATEGORY_TYPE;
        }
        else if(mRecipes.get(position).getTitle().equals("LOADING...")){
            return LOADING_TYPE;
        }
        else if(mRecipes.get(position).getTitle().equals("EXHAUSTED...")) {
            return EXHAUSTED_TYPE;
        }
        else{
            return RECIPE_TYPE;
        }
    }

    public void hideLoading(){
        if(isLoading()) {
            if (mRecipes.get(0).getTitle().equals("LOADING...")) {
                mRecipes.remove(mRecipes.size() - 1);
            }
        }
        if(isLoading()){
            if(mRecipes.get(mRecipes.size() - 1).getTitle().equals("LOADING...")){
                mRecipes.remove(mRecipes.size() - 1);
            }
        }
        notifyDataSetChanged();
    }

    private void clearRecipesList(){
        if(mRecipes == null){
            mRecipes = new ArrayList<>();
        }
        else {
            mRecipes.clear();
        }
        notifyDataSetChanged();
    }


    // this one is for doing the search while nothing comes back
    public void displayOnlyLoading(){
        clearRecipesList();
        Recipe recipe = new Recipe();
        recipe.setTitle("LOADING...");
        mRecipes.add(recipe);
        notifyDataSetChanged();
    }

    // this one is for pagination
    public void displayLoading(){
        if(mRecipes == null){
            mRecipes = new ArrayList<>();
        }
        if(!isLoading()){
            Recipe recipe = new Recipe();
            recipe.setTitle("LOADING...");
            mRecipes.add(recipe); // loading at bottom of screen
            notifyDataSetChanged();
        }
    }

    private boolean isLoading(){
        if (mRecipes != null) {
            if (mRecipes.size() > 0) {
                if (mRecipes.get(mRecipes.size() - 1).getTitle().equals("LOADING...")){
                    return true;
                }
            }
        }
        return false;
    }

    public void setRecipes(List<Recipe> recipes) {
        mRecipes = recipes;
        notifyDataSetChanged();
    }

    public void displaySearchCategories(){
        List<Recipe> categories = new ArrayList<>();
        for (int i = 0; i < Constants.DEFAULT_SEARCH_CATEGORIES.length; i++) {
            Recipe recipe = new Recipe();
            recipe.setTitle(Constants.DEFAULT_SEARCH_CATEGORIES[i]);
            recipe.setImageUrl(Constants.DEFAULT_SEARCH_CATEGORY_IMAGES[i]);
            recipe.setSocialUrl(-1);
            categories.add(recipe);
        }
        mRecipes = categories;
        notifyDataSetChanged();
    }

    public Recipe getSelectedRecipe(int position) {
        if (mRecipes != null) {
            if(mRecipes.size() > 0){
                return mRecipes.get(position);
            }
        }
        return null;
    }


}

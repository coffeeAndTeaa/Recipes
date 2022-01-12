package com.jingyu.recipe.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jingyu.recipe.models.Recipe;
import com.jingyu.recipe.repository.RecipeRepository;

import java.util.List;

public class RecipeListViewModel extends ViewModel {

    private RecipeRepository mRecipeRepository;
    private boolean mIsViewingRecipes;
    private boolean mIsPerformingQuery;

    public boolean ismIsPerformingQuery() {
        return mIsPerformingQuery;
    }

    public void setmIsPerformingQuery(boolean mIsPerformingQuery) {
        this.mIsPerformingQuery = mIsPerformingQuery;
    }

    public RecipeListViewModel() {
        mRecipeRepository = RecipeRepository.getInstance();
    }

    public LiveData<List<Recipe>> getRecipes(){
        return mRecipeRepository.getRecipes();
    }

    public void searchRecipesApi(String query, int pageNumber) {
        mIsViewingRecipes = true;
        mIsPerformingQuery = true;
        mRecipeRepository.searchRecipesApi(query, pageNumber);
    }

    public boolean isViewingRecipes(){
        return mIsViewingRecipes;
    }

    public void setmIsViewingRecipes(boolean isViewingRecipes) {
        mIsViewingRecipes = isViewingRecipes;
    }

    public boolean onBackPressed(){
        if (mIsPerformingQuery) {
            // cancel the query
            mRecipeRepository.cancelRequest();
            mIsPerformingQuery = false;
        }

        if (mIsViewingRecipes) {
            mIsViewingRecipes = false;
            return false;
        }
        return true;
    }

    public void searchNextPage(){
        if(!mIsPerformingQuery && mIsViewingRecipes) {
            mRecipeRepository.searchNextPage();
        }
    }

 }

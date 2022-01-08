package com.jingyu.recipe.requests;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.jingyu.recipe.models.Recipe;

import java.lang.invoke.MutableCallSite;
import java.util.List;

public class RecipeApiClient {

    private static RecipeApiClient instance;
    private RecipeApiClient mRecipeApiClient;

    public static RecipeApiClient getInstance() {
        if (instance == null) {
            instance = new RecipeApiClient();
        }
        return instance;
    }

    private RecipeApiClient() {
        mRecipeApiClient = RecipeApiClient.getInstance();
    }

    public LiveData<List<Recipe>> getRecipes(){
        return mRecipeApiClient.getRecipes();
    }
}

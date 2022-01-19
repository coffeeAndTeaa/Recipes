package com.jingyu.recipe.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.jingyu.recipe.models.Recipe;
import com.jingyu.recipe.repository.RecipeRepository;

public class RecipeViewModel extends ViewModel {

    private RecipeRepository mRecipeRepository;
    private String mId;

    public RecipeViewModel(){
        this.mRecipeRepository = RecipeRepository.getInstance();
    }

    public LiveData<Recipe> getRecipe(){
        return mRecipeRepository.getRecipe();
    }

    public void searchRecipeById(String rId) {
        mId = rId;
        mRecipeRepository.searchRecipeById(rId);
    }

    public String getId(){
        return mId;
    }

}

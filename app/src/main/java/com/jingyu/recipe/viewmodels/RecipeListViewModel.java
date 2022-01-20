package com.jingyu.recipe.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.jingyu.recipe.models.Recipe;
import com.jingyu.recipe.repositories.RecipeRepository;
import com.jingyu.recipe.util.Resource;

import java.util.List;

public class RecipeListViewModel extends AndroidViewModel {

    private static final String TAG = "RecipeListViewModel";
    private MediatorLiveData<Resource<List<Recipe>>> recipes = new MediatorLiveData<>();
    private RecipeRepository recipeRepository;

    public enum ViewState {CATEGORIES, RECIPES}

    private MutableLiveData<ViewState> viewState;

    public RecipeListViewModel(@NonNull Application application){
        super(application);
        recipeRepository = RecipeRepository.getInstance(application);
        init();
    }

    private void init(){
        if (viewState == null) {
            viewState = new MutableLiveData<>();
            viewState.setValue(ViewState.CATEGORIES);
        }
    }

    public LiveData<ViewState> getViewState(){
        return viewState;
    }

    public LiveData<Resource<List<Recipe>>> getRecipes(){
        return recipes;
    }

    public void searchRecipesApi(String query, int pageNumber) {
        final LiveData<Resource<List<Recipe>>> repositorySource = recipeRepository.searchRecipesApi(query, pageNumber);

        recipes.addSource(repositorySource, new Observer<Resource<List<Recipe>>>() {
            @Override
            public void onChanged(Resource<List<Recipe>> listResource) {

                // react to the data
                recipes.setValue(listResource);
            }
        });
    }
}

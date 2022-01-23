package com.jingyu.recipe.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    //query extras
    private boolean isQueryExhausted;
    private boolean isPerformingQuery;
    private int pageNumber; // keep track for pagination
    private String query;
    private boolean cancelRequest;
    private long requestStartTime;

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

    public void setViewCategories(){
        viewState.setValue(ViewState.CATEGORIES);
    }

    public LiveData<Resource<List<Recipe>>> getRecipes(){
        return recipes;
    }

    public void searchRecipesApi(String query, int pageNumber) {
        if(!isPerformingQuery) {
            if (pageNumber == 0) {
                pageNumber = 1;
            }
            this.pageNumber = pageNumber;
            this.query = query;
            isQueryExhausted = false;
            executeSearch();
        }
    }

    public void searchNextPage(){
        if(!isQueryExhausted && !isPerformingQuery){
            pageNumber++;
            executeSearch();
        }
    }

    public int getPageNumber(){
        return pageNumber;
    }

    public static final String QUERY_EXHAUSTED = "Query is exhausted.";
    private void executeSearch(){
        requestStartTime = System.currentTimeMillis();
        isPerformingQuery = true;
        viewState.setValue(ViewState.RECIPES);
        final LiveData<Resource<List<Recipe>>> repositorySource = recipeRepository.searchRecipesApi(query, pageNumber);
        recipes.addSource(repositorySource, new Observer<Resource<List<Recipe>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<Recipe>> listResource) {
                if (!cancelRequest){
                    if(listResource != null){
                        recipes.setValue(listResource);
                        if(listResource.status == Resource.Status.SUCCESS ){
                            Log.d(TAG, "onChanged: REQUEST TIME: " + (System.currentTimeMillis() - requestStartTime) / 1000 + " seconds.");
                            isPerformingQuery = false;
                            if(listResource.data != null) {
                                if (listResource.data.size() == 0) {
                                    Log.d(TAG, "onChanged: query is EXHAUSTED...");
                                    recipes.setValue(new Resource<List<Recipe>>(
                                            Resource.Status.ERROR,
                                            listResource.data,
                                            QUERY_EXHAUSTED
                                    ));
                                    isPerformingQuery = true;
                                }
                            }
                            // must remove or it will keep listening to repository
                            recipes.removeSource(repositorySource);
                        }
                        else if(listResource.status == Resource.Status.ERROR ){
                            isPerformingQuery = false;
                            recipes.removeSource(repositorySource);
                        }
                    }
                    else{
                        recipes.removeSource(repositorySource);
                    }
                } else {
                        recipes.removeSource(repositorySource);
                }

            }
        });
    }

    public void cancelSearchRequest(){
        if(isPerformingQuery){
            Log.d(TAG, "cancelSearchRequest: canceling the search request.");
            cancelRequest = true;
            isPerformingQuery = false;
            pageNumber = 1;
        }
    }
}

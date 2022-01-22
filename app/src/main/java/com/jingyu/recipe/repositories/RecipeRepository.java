package com.jingyu.recipe.repositories;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.jingyu.recipe.AppExecutors;
import com.jingyu.recipe.models.Recipe;
import com.jingyu.recipe.persistence.RecipeDao;
import com.jingyu.recipe.persistence.RecipeDatabase;
import com.jingyu.recipe.requests.ServiceGenerator;
import com.jingyu.recipe.requests.responses.ApiResponse;
import com.jingyu.recipe.requests.responses.RecipeSearchResponse;
import com.jingyu.recipe.util.NetworkBoundResource;
import com.jingyu.recipe.util.Resource;

import java.util.List;

public class RecipeRepository {

    private static final String TAG = "RecipeRepository";
    private static RecipeRepository instance;
    private RecipeDao recipeDao;

    public static RecipeRepository getInstance(Context context){
        if (instance == null) {
            instance = new RecipeRepository(context);
        }
        return instance;
    }

    private RecipeRepository(Context context) {
        recipeDao = RecipeDatabase.getInstance(context).getRecipeDao();
    }

    // search api class
    public LiveData<Resource<List<Recipe>>> searchRecipesApi(final String query, final int pageNumber) {
        return new NetworkBoundResource<List<Recipe>, RecipeSearchResponse>(AppExecutors.getInstance()) {

            // fetch result from the internet and insert into the cache
            @Override
            public void saveCallResult(@NonNull RecipeSearchResponse item) {
                if (item.getRecipes() != null) {

                    // construct the a recipe array
                    Recipe[] recipes = new Recipe[item.getRecipes().size()];

                    int index = 0;
                    for(long rowId: recipeDao.insertRecipes((Recipe[]) (item.getRecipes().toArray(recipes)))){
                        if (rowId == -1) {
                            Log.d(TAG, "saveCallResult: we have met a conflict");
                            // recipe is already in the cache so we need to reinsert the particular recipe
                            recipeDao.updateRecipe(
                                    recipes[index].getId(),
                                    recipes[index].getTitle(),
                                    recipes[index].getPublisher(),
                                    recipes[index].getImageUrl(),
                                    recipes[index].getSocialUrl()
                            );
                        }
                        index++;
                    }

                }
            }

            @Override
            public boolean shouldFetch(@Nullable List<Recipe> data) {
                return true;
            }

            @NonNull
            @Override
            public LiveData<List<Recipe>> loadFromDb() {
                return recipeDao.searchRecipes(query, pageNumber);
            }

            // make retrofit request and convert Call<RecipeSearchResponse> into liveData
            @NonNull
            @Override
            public LiveData<ApiResponse<RecipeSearchResponse>> createCall() {
                return ServiceGenerator.getRecipeApi().searchRecipe(query, String.valueOf(pageNumber));
            }
        }.getAsLiveData();
    }

}

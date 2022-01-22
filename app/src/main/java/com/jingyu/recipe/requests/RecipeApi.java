package com.jingyu.recipe.requests;

import androidx.lifecycle.LiveData;

import com.jingyu.recipe.requests.responses.ApiResponse;
import com.jingyu.recipe.requests.responses.RecipeResponse;
import com.jingyu.recipe.requests.responses.RecipeSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeApi {

    // search
    @GET("api/v2/recipes")
    LiveData<ApiResponse<RecipeSearchResponse>> searchRecipe(
            @Query("q") String query,
            @Query("page") String page
    );

    // Get Recipe Request
    @GET("api/get")
    LiveData<ApiResponse<RecipeResponse>> getRecipe(
            @Query("rId") String recipe_id
    );

}

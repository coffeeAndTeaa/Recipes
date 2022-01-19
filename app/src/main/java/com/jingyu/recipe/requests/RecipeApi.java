package com.jingyu.recipe.requests;

import com.jingyu.recipe.requests.responses.RecipeResponse;
import com.jingyu.recipe.requests.responses.RecipeSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeApi {

    // search
    @GET("api/v2/recipes")
    Call<RecipeSearchResponse> searchRecipe(
            @Query("q") String query,
            @Query("page") String page
    );

    // Get Recipe Request
    @GET("api/get")
    Call<RecipeResponse> getRecipe(
            @Query("rId") String recipe_id
    );

}

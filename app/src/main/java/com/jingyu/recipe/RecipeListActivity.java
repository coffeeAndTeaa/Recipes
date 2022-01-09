package com.jingyu.recipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.jingyu.recipe.models.Recipe;
import com.jingyu.recipe.requests.RecipeApi;
import com.jingyu.recipe.requests.ServiceGenerator;
import com.jingyu.recipe.requests.responses.RecipeSearchResponse;
import com.jingyu.recipe.util.Constants;
import com.jingyu.recipe.viewmodels.RecipeListViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeListActivity extends BaseActivity {
    private static final String TAG = "RecipeListActivity";
    
    private RecipeListViewModel mRecipeListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        mRecipeListViewModel = new ViewModelProvider(this).get(RecipeListViewModel.class);
        subscribeObservers();
        findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testRetrofitRequest();
            }
        });


    }

    public void searchRecipesApi(String query, int pageNumber) {
        mRecipeListViewModel.searchRecipesApi(query, pageNumber);
    }

    private void testRetrofitRequest(){
        searchRecipesApi("pork", 1);
    }

    private void subscribeObservers(){
        mRecipeListViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                if (recipes != null) {
                    for (Recipe recipe: recipes) {
                        Log.d(TAG, "onChanged: "+ recipe.getTitle());
                    }
                }
            }
        });
    }


}
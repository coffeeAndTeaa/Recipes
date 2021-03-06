package com.jingyu.recipe;

import static com.jingyu.recipe.viewmodels.RecipeListViewModel.QUERY_EXHAUSTED;


import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.util.ViewPreloadSizeProvider;
import com.jingyu.recipe.adapters.OnRecipeListener;
import com.jingyu.recipe.adapters.RecipeRecyclerAdapter;
import com.jingyu.recipe.models.Recipe;
import com.jingyu.recipe.util.Resource;
import com.jingyu.recipe.util.VerticalSpacingItemDecorator;
import com.jingyu.recipe.viewmodels.RecipeListViewModel;

import java.util.List;

public class RecipeListActivity extends BaseActivity implements OnRecipeListener {
    private static final String TAG = "RecipeListActivity";
    
    private RecipeListViewModel mRecipeListViewModel;
    private RecyclerView mRecyclerView;
    private RecipeRecyclerAdapter mAdapter;
    private SearchView mSearchView;

    // print all the staff for testing
    public static void printRecipes(List<Recipe>list, String tag){
        for(Recipe recipe: list){
            Log.d(tag, "onChanged: " + recipe.getTitle());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        mRecyclerView = findViewById(R.id.recipe_list);
        mSearchView = findViewById(R.id.search_view);

        mRecipeListViewModel = new ViewModelProvider(this).get(RecipeListViewModel.class);

        initRecyclerView();
        initSearchView();
        subscribeObservers();
        setSupportActionBar(findViewById(R.id.toolbar));

    }

    private RequestManager initGlide(){
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.white_background);

        return Glide.with(this).setDefaultRequestOptions(options);
    }

    private void initRecyclerView(){
        VerticalSpacingItemDecorator decorator = new VerticalSpacingItemDecorator(30);
        mRecyclerView.addItemDecoration(decorator);
        ViewPreloadSizeProvider<String> viewPreloader = new ViewPreloadSizeProvider<>();
        mAdapter = new RecipeRecyclerAdapter(this, initGlide(), viewPreloader);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        RecyclerViewPreloader<String> preloader = new RecyclerViewPreloader<String>(Glide.with(this), mAdapter, viewPreloader, 30);
        mRecyclerView.addOnScrollListener(preloader);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                if(!mRecyclerView.canScrollVertically(1)
                        // only search on recipes page
                        && mRecipeListViewModel.getViewState().getValue() == RecipeListViewModel.ViewState.RECIPES){
                    // search the next page
                    mRecipeListViewModel.searchNextPage();
                }
            }
        });
    }

    private void initSearchView(){
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                searchRecipeApi(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void subscribeObservers(){
        mRecipeListViewModel.getRecipes().observe(this, new Observer<Resource<List<Recipe>>>() {
            @Override
            public void onChanged(Resource<List<Recipe>> listResource) {
                if (listResource != null) {
                    Log.d(TAG, "onChanged: status" + listResource.status);

                    if (listResource.data != null) {
                        switch(listResource.status) {
                            case LOADING: {
                                if(mRecipeListViewModel.getPageNumber() > 1){
                                    mAdapter.displayLoading();
                                }
                                else{
                                    mAdapter.displayOnlyLoading();
                                }
                                break;
                            }
                            case SUCCESS: {
                                Log.d(TAG, "onChanged: cache has been refreshed.");
                                Log.d(TAG, "onChanged: status: SUCCESS, #Recipes: " + listResource.data.size());
                                mAdapter.hideLoading();
                                mAdapter.setRecipes(listResource.data);
                                break;
                            }
                            case ERROR: {
                                Log.e(TAG, "onChanged: cannot refresh cache.");
                                Log.e(TAG, "onChanged: ERROR message: " + listResource.message );
                                Log.e(TAG, "onChanged: status: ERROR, #Recipes: " + listResource.data.size());
                                mAdapter.hideLoading();
                                mAdapter.setRecipes(listResource.data);
                                Toast.makeText(RecipeListActivity.this, listResource.message, Toast.LENGTH_SHORT).show();

                                if(listResource.message.equals(QUERY_EXHAUSTED)){
                                    mAdapter.setQueryExhausted();
                                }
                                break;
                            }
                        }
                    }
                }
            }
        });

        mRecipeListViewModel.getViewState().observe(this, new Observer<RecipeListViewModel.ViewState>() {
            @Override
            public void onChanged(RecipeListViewModel.ViewState viewState) {
                if (viewState != null) {
                    switch(viewState) {
                        case RECIPES:
                            break;
                        case CATEGORIES:
                            displaySearchCategories();
                            break;
                    }
                }
            }
        });
    }

    private void searchRecipeApi(String query) {
        mRecyclerView.smoothScrollToPosition(0);
        mRecipeListViewModel.searchRecipesApi(query, 1);
        mSearchView.clearFocus();
    }

    @Override
    public void onRecipeClick(int position) {
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra("recipe", mAdapter.getSelectedRecipe(position));
        startActivity(intent);
        Log.d(TAG, "onRecipeClick: clicked. " + position);
    }

    @Override
    public void onCategoryClick(String category) {
        searchRecipeApi(category);
    }

    private void displaySearchCategories(){
        mAdapter.displaySearchCategories();
    }

    @Override
    public void onBackPressed() {
        if (mRecipeListViewModel.getViewState().getValue() == RecipeListViewModel.ViewState.CATEGORIES) {
            super.onBackPressed();
        }else {
            mRecipeListViewModel.setViewCategories();
            mRecipeListViewModel.cancelSearchRequest();
        }
    }
}
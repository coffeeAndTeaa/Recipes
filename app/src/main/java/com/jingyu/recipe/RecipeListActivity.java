package com.jingyu.recipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jingyu.recipe.adapters.OnRecipeListener;
import com.jingyu.recipe.adapters.RecipeRecyclerAdapter;
import com.jingyu.recipe.models.Recipe;
import com.jingyu.recipe.requests.RecipeApi;
import com.jingyu.recipe.requests.ServiceGenerator;
import com.jingyu.recipe.requests.responses.RecipeSearchResponse;
import com.jingyu.recipe.util.Constants;
import com.jingyu.recipe.util.VerticalSpacingItemDecorator;
import com.jingyu.recipe.viewmodels.RecipeListViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeListActivity extends BaseActivity implements OnRecipeListener {
    private static final String TAG = "RecipeListActivity";
    
    private RecipeListViewModel mRecipeListViewModel;
    private RecyclerView mRecyclerView;
    private RecipeRecyclerAdapter mAdapter;
    private SearchView mSearchView;

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


    private void initRecyclerView(){
        VerticalSpacingItemDecorator decorator = new VerticalSpacingItemDecorator(30);
        mRecyclerView.addItemDecoration(decorator);
        mAdapter = new RecipeRecyclerAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initSearchView(){
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void subscribeObservers(){
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


    @Override
    public void onRecipeClick(int position) {
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra("recipe", mAdapter.getSelectedRecipe(position));
        startActivity(intent);
        Log.d(TAG, "onRecipeClick: clicked. " + position);
    }

    @Override
    public void onCategoryClick(String category) {

    }

    private void displaySearchCategories(){
        mAdapter.displaySearchCategories();
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_search_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_categories) {
            displaySearchCategories();
        }
        return super.onOptionsItemSelected(item);
    }
}
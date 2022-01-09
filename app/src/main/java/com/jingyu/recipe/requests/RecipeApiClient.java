package com.jingyu.recipe.requests;

import static com.jingyu.recipe.util.Constants.NETWORK_TIMEOUT;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.jingyu.recipe.AppExecutors;
import com.jingyu.recipe.models.Recipe;
import com.jingyu.recipe.requests.responses.RecipeSearchResponse;
import com.jingyu.recipe.util.Constants;

import java.lang.invoke.MutableCallSite;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

public class RecipeApiClient {

    private static final String TAG = "RecipeApiClient";

    private static RecipeApiClient instance;
    private MutableLiveData<List<Recipe>> mRecipes;
    private RetrieveRecipeRunnable mRetrieveRecipeRunnable;

    public static RecipeApiClient getInstance() {
        if (instance == null) {
            instance = new RecipeApiClient();
        }
        return instance;
    }

    private RecipeApiClient() {
        mRecipes = new MutableLiveData<>();
    }

    public LiveData<List<Recipe>> getRecipes(){
        return mRecipes;
    }

    public void searchRecipeApi(String query, int pageNumber) {
        if(mRetrieveRecipeRunnable != null) {
            mRetrieveRecipeRunnable = null;
        }
        mRetrieveRecipeRunnable = new RetrieveRecipeRunnable(query, pageNumber);
        final Future handler = AppExecutors.getInstance().networkIO().submit(mRetrieveRecipeRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                // let the user know it timed out
                handler.cancel(true);
            }
        }, NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    // doing the search query
    private class RetrieveRecipeRunnable implements Runnable{

        private String query;
        private int pageNumber;
        private boolean cancelRequest;

        private RetrieveRecipeRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
                try {
                    Response response = getRecipes(query, pageNumber).execute();
                    if (cancelRequest == true) {
                        return;
                    }
                    if (response.code() == 200) {
                        List<Recipe> list = new ArrayList<>(((RecipeSearchResponse)response.body()).getRecipes());
                        if (pageNumber == 1) {
                            mRecipes.postValue(list);
                        } else {
                            List<Recipe> currentRecipes = mRecipes.getValue();
                            currentRecipes.addAll(list);
                            mRecipes.postValue(currentRecipes);
                        }
                    }
                    else {
                        String error = response.errorBody().string();
                        Log.e(TAG, "run: " + error);
                        mRecipes.postValue(null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    mRecipes.postValue(null);
                }
        }

        private Call<RecipeSearchResponse> getRecipes(String query, int pageNumber) {
            return ServiceGenerator.getRecipeApi().searchRecipe(query, String.valueOf(pageNumber));
        }

        private void cancelRequest(){
            Log.d(TAG, "cancelRequest: canceling the retrieval query");
            cancelRequest = true;
        }
    }
}

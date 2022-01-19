package com.jingyu.recipe.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jingyu.recipe.models.Recipe;
import com.jingyu.recipe.repository.RecipeRepository;

import java.util.List;

public class RecipeListViewModel extends ViewModel {

    private static final String TAG = "RecipeListViewModel";

    public enum ViewState {CATEGORIES, RECIPES}

    private MutableLiveData<ViewState> viewState;

    public RecipeListViewModel(){
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
}

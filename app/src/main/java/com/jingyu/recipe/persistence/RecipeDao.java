package com.jingyu.recipe.persistence;

import static androidx.room.OnConflictStrategy.IGNORE;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;

import androidx.room.Insert;
import androidx.room.Query;

import com.jingyu.recipe.models.Recipe;

import java.util.List;

@Dao
public interface RecipeDao {

    @Insert(onConflict = IGNORE)
    long[] insertRecipes(Recipe...recipe);

    @Insert(onConflict = IGNORE)
    void insertRecipe(Recipe recipe);

    @Query("UPDATE recipes SET title = :title, publisher =:publisher, image_url =:image_url, social_rank = :social_rank " +
            "WHERE id = :recipe_id")
    LiveData<List<Recipe>> updateRecipe(String recipe_id, String title,
                                        String publisher, String image_url, float social_rank);

    @Query("SELECT * FROM recipes WHERE title LIKE '%' || :query || '%' OR " + " ingredients LIKE '%' || :query || '%'" +
            " ORDER BY social_rank DESC LIMIT (:pageNumber * 30)")
    LiveData<List<Recipe>> searchRecipes(String query, int pageNumber);

    @Query("SELECT * FROM recipes WHERE id = :recipeId")
    LiveData<Recipe> getRecipe(String recipeId);
}

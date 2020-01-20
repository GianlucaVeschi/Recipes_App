package www.gianlucaveschi.mijirecipesapp.database;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import www.gianlucaveschi.mijirecipesapp.models.Recipe;

import static androidx.room.OnConflictStrategy.IGNORE;
import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface RecipeDAO {

    @Insert(onConflict = IGNORE) //This means that if a Recipe already Exist in the DB it doesn't have to be replaced
    long[] insertRecipes(Recipe... recipes); //VarArg annotations can contain 0 or N elements but it's always treated as an array

    @Insert(onConflict = REPLACE)
    void insertRecipe(Recipe recipe);

    // Custom update statement so ingredients and timestamp don't get removed
    @Query("UPDATE recipes SET title = :title, publisher = :publisher, image_url = :image_url, " +
            "social_rank = :social_rank WHERE recipe_id = :recipe_id")
    void updateRecipe(String recipe_id, String title, String publisher, String image_url, float social_rank);

    // NOTE: The SQL query sometimes won't return EXACTLY what the api does since the API might use a different query
    // or even a different database. But they are very very close.
    @Query("SELECT * FROM recipes WHERE title LIKE '%' || :query || '%' OR " + " ingredients LIKE '%' || :query || '%'" +
            " ORDER BY social_rank DESC LIMIT (:pageNumber * 30)")
    LiveData<List<Recipe>> searchRecipes(String query, int pageNumber);

    @Query("SELECT * FROM recipes WHERE recipe_id = :recipeId")
    LiveData<Recipe> getRecipe(String recipeId);


}

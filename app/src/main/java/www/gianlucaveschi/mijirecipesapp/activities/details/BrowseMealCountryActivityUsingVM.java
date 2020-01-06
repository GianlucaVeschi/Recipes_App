package www.gianlucaveschi.mijirecipesapp.activities.details;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gianlucaveschi.load_json_images_picasso.R;
import com.r0adkll.slidr.Slidr;

import java.io.IOException;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.gianlucaveschi.mijirecipesapp.models.recipes.Recipe;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.RecipeApi;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.ServiceGenerator;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.responses.RecipeResponse;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.themealdb.MealAPI;
import www.gianlucaveschi.mijirecipesapp.utils.Constants;
import www.gianlucaveschi.mijirecipesapp.viewmodels.BrowseMealCountriesViewModel;


public class BrowseMealCountryActivityUsingVM extends AppCompatActivity {

    //Bind UI
    @BindView(R.id.testGetData_Btn)     Button testGetData;

    //Logger
    private static final String TAG = "BrowseMealCountry_act_v";
    
    //View Model
    private static BrowseMealCountriesViewModel mBrowseMealCountriesViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_meal_using_viewholder);
        ButterKnife.bind(this);

        //Instantiate View Model
        mBrowseMealCountriesViewModel = ViewModelProviders.of(this).get(BrowseMealCountriesViewModel.class);

        //Observe Live Data
        subscribeObservers();

        testGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Test
                testRetrofitRequestSimple();

            }
        });

        //Slide back to the Previous Activity
        Slidr.attach(this);

    }

    //Retrieve Live Data from the Repository
    public LiveData<List<Recipe>> getRecipes() {
        return mBrowseMealCountriesViewModel.getRecipes();
    }

    //Observe Live Data
    private void subscribeObservers(){
        mBrowseMealCountriesViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                if(recipes != null){
                    for(Recipe recipe : recipes){
                        Log.d(TAG, "onChanged: " + recipe.getTitle());
                    }
                }

            }
        });
    }

    private void testRetrofitRequest(){
        searchRecipesApi("chicken breast", 1);
    }


    private void searchRecipesApi(String query, int pageNumber){
        if(pageNumber == 0){
            pageNumber = 1;
        }
        mBrowseMealCountriesViewModel.searchRecipesApi(query, pageNumber);
    }

    private void testRetrofitRequestSimple(){
        RecipeApi recipeApi = ServiceGenerator.getRecipeApi();
        // do get using retrofit
        Call<RecipeResponse> responseCall = recipeApi
                .getRecipe(
                        Constants.API_KEY,
                        "35382"
                );

        responseCall.enqueue(new Callback<RecipeResponse>() {
            @Override
            public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response) {
                Log.d(TAG, "onResponse: URL" + response.raw().request().url());
                Log.d(TAG, "onResponse: Server Response: " + response.toString());
                if (response.code() == 200) {
                    Recipe recipe = response.body().getRecipe();
                    Log.d(TAG, "onResponse: " + recipe.toString());
                }
                else{
                    try {
                        Log.d(TAG, "onResponse: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<RecipeResponse> call, Throwable t) {
                Log.d(TAG, "onResponse: ERROR: " + t.getMessage());
            }
        });
    }

}

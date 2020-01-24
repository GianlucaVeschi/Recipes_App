package www.gianlucaveschi.mijirecipesapp.activities.details;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gianlucaveschi.load_json_images_picasso.R;
import com.r0adkll.slidr.Slidr;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import www.gianlucaveschi.mijirecipesapp.models.Recipe;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.optimized.Resource;
import www.gianlucaveschi.mijirecipesapp.utils.Constants;
import www.gianlucaveschi.mijirecipesapp.viewmodels.RecipeDetailsViewModel;


public class RecipeDetailsActivity extends AppCompatActivity {

    private static final String TAG = "RecipeDetailsActivity";

    //UI Components
    @BindView(R.id.image_view_recipe_detail)    ImageView imageView;
    @BindView(R.id.name_recipe_detail)          TextView textViewRecipeTitle;
    @BindView(R.id.ingredients_title )          TextView textViewInstructionsTitle;
    @BindView(R.id.ingredients_list)            TextView textViewIngredients;
    @BindView(R.id.progress_bar)                ProgressBar mProgressBar;   

    //private RecipeDetailsViewModel mRecipeDetailsViewModel;
    private RecipeDetailsViewModel mRecipeDetailsViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        ButterKnife.bind(this);

        //Instantiate ViewModel
        mRecipeDetailsViewModel = ViewModelProviders.of(this).get(RecipeDetailsViewModel.class);

        //Internally calls subscribeObservers()
        getIncomingIntent();

        //Slide back to the Previous Activity
        Slidr.attach(this);
    }

    private void getIncomingIntent(){
        if(getIntent().hasExtra(Constants.EXTRA_RECIPE)){
            Recipe recipe = getIntent().getParcelableExtra(Constants.EXTRA_RECIPE);
            Log.d(TAG, "getIncomingIntent: " + recipe.getTitle());
            Log.d(TAG, "getIncomingIntent: " + recipe.toString());
            subscribeObservers(recipe.getRecipe_id());
        }
    }

    private void subscribeObservers(final String recipeID){
        mRecipeDetailsViewModel.getRecipe(recipeID).observe(this, new Observer<Resource<Recipe>>(){
            @Override
            public void onChanged(Resource<Recipe> recipeResource) {
                if(recipeResource != null) {
                    if (recipeResource.data != null) { //data is the body of the response
                        switch (recipeResource.status){
                            case LOADING:{
                                Log.d(TAG, "onChanged: LOADING");
                                showProgressBar(true);
                                break;
                            }
                            case SUCCESS:{
                                Log.d(TAG, "onChanged: cache has been refreshed.");
                                Log.d(TAG, "onChanged: status: SUCCESS, Recipe: " + recipeResource.data.getTitle());
                                showParent();
                                showProgressBar(false);
                                setRecipeProperties(recipeResource.data);
                                break;
                            }
                            case ERROR:{
                                Log.e(TAG, "onChanged: status: ERROR, Recipe: " + recipeResource.data.getTitle());
                                Log.e(TAG, "onChanged: status: ERROR message: " + recipeResource.message);
                                Toast.makeText(RecipeDetailsActivity.this, recipeResource.message, Toast.LENGTH_SHORT).show();
                                showParent();
                                showProgressBar(false);
                                break;
                            }
                        }
                    }
                }
            }
        });
    }

    private void setRecipeProperties(Recipe recipe) {
        Log.d(TAG, "setRecipeProperties: " + recipe.toString());
        setImage(recipe.getImage_url());
        textViewRecipeTitle.setText(recipe.getTitle());
        textViewIngredients.setText("");

        if(recipe.getIngredients()!= null) {    //when network is down the ingredients would be null
            for (String ingredient : recipe.getIngredients()) {
                textViewIngredients.append(ingredient + "\n");
            }
        }
        else{
            textViewIngredients.append("Error retrieving Ingredients. \n Check error connection");
        }
    }

    private void setImage(String imgUrl){

        //PlaceHolder is something Goes Wrong
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background);

        Glide.with(this)
                .setDefaultRequestOptions(requestOptions)
                .load(imgUrl)
                .into(imageView);
    }

    // TODO: 22/01/2020 this method 
    public void showProgressBar(boolean visibility){
        mProgressBar.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
    }

    private void showParent(){
        //mScrollView.setVisibility(View.VISIBLE);
    }
}

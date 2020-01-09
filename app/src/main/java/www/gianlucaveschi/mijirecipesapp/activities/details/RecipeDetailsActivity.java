package www.gianlucaveschi.mijirecipesapp.activities.details;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gianlucaveschi.load_json_images_picasso.R;
import com.r0adkll.slidr.Slidr;
import com.squareup.picasso.Picasso;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import www.gianlucaveschi.mijirecipesapp.models.recipes.Recipe;
import www.gianlucaveschi.mijirecipesapp.utils.Constants;
import www.gianlucaveschi.mijirecipesapp.viewmodels.RecipeDetailsViewModel;


public class RecipeDetailsActivity extends AppCompatActivity {

    private static final String TAG = "RecipeDetailsActivity";

    //UI Components
    @BindView(R.id.image_view_recipe_detail)     ImageView imageView;
    @BindView(R.id.name_recipe_detail)           TextView textViewName;
    @BindView(R.id.ingredients_title )           TextView textViewInstructionsTitle;
    @BindView(R.id.ingredients_list)             TextView textViewIngredients;

    private RecipeDetailsViewModel mRecipeDetailsViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_details);
        ButterKnife.bind(this);

        //Instatiate ViewModel
        mRecipeDetailsViewModel = ViewModelProviders.of(this).get(RecipeDetailsViewModel.class);

        subscribeObservers();
        getIncomingIntent();

        //MyCode
        /*
        //Get Incoming Intent
        Intent intent = getIntent();
        Recipe recipe = intent.getParcelableExtra(Constants.EXTRA_RECIPE);

        //Set UI
        String imageURL     = recipe.getImage_url();
        String recipeName   = recipe.getTitle();
        //String recipeRank   = recipe.getSocial_rank(); // TODO: 09/01/2020 change to string
        String idRecipe     = recipe.getRecipe_id();
        String ingredients[]  = recipe.getIngredients();
        Log.d(TAG, "onCreate: " + recipe.toString());

        setImage(imageURL);
        textViewIngredients.setText("");
        textViewName.setText(recipe.getTitle(););
        //setIngredients(ingredients);
        */

        //Slide back to the Previous Activity
        Slidr.attach(this);
    }

    private void subscribeObservers(){
        mRecipeDetailsViewModel.getRecipe().observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(@Nullable Recipe recipe) {
                if(recipe != null){
                    /*
                    Log.d(TAG, "onChanged: --------------------------------------------");
                    Log.d(TAG, "onChanged: " + recipe.getTitle());
                    for(String ingredient: recipe.getIngredients()){
                        Log.d(TAG, "onChanged: INGREDIENT " + ingredient);
                    }
                     */
                    if(recipe.getRecipe_id().equals(mRecipeDetailsViewModel.getRecipeId())){
                        setRecipeProperties(recipe);
                    }
                }
            }
        });
    }

    private void setRecipeProperties(Recipe recipe) {
        setImage(recipe.getImage_url());
        textViewName.setText(recipe.getTitle());
        textViewIngredients.setText("");
        for(String ingredient: recipe.getIngredients()){
            textViewIngredients.append(ingredient + "\n");
        }

    }

    private void getIncomingIntent(){
        if(getIntent().hasExtra(Constants.EXTRA_RECIPE)){
            Recipe recipe = getIntent().getParcelableExtra(Constants.EXTRA_RECIPE);
            Log.d(TAG, "getIncomingIntent: " + recipe.getTitle());
            mRecipeDetailsViewModel.searchRecipeById(recipe.getRecipe_id());
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
}

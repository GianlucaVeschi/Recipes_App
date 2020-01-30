package www.gianlucaveschi.mijirecipesapp.activities.meal_drawer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.util.ViewPreloadSizeProvider;
import com.gianlucaveschi.load_json_images_picasso.R;
import com.r0adkll.slidr.Slidr;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import www.gianlucaveschi.mijirecipesapp.activities.details.RecipeDetailsActivity;
import www.gianlucaveschi.mijirecipesapp.adapters.recipes.OnRecipeListener;
import www.gianlucaveschi.mijirecipesapp.adapters.recipes.RecipeAdapter;
import www.gianlucaveschi.mijirecipesapp.models.Recipe;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.resources.Resource;
import www.gianlucaveschi.mijirecipesapp.utils.Constants;
import www.gianlucaveschi.mijirecipesapp.utils.UI.VerticalSpacingItemDecorator;
import www.gianlucaveschi.mijirecipesapp.viewmodels.RecipesCategoriesViewModel;

import static www.gianlucaveschi.mijirecipesapp.utils.Constants.QUERY_EXHAUSTED;

public class RecipeCategoriesActivity extends AppCompatActivity implements OnRecipeListener {

    private static final String TAG = "RecipeCategoriesAct";

    @BindView(R.id.recipes_categories_list) RecyclerView categoriesRecView;
    @BindView(R.id.search_view)             SearchView mSearchView;

    //private RecipesCategoriesViewModel mRecipesCategoriesViewModel;
    private RecipesCategoriesViewModel mRecipesCategoriesViewModel;
    private RecipeAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_categories);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        ButterKnife.bind(this);
        Slidr.attach(this);

        initViewModel();
        initRecyclerView();
        subscribeObservers();
        initSearchView();

        //When the activity is accessed through an Intent directly display a list of recipes
        if(getIntent().hasExtra(Constants.EXTRA_RECIPE_CAT)){
            displayIntentRecipeList();
        }
    }

    private void initViewModel() {
        mRecipesCategoriesViewModel = ViewModelProviders.of(this)
                .get(RecipesCategoriesViewModel.class);
    }

    @Override
    public void onBackPressed() {
        if(mRecipesCategoriesViewModel.getViewState()
                .getValue() == RecipesCategoriesViewModel.ViewState.CATEGORIES){
            super.onBackPressed();
        }
        else{
            //Go to back to Search all categories if you are actually inside a category
            mRecipesCategoriesViewModel.cancelSearchRequest();
            mRecipesCategoriesViewModel.setViewCategories();//Will trigger the observer
        }
    }


    private void subscribeObservers(){
        mRecipesCategoriesViewModel.getRecipes().observe(this, new Observer<Resource<List<Recipe>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<Recipe>> listResource) {
                if(listResource != null){
                    Log.d(TAG, "onChanged: status: " + listResource.status);
                    if(listResource.data != null){
                        switch (listResource.status) {
                            case LOADING: {
                                if(mRecipesCategoriesViewModel.getPageNumber() > 1){
                                    //Display loading to simulate pagination
                                    mAdapter.displayLoading();
                                }
                                else{
                                    //Display loading at the very beginning
                                    mAdapter.displayOnlyLoading();
                                }
                                break;
                            }
                            case SUCCESS: {
                                Log.d(TAG, "onChanged: cache has been refreshed.");
                                Log.d(TAG, "onChanged: status: SUCCESS, #Recipes: " + listResource.data.size());
                                mAdapter.hideLoading();
                                mAdapter.setRecipes(listResource.data); //Set data to the RV
                                break;
                            }
                            case ERROR: {

                                Log.e(TAG, "onChanged: cannot refresh cache.");
                                Log.e(TAG, "onChanged: ERROR message: " + listResource.message );
                                Log.e(TAG, "onChanged: status: ERROR, #Recipes: " + listResource.data.size());
                                mAdapter.hideLoading();
                                mAdapter.setRecipes(listResource.data);
                                Toast.makeText(RecipeCategoriesActivity.this, listResource.message, Toast.LENGTH_SHORT).show();

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

        mRecipesCategoriesViewModel.getViewState().observe(this, new Observer<RecipesCategoriesViewModel.ViewState>() {
            @Override
            public void onChanged(@Nullable RecipesCategoriesViewModel.ViewState viewState) {
                if(viewState != null){
                    switch (viewState){

                        case RECIPES:{
                            // recipes will show automatically from other observer
                            break;
                        }

                        case CATEGORIES:{
                            displaySearchCategories();
                            break;
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onRecipeClick(int position) {
        Intent intent = new Intent(this, RecipeDetailsActivity.class);
        intent.putExtra(Constants.EXTRA_RECIPE, mAdapter.getSelectedRecipe(position));
        startActivity(intent);
    }

    @Override
    public void onCategoryClick(String category) {
        Log.d(TAG, "onCategoryClick: OK");
        mRecipesCategoriesViewModel.searchRecipesApi(category,1);
        mSearchView.clearFocus();
    }

    private void displaySearchCategories(){
        mAdapter.displaySearchCategories();
    }

    private void initRecyclerView(){
        ViewPreloadSizeProvider<String> viewPreloader = new ViewPreloadSizeProvider<>();
        mAdapter = new RecipeAdapter(this, initGlide(), viewPreloader);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(30);
        categoriesRecView.addItemDecoration(itemDecorator);
        categoriesRecView.setAdapter(mAdapter);
        categoriesRecView.setLayoutManager(new LinearLayoutManager(this));

        RecyclerViewPreloader<String> preloader = new RecyclerViewPreloader<String>(Glide.with(this), mAdapter, viewPreloader, 30);
        categoriesRecView.addOnScrollListener(preloader);

        categoriesRecView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                if(!categoriesRecView.canScrollVertically(1)
                        && mRecipesCategoriesViewModel.getViewState().getValue() == RecipesCategoriesViewModel.ViewState.RECIPES){
                    // search the next page
                    mRecipesCategoriesViewModel.searchNextPage();
                }
            }
        });
    }

    private void initSearchView(){
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                Log.d(TAG, "onQueryTextSubmit: OK");
                mRecipesCategoriesViewModel.searchRecipesApi(s, 1);
                mSearchView.clearFocus();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    private void displayIntentRecipeList(){
        String categoryName = getIntent().getStringExtra(Constants.EXTRA_RECIPE_CAT);
        Log.d(TAG, "getIncomingIntent: " + categoryName);
        onCategoryClick(categoryName);
    }

    private RequestManager initGlide(){
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.white_image);
        return Glide.with(this).setDefaultRequestOptions(options);
    }
}

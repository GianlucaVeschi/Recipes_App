package www.gianlucaveschi.mijirecipesapp.activities.meal_drawer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;

import com.gianlucaveschi.load_json_images_picasso.R;
import com.r0adkll.slidr.Slidr;

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
import www.gianlucaveschi.mijirecipesapp.utils.Constants;
import www.gianlucaveschi.mijirecipesapp.utils.UI.VerticalSpacingItemDecorator;
import www.gianlucaveschi.mijirecipesapp.viewmodels.RecipesCategoriesViewModel;
import www.gianlucaveschi.mijirecipesapp.viewmodels.RecipesCategoriesViewModelNEW;

public class RecipeCategoriesActivity extends AppCompatActivity implements OnRecipeListener {

    private static final String TAG = "RecipeCategoriesActivit";

    @BindView(R.id.recipes_categories_list) RecyclerView categoriesRecView;
    @BindView(R.id.search_view) SearchView mSearchView;

    private RecipesCategoriesViewModel mRecipesCategoriesViewModel;
    private RecipesCategoriesViewModelNEW mRecipesCategoriesViewModelNEW;
    private RecipeAdapter mAdapter;
   

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_categories);
        ButterKnife.bind(this);
        Slidr.attach(this);

        //set View Model
        mRecipesCategoriesViewModel = ViewModelProviders.of(this).get(RecipesCategoriesViewModel.class);
        mRecipesCategoriesViewModelNEW = ViewModelProviders.of(this).get(RecipesCategoriesViewModelNEW.class);

        initRecyclerView();
        subscribeObservers();
        initSearchView();
        if(!mRecipesCategoriesViewModel.isViewingRecipes()){
            // display search categories
            displaySearchCategories();
        }
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
    }

    @Override
    public void onBackPressed() {
        if(mRecipesCategoriesViewModel.onBackPressed()){
            super.onBackPressed();
        }
        else{
            //Go to back to Search all categories if you are actually inside a category
            displaySearchCategories();
        }
    }

    private void subscribeObservers(){
        mRecipesCategoriesViewModelNEW.getViewState().observe(this, new Observer<RecipesCategoriesViewModelNEW.ViewState>() {
            @Override
            public void onChanged(@Nullable RecipesCategoriesViewModelNEW.ViewState viewState) {
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
    /*private void subscribeObservers(){
        mRecipesCategoriesViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                if(recipes != null){
                    if(mRecipesCategoriesViewModel.isViewingRecipes()){
                        MyLogger.logRecipes(TAG,recipes);
                        mRecipesCategoriesViewModel.setIsPerformingQuery(false);
                        mAdapter.setRecipes(recipes);
                    }
                }
            }
        });

        mRecipesCategoriesViewModel.isQueryExhausted().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                Log.d(TAG, "onChanged: the query is exhausted..." + aBoolean);
                if(aBoolean) {
                    mAdapter.setQueryExhausted();
                }
            }
        });
    }*/

    @Override
    public void onRecipeClick(int position) {
        Intent intent = new Intent(this, RecipeDetailsActivity.class);
        intent.putExtra(Constants.EXTRA_RECIPE, mAdapter.getSelectedRecipe(position));
        startActivity(intent);
    }

    @Override
    public void onCategoryClick(String category) {
        mAdapter.displayLoading();
        mRecipesCategoriesViewModel.searchRecipesApi(category, 1);
        mSearchView.clearFocus();
    }

    private void displaySearchCategories(){
        mRecipesCategoriesViewModel.setIsViewingRecipes(false);
        mAdapter.displaySearchCategories();
    }

    private void initRecyclerView(){
        mAdapter = new RecipeAdapter(this);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(30);
        categoriesRecView.addItemDecoration(itemDecorator);
        categoriesRecView.setAdapter(mAdapter);
        categoriesRecView.setLayoutManager(new LinearLayoutManager(this));

        categoriesRecView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                if(!categoriesRecView.canScrollVertically(1)){
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

                mAdapter.displayLoading();
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
}

package www.gianlucaveschi.mijirecipesapp.viewmodels;

import android.app.Application;
import android.util.Log;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import www.gianlucaveschi.mijirecipesapp.models.Recipe;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.optimized.Resource;
import www.gianlucaveschi.mijirecipesapp.repositories.RecipeRepositoryNew;

import static www.gianlucaveschi.mijirecipesapp.utils.Constants.QUERY_EXHAUSTED;

//Everything in the ViewModel gets saved
public class RecipesCategoriesViewModelNEW extends AndroidViewModel {

    /*--------------------------------- INNER VARIABLES ------------------------------------------*/
    private static final String TAG = "RecipesCategoriesVmNEW";

    public enum ViewState {CATEGORIES, RECIPES}

    private RecipeRepositoryNew recipeRepositoryNew;

    //MediatorLiveData is useful to *change* the LiveData retrieved before posting it to the UI
    private MediatorLiveData<Resource<List<Recipe>>> recipes = new MediatorLiveData<>();
    private MutableLiveData<ViewState> viewState;

    // query extras
    private boolean isQueryExhausted;
    private String query;
    private int pageNumber;
    private boolean isPerformingQuery;

    /*--------------------------------- CONSTRUCTOR ----------------------------------------------*/

    public RecipesCategoriesViewModelNEW(@NonNull Application application) {
        super(application);
        recipeRepositoryNew = RecipeRepositoryNew.getInstance(application);
        init();
    }

    /*--------------------------------- INITIALIZATION -------------------------------------------*/
    private void init() {
        if (viewState == null) {
            //As a first view we want to see the recipies categories
            viewState = new MutableLiveData<>();
            viewState.setValue(ViewState.CATEGORIES);
        }
    }

    /*--------------------------------- EXECUTION ..----------------------------------------------*/
    public void searchRecipesApi(String query, int pageNumber) {
        if (!isPerformingQuery) {
            if (pageNumber == 0) {
                pageNumber = 1;
            }
            this.pageNumber = pageNumber;
            this.query = query;
            isQueryExhausted = false;
            executeSearch();
        }
    }

    private void executeSearch() {
        isPerformingQuery = true;
        viewState.setValue(ViewState.RECIPES);
        final LiveData<Resource<List<Recipe>>> repositorySource = recipeRepositoryNew.searchRecipesApi(query, pageNumber);

        //The OnChanged method is triggered when the data is returned from the repository and by
        //using MEDIATOR DATA, before posting it to the UI, we can apply some changes to the data.
        recipes.addSource(repositorySource, new Observer<Resource<List<Recipe>>>() {
            @Override
            public void onChanged(Resource<List<Recipe>> listResource) {
                if (listResource != null) {
                    recipes.setValue(listResource);

                    //REQUEST SUCCESSFUL
                    if (listResource.status == Resource.Status.SUCCESS) {
                        isPerformingQuery = false;
                        if (listResource.data != null) {
                            if (listResource.data.size() == 0) {
                                Log.d(TAG, "onChanged: query is EXHAUSTED...");
                                recipes.setValue(new Resource<List<Recipe>>(
                                        Resource.Status.ERROR,
                                        listResource.data,
                                        QUERY_EXHAUSTED
                                ));
                                isPerformingQuery = true;
                            }
                        }
                        // must remove or it will keep listening to repository
                        recipes.removeSource(repositorySource);
                    }

                    //REQUEST FAILED
                    else if(listResource.status == Resource.Status.ERROR ){
                        isPerformingQuery = false;
                        recipes.removeSource(repositorySource);
                    }
                }
                else{
                    recipes.removeSource(repositorySource);
                }
            }
        });
    }

    public boolean onBackPressed(){
        if(isPerformingQuery){
            // cancel the query
            //recipeRepositoryNew.cancelRequest();
            isPerformingQuery = false;
        }
        return true;
    }

    /*------------------------------ GETTERS -----------------------------------------------------*/
    public MutableLiveData<ViewState> getViewState() {
        return viewState;
    }

    public LiveData<Resource<List<Recipe>>> getRecipes() {
        return recipes;
    }

    public boolean isQueryExhausted() {
        return isQueryExhausted;
    }

    public String getQuery() {
        return query;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public boolean isPerformingQuery() {
        return isPerformingQuery;
    }
}

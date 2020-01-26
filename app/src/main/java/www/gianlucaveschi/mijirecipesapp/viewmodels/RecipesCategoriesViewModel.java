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
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.resources.Resource;
import www.gianlucaveschi.mijirecipesapp.repositories.RecipeRepository;

import static www.gianlucaveschi.mijirecipesapp.utils.Constants.QUERY_EXHAUSTED;

//Everything in the ViewModel gets saved
public class RecipesCategoriesViewModel extends AndroidViewModel {

    /*--------------------------------- INNER VARIABLES ------------------------------------------*/
    private static final String TAG = "RecipesCategoriesVmNEW";

    public enum ViewState {CATEGORIES, RECIPES}

    private RecipeRepository recipeRepository;

    //MediatorLiveData is useful to *change* the LiveData retrieved before posting it to the UI
    private MediatorLiveData<Resource<List<Recipe>>> recipes = new MediatorLiveData<>();
    private MutableLiveData<ViewState> viewState;

    // query extras
    private boolean isQueryExhausted;
    private String query;
    private int pageNumber;
    private boolean isPerformingQuery;
    private boolean cancelRequest;
    private long requestStartTime;

    /*--------------------------------- CONSTRUCTOR ----------------------------------------------*/

    public RecipesCategoriesViewModel(@NonNull Application application) {
        super(application);
        recipeRepository = RecipeRepository.getInstance(application);
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

    /*--------------------------------- EXECUTION ------------------------------------------------*/
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
        requestStartTime = System.currentTimeMillis();
        isPerformingQuery = true;
        cancelRequest = false;
        viewState.setValue(ViewState.RECIPES);
        final LiveData<Resource<List<Recipe>>> repositorySource =
                recipeRepository.searchRecipesApi(query, pageNumber);

        //The OnChanged method is triggered when the data is returned from the repository and by
        //using MEDIATOR DATA, before posting it to the UI, we can apply some changes to the data.
        //In this case : setting the value to QUERY_EXHAUSTED.
        recipes.addSource(repositorySource, new Observer<Resource<List<Recipe>>>() {
            @Override
            public void onChanged(Resource<List<Recipe>> listResource) {
                if (!cancelRequest) {
                    if (listResource != null) {
                        recipes.setValue(listResource);

                        //REQUEST SUCCESSFUL
                        if (listResource.status == Resource.Status.SUCCESS) {
                            Log.d(TAG, "onChanged: REQUEST TIME: " +
                                    (System.currentTimeMillis() - requestStartTime) + " milliseconds.");
                            isPerformingQuery = false;

                            //REQUEST SUCCESSFUL BUT THERE'S NO MORE DATA TO RETRIEVE
                            if (listResource.data != null && listResource.data.size() == 0) {
                                Log.d(TAG, "onChanged: query is EXHAUSTED...");
                                recipes.setValue(new Resource<List<Recipe>>(
                                        Resource.Status.ERROR,
                                        listResource.data,
                                        QUERY_EXHAUSTED
                                ));
                                isPerformingQuery = true;
                            }
                            // must remove or it will keep listening to repository
                            recipes.removeSource(repositorySource);
                        }

                        //REQUEST FAILED
                        else if (listResource.status == Resource.Status.ERROR) {
                            Log.d(TAG, "onChanged: REQUEST TIME: " +
                                    (System.currentTimeMillis() - requestStartTime) + " milliseconds.");
                            isPerformingQuery = false;
                            recipes.removeSource(repositorySource);
                        }
                    } else {
                        recipes.removeSource(repositorySource);
                    }
                } else {
                    recipes.removeSource(repositorySource);
                }
            }
        });
    }


    public void searchNextPage() {
        if (!isQueryExhausted && !isPerformingQuery) {
            pageNumber++;
            executeSearch();
        }
    }

    public void cancelSearchRequest() {
        if (isPerformingQuery) {
            Log.d(TAG, "cancelSearchRequest: canceling the search request");
            cancelRequest = true;
            isPerformingQuery = false;
            pageNumber = 1;
        }
    }

    /*------------------------------ SETTERS -----------------------------------------------------*/

    public void setViewCategories() {
        viewState.setValue(ViewState.CATEGORIES);
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

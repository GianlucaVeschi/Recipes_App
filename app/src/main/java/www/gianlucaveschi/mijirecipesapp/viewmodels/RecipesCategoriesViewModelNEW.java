package www.gianlucaveschi.mijirecipesapp.viewmodels;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import www.gianlucaveschi.mijirecipesapp.models.Recipe;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.optimized.Resource;
import www.gianlucaveschi.mijirecipesapp.repositories.RecipeRepository;
import www.gianlucaveschi.mijirecipesapp.repositories.RecipeRepositoryNew;

//Everything in the ViewModel gets saved
public class RecipesCategoriesViewModelNEW extends AndroidViewModel {

    private static final String TAG = "RecipesCategoriesVM2";

    public enum ViewState {CATEGORIES, RECIPES}

    private MutableLiveData<ViewState> viewState;
    private RecipeRepositoryNew recipeRepositoryNew;

    //MediatorLiveData is useful to *change* the LiveData retrieved before posting it to the UI
    private MediatorLiveData<Resource<List<Recipe>>> recipes = new MediatorLiveData<>();

    //Constructor
    public RecipesCategoriesViewModelNEW(@NonNull Application application) {
        super(application);
        recipeRepositoryNew = RecipeRepositoryNew.getInstance(application);
        init();
    }

    //Initialization
    private void init(){
        if(viewState == null){
            //As a first view we want to see the recipies categories
            viewState = new MutableLiveData<>();
            viewState.setValue(ViewState.CATEGORIES);
        }
    }

    //Get data from the
    public void searchRecipesApi(String query, int pageNumber){

        final LiveData<Resource<List<Recipe>>> repositorySource = recipeRepositoryNew.searchRecipesApi(query,pageNumber);

        //The OnChanged method is triggered when the data is returned from the repository and by
        //using MEDIATOR DATA, before posting it to the UI, we can apply some changes to the data.
        recipes.addSource(repositorySource, new Observer<Resource<List<Recipe>>>() {
            @Override
            public void onChanged(Resource<List<Recipe>> listResource) {


                recipes.setValue(listResource);
            }
        });
    }

    //Getters
    public MutableLiveData<ViewState> getViewState() {
        return viewState;
    }

    public LiveData<Resource<List<Recipe>>> getRecipes(){
        return recipes;
    }
}

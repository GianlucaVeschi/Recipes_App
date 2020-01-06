package www.gianlucaveschi.mijirecipesapp.viewmodels;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import www.gianlucaveschi.mijirecipesapp.models.recipes.Recipe;
import www.gianlucaveschi.mijirecipesapp.repositories.BrowseMealCountryRepository;

public class BrowseMealCountriesViewModel extends ViewModel {

    private BrowseMealCountryRepository mBrowseMealCountryRepository;

    public BrowseMealCountriesViewModel() {
        mBrowseMealCountryRepository = BrowseMealCountryRepository.getInstance();
    }

    public LiveData<List<Recipe>> getRecipes(){
        return mBrowseMealCountryRepository.getRecipes();
    }
    public void searchRecipesApi(String query, int pageNumber){
        mBrowseMealCountryRepository.searchRecipesApi(query, pageNumber);
    }
}

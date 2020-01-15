package www.gianlucaveschi.mijirecipesapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

//Everything in the ViewModel gets saved
public class RecipesCategoriesViewModelNEW extends AndroidViewModel {

    private static final String TAG = "RecipesCategoriesVM2";

    public enum ViewState {CATEGORIES, RECIPES}

    private MutableLiveData<ViewState> viewState;

    public RecipesCategoriesViewModelNEW(@NonNull Application application) {
        super(application);

        init();
    }

    private void init(){
        if(viewState == null){
            //As a first view we want to see the recipies categories
            viewState = new MutableLiveData<>();
            viewState.setValue(ViewState.CATEGORIES);
        }
    }

    public MutableLiveData<ViewState> getViewState() {
        return viewState;
    }

}

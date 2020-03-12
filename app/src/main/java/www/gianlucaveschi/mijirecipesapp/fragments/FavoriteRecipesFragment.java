package www.gianlucaveschi.mijirecipesapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.util.ViewPreloadSizeProvider;
import com.gianlucaveschi.load_json_images_picasso.R;

import java.util.ArrayList;
import java.util.Arrays;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import www.gianlucaveschi.mijirecipesapp.adapters.recipes.OnRecipeListener;
import www.gianlucaveschi.mijirecipesapp.adapters.recipes.RecipeAdapter;
import www.gianlucaveschi.mijirecipesapp.models.Recipe;

public class FavoriteRecipesFragment extends Fragment implements OnRecipeListener {


    private static final String TAG = "FavoriteRecipesFragment";
    public static ArrayList<Recipe> favRecipesList;

    @BindView(R.id.fav_recipes_rec_view) RecyclerView favRecipesRecView;

    RecipeAdapter mRecipeAdapter;
    Recipe exampleRecipe = new Recipe("8586"
            , "Christmas Breakfast Sausage Casserole"
            , "All Recipes"
            , null
            , "https://res.cloudinary.com/dk4ocuiwa/image/upload/v1575163942/RecipesApi/392254b0f1.jpg"
            , 0
            , 0);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_recipes, container, false);
        ButterKnife.bind(this,view);
        initFavRecipesRecView();
        return view;
    }

    private void initFavRecipesRecView() {
        favRecipesList = new ArrayList<>();
        favRecipesList.add(exampleRecipe);
        ViewPreloadSizeProvider<String> viewPreloader = new ViewPreloadSizeProvider<>();

        mRecipeAdapter = new RecipeAdapter(this, initGlide(), viewPreloader);
        mRecipeAdapter.setRecipes(favRecipesList);

        favRecipesRecView.setAdapter(mRecipeAdapter);
        favRecipesRecView.setHasFixedSize(true);
        favRecipesRecView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private RequestManager initGlide(){
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.white_image);
        return Glide.with(this).setDefaultRequestOptions(options);
    }

    @Override
    public void onRecipeClick(int position) {
        Log.d(TAG, "onRecipeClick: ");
    }

    @Override
    public void onCategoryClick(String category) {
        Log.d(TAG, "onCategoryClick: ");
    }
}

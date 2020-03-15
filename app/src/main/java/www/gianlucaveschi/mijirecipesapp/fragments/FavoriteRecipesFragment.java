package www.gianlucaveschi.mijirecipesapp.fragments;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import www.gianlucaveschi.mijirecipesapp.activities.details.RecipeDetailsActivity;
import www.gianlucaveschi.mijirecipesapp.adapters.recipes.OnRecipeListener;
import www.gianlucaveschi.mijirecipesapp.adapters.recipes.RecipeAdapter;
import www.gianlucaveschi.mijirecipesapp.utils.Constants;
import www.gianlucaveschi.mijirecipesapp.utils.SavedRecipes;

public class FavoriteRecipesFragment extends Fragment implements OnRecipeListener {


    private static final String TAG = "FavoriteRecipesFragment";

    @BindView(R.id.fav_recipes_rec_view) RecyclerView favRecipesRecView;
    @BindView(R.id.noFavoriteRecipes)    TextView noFavoriteRecipes;

    RecipeAdapter mRecipeAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_recipes, container, false);
        ButterKnife.bind(this,view);

        if(SavedRecipes.isEmpty()){
            noFavoriteRecipes.setVisibility(View.VISIBLE);
        }
        else{
            noFavoriteRecipes.setVisibility(View.GONE);
            initFavRecipesRecView();
        }

        return view;
    }

    private void initFavRecipesRecView() {
        ViewPreloadSizeProvider<String> viewPreloader = new ViewPreloadSizeProvider<>();

        mRecipeAdapter = new RecipeAdapter(this, initGlide(), viewPreloader);
        mRecipeAdapter.setRecipes(SavedRecipes.getRecipes());

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
        Intent intent = new Intent(getActivity(), RecipeDetailsActivity.class);
        intent.putExtra(Constants.EXTRA_RECIPE, mRecipeAdapter.getSelectedRecipe(position));
        startActivity(intent);
    }

    @Override
    public void onCategoryClick(String category) {
        Log.d(TAG, "onCategoryClick: ");
    }
}

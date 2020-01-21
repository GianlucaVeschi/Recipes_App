package www.gianlucaveschi.mijirecipesapp.adapters.recipes;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestOptions;
import com.gianlucaveschi.load_json_images_picasso.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import www.gianlucaveschi.mijirecipesapp.models.Recipe;
import www.gianlucaveschi.mijirecipesapp.utils.Constants;

public class RecipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //ViewHolders
    private static final int RECIPE_TYPE    = 1;
    private static final int LOADING_TYPE   = 2;
    private static final int CATEGORY_TYPE  = 3;
    private static final int EXHAUSTED_TYPE = 4;

    private List<Recipe>        mRecipes;
    private OnRecipeListener    mOnRecipeListener;
    private RequestManager      mRequestManager;

    public RecipeAdapter(OnRecipeListener onRecipeListener, RequestManager requestManager) {
        this.mOnRecipeListener  = onRecipeListener;
        this.mRequestManager    = requestManager;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = null;
        switch (i){

            case RECIPE_TYPE:{
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_recipe, viewGroup, false);
                return new RecipeViewHolder(view, mOnRecipeListener, mRequestManager);
            }

            case LOADING_TYPE:{
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_loading_view, viewGroup, false);
                return new LoadingViewHolder(view);
            }

            case EXHAUSTED_TYPE:{
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_search_exhausted, viewGroup, false);
                return new SearchExhaustedViewHolder(view);
            }

            case CATEGORY_TYPE:{
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_recipe_category, viewGroup, false);
                return new RecipeCategoryViewHolder(view, mOnRecipeListener, mRequestManager);
            }

            default:{
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_recipe, viewGroup, false);
                return new RecipeViewHolder(view, mOnRecipeListener, mRequestManager);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        int itemViewType = getItemViewType(i);
        if(itemViewType == RECIPE_TYPE){
            ((RecipeViewHolder)viewHolder).onBind(mRecipes.get(i));
        }
        else if(itemViewType == CATEGORY_TYPE){
            ((RecipeCategoryViewHolder)viewHolder).onBind(mRecipes.get(i));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mRecipes.get(position).getSocial_rank() == -1){
            return CATEGORY_TYPE;
        }
        else if(mRecipes.get(position).getTitle().equals("LOADING...")){
            return LOADING_TYPE;
        }
        else if(mRecipes.get(position).getTitle().equals("EXHAUSTED...")){
            return EXHAUSTED_TYPE;
        }
        else{
            return RECIPE_TYPE;
        }
    }

    public void setQueryExhausted(){
        hideLoading();
        Recipe exhaustedRecipe = new Recipe();
        exhaustedRecipe.setTitle("EXHAUSTED...");
        mRecipes.add(exhaustedRecipe);
        notifyDataSetChanged();
    }

    public void hideLoading(){
        if(isLoading()){
            if(isLoading()) {
                if (mRecipes.get(0).getTitle().equals("LOADING...")) {
                    mRecipes.remove(mRecipes.size() - 1);
                }
            }
            if(isLoading()){
                if(mRecipes.get(mRecipes.size() - 1).getTitle().equals("LOADING...")){
                    mRecipes.remove(mRecipes.size() - 1);
                }
            }
            notifyDataSetChanged();
        }
    }

    public void displayOnlyLoading(){
        clearRecipesList();
        Recipe recipe = new Recipe();
        recipe.setTitle("LOADING...");
        mRecipes.add(recipe);
        notifyDataSetChanged();
    }

    private void clearRecipesList(){
        if(mRecipes == null){
            mRecipes = new ArrayList<>();
        }
        else {
            mRecipes.clear();
        }
        notifyDataSetChanged();
    }

    public void displayLoading(){
        if(mRecipes == null){
            mRecipes = new ArrayList<>();
        }
        if(!isLoading()){
            Recipe recipe = new Recipe();
            recipe.setTitle("LOADING...");
            mRecipes.add(recipe); // loading at bottom of screen
            notifyDataSetChanged();
        }
    }

    private boolean isLoading(){
        if(mRecipes != null){
            if(mRecipes.size() > 0){
                if(mRecipes.get(mRecipes.size() - 1).getTitle().equals("LOADING...")){
                    return true;
                }
            }
        }
        return false;
    }

    public void displaySearchCategories(){
        List<Recipe> categories = new ArrayList<>();
        for(int i = 0; i< Constants.DEFAULT_SEARCH_CATEGORIES.length; i++){
            Recipe recipe = new Recipe();
            recipe.setTitle(Constants.DEFAULT_SEARCH_CATEGORIES[i]);
            recipe.setImage_url(Constants.DEFAULT_SEARCH_CATEGORY_IMAGES[i]);
            recipe.setSocial_rank(-1);
            categories.add(recipe);
        }
        mRecipes = categories;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(mRecipes != null){
            return mRecipes.size();
        }
        return 0;
    }

    public void setRecipes(List<Recipe> recipes){
        mRecipes = recipes;
        notifyDataSetChanged();
    }

    public Recipe getSelectedRecipe(int position){
        if(mRecipes != null){
            if(mRecipes.size() > 0){
                return mRecipes.get(position);
            }
        }
        return null;
    }

}


package www.gianlucaveschi.mijirecipesapp.adapters.recipes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gianlucaveschi.load_json_images_picasso.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import www.gianlucaveschi.mijirecipesapp.models.recipes.Recipe;
import www.gianlucaveschi.mijirecipesapp.utils.Constants;

public class RecipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int RECIPE_TYPE = 1;
    private static final int LOADING_TYPE = 2;

    private List<Recipe> mRecipes;
    private OnRecipeListener mOnRecipeListener;

    public RecipeAdapter(){
        //empty
    }
    public RecipeAdapter(OnRecipeListener mOnRecipeListener) {
        this.mOnRecipeListener = mOnRecipeListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        switch(viewType){
            case(RECIPE_TYPE):
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_item, parent, false);
                return new RecipeViewHolder(view, mOnRecipeListener);
            case(LOADING_TYPE):
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_loading_list_item, parent, false);
                return new LoadingViewHolder(view);
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_item, parent, false);
                return new RecipeViewHolder(view, mOnRecipeListener);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        int itemViewType = getItemViewType(position);
        if(itemViewType == RECIPE_TYPE){
            ((RecipeViewHolder)viewHolder).title.setText(mRecipes.get(position).getTitle());
            ((RecipeViewHolder)viewHolder).publisher.setText(mRecipes.get(position).getPublisher());
            ((RecipeViewHolder)viewHolder).socialScore.setText(String.valueOf(Math.round(mRecipes.get(position).getSocial_rank())));

            // set the image
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .error(R.drawable.ic_launcher_background);

            Glide.with(((RecipeViewHolder) viewHolder).itemView)
                    .setDefaultRequestOptions(options)
                    .load(mRecipes.get(position).getImage_url())
                    .into(((RecipeViewHolder) viewHolder).image);
        }
    }

    public void displayLoading(){
        if(!isLoading()){
            Recipe loadingRecipe = new Recipe();
            loadingRecipe.setTitle(Constants.LOADING);
            List<Recipe> loadingList = new ArrayList<>();
            loadingList.add(loadingRecipe);
            mRecipes = loadingList;     //Set the adapter list to the loading item
            notifyDataSetChanged();
        }
    }

    private boolean isLoading(){
        if(mRecipes != null){
            if(mRecipes.size() > 0){
                return mRecipes.get(mRecipes.size() - 1).getTitle().equals(Constants.LOADING);
            }
        }
        return false;
    }


    @Override
    public int getItemViewType(int position) {
        if(mRecipes.get(position).getTitle().equals(Constants.LOADING)){
            return LOADING_TYPE;
        }
        else{
            return RECIPE_TYPE;
        }
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
    }
}

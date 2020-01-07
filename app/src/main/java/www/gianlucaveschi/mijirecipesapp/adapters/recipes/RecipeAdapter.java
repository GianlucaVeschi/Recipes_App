package www.gianlucaveschi.mijirecipesapp.adapters.recipes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gianlucaveschi.load_json_images_picasso.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import www.gianlucaveschi.mijirecipesapp.models.recipes.Recipe;

public class RecipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Recipe> mRecipes;
    private OnRecipeListener mOnRecipeListener;

    public RecipeAdapter(OnRecipeListener mOnRecipeListener) {
        this.mOnRecipeListener = mOnRecipeListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_item, parent, false);
        return new RecipeViewHolder(view, mOnRecipeListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
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

    @Override
    public int getItemCount() {
        //if(mRecipes != null){
            return mRecipes.size();
        //}
        // return 0;
    }

    public void setRecipes(List<Recipe> recipes){
        mRecipes = recipes;
    }
}

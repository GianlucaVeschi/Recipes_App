package www.gianlucaveschi.mijirecipesapp.adapters.recipes;

import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.gianlucaveschi.load_json_images_picasso.R;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import www.gianlucaveschi.mijirecipesapp.models.Recipe;

public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private TextView            title, publisher, socialScore;
    private AppCompatImageView  image;
    private OnRecipeListener    onRecipeListener;
    private RequestManager      requestManager;

    public RecipeViewHolder(@NonNull View itemView, OnRecipeListener onRecipeListener, RequestManager requestManager) {
        super(itemView);
        this.onRecipeListener   = onRecipeListener;
        this.requestManager     = requestManager;

        title       = itemView.findViewById(R.id.recipe_title);
        publisher   = itemView.findViewById(R.id.recipe_publisher);
        socialScore = itemView.findViewById(R.id.recipe_social_score);
        image       = itemView.findViewById(R.id.recipe_image);

        itemView.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        onRecipeListener.onRecipeClick(getAdapterPosition());
    }

    public void onBind(Recipe recipe){
        requestManager
                .load(recipe.getImage_url())
                .into(image);

        title.setText(recipe.getTitle());
        publisher.setText(recipe.getPublisher());
        socialScore.setText(String.valueOf(Math.round(recipe.getSocial_rank())));

    }


}

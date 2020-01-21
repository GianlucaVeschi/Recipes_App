package www.gianlucaveschi.mijirecipesapp.adapters.recipes;

import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.gianlucaveschi.load_json_images_picasso.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import www.gianlucaveschi.mijirecipesapp.models.Recipe;

public class RecipeCategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    CircleImageView     categoryImage;
    TextView            categoryTitle;
    OnRecipeListener    listener;
    RequestManager      requestManager;

    public RecipeCategoryViewHolder(@NonNull View itemView, OnRecipeListener listener, RequestManager requestManager) {
        super(itemView);

        this.listener = listener;
        this.categoryImage = itemView.findViewById(R.id.category_image);
        this.categoryTitle = itemView.findViewById(R.id.category_title);
        this.requestManager = requestManager;

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        listener.onCategoryClick(categoryTitle.getText().toString());
    }

    public void onBind(Recipe recipe){
        Uri path = Uri.parse("android.resource://www.gianlucaveschi.mijirecipesapp/drawable/" + recipe.getImage_url());
        requestManager.load(path).into(categoryImage);
        categoryTitle.setText(recipe.getTitle());
    }
}

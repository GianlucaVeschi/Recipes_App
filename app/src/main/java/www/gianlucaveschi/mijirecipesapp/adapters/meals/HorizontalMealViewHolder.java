package www.gianlucaveschi.mijirecipesapp.adapters.meals;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gianlucaveschi.load_json_images_picasso.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import www.gianlucaveschi.mijirecipesapp.models.Meal;

/**
 * INNER HORIZONTAL VIEW HOLDER
 * */
public class HorizontalMealViewHolder extends RecyclerView.ViewHolder{

    private ImageView mImageView;
    private TextView m_tv_RecipeName;
    private TextView m_tv_RecipeID;
    private ProgressBar progressBar;

    public HorizontalMealViewHolder(@NonNull View itemView, final OnMealClickListener listener, final ArrayList<Meal> mealsList) {
        super(itemView);
        mImageView = itemView.findViewById(R.id.image_view);
        m_tv_RecipeName = itemView.findViewById(R.id.recipe_name);
        m_tv_RecipeID = itemView.findViewById(R.id.recipe_id);
        progressBar = itemView.findViewById(R.id.progress_bar);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        listener.onItemClick(position,mealsList);
                    }
                }
            }
        });
    }

    public void initHorizLayout(HorizontalMealViewHolder horizontalMealViewHolder, int position, ArrayList<Meal> mealsList) {
        Meal currentItem = mealsList.get(position);

        String imageUrl = currentItem.getImgUrl();
        String mealName = currentItem.getMealName();
        String idMeal = currentItem.getIdMeal();

        horizontalMealViewHolder.progressBar.setVisibility(View.INVISIBLE);
        horizontalMealViewHolder.m_tv_RecipeName.setText(mealName);
        horizontalMealViewHolder.m_tv_RecipeID.setText("ID : " + idMeal);
        Picasso.get()
                .load(imageUrl)
                .fit()
                .centerInside()
                .into(horizontalMealViewHolder.mImageView);
    }

}
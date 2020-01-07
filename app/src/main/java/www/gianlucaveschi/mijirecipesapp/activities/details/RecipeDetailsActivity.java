package www.gianlucaveschi.mijirecipesapp.activities.details;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.gianlucaveschi.load_json_images_picasso.R;
import com.r0adkll.slidr.Slidr;
import com.squareup.picasso.Picasso;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import www.gianlucaveschi.mijirecipesapp.models.meals.MealSimple;
import www.gianlucaveschi.mijirecipesapp.models.recipes.Recipe;
import www.gianlucaveschi.mijirecipesapp.utils.Constants;


public class RecipeDetailsActivity extends AppCompatActivity {

    @BindView(R.id.image_view_recipe_detail)      ImageView imageView;
    @BindView(R.id.name_recipe_detail)            TextView textViewName;
    @BindView(R.id.instructions_recipe)         TextView textViewInstructions;
    @BindView(R.id.ingredient_quantity)         TextView textViewQuantity;
    @BindView(R.id.ingredients_title )          TextView textViewInstructionsTitle;
    @BindView(R.id.ingredient_list)             TextView textViewIngredients;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_details);
        ButterKnife.bind(this);

        textViewIngredients.setText("");
        textViewQuantity.setText("");

        Intent intent = getIntent();
        Recipe recipe = intent.getParcelableExtra(Constants.EXTRA_RECIPE);

        String imageURL     = recipe.getImage_url();
        String recipeName   = recipe.getTitle();
        String idRecipe     = recipe.getRecipe_id();

        textViewName.setText(recipeName);
        setImage(imageURL);

        //Slide back to the Previous Activity
        Slidr.attach(this);
    }

    private void setImage(String imgUrl){
        Picasso.get()
                .load(imgUrl)
                .fit()
                .centerInside()
                .into(imageView);

    }
}

package www.gianlucaveschi.mijirecipesapp.activities.meal_drawer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.gianlucaveschi.load_json_images_picasso.R;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import www.gianlucaveschi.mijirecipesapp.activities.details.BrowseMainIngredientActivity;
import www.gianlucaveschi.mijirecipesapp.activities.details.BrowseMealCountryActivity;
import www.gianlucaveschi.mijirecipesapp.adapters.CountryAdapter;
import www.gianlucaveschi.mijirecipesapp.adapters.IngredientAdapter;
import www.gianlucaveschi.mijirecipesapp.models.others.Country;
import www.gianlucaveschi.mijirecipesapp.models.others.Ingredient;

public class MealMainIngredientActivity extends AppCompatActivity {

    @BindView(R.id.meat_rv)         RecyclerView meatRecView;
    @BindView(R.id.vegetables_rv)   RecyclerView vegetablesRecView;
    @BindView(R.id.cheese_rv)       RecyclerView cheeseRecView;

    private ArrayList<Ingredient> meatList = new ArrayList<>();
    private ArrayList<Ingredient> vegetablesList = new ArrayList<>();
    private ArrayList<Ingredient> cheeseList = new ArrayList<>();

    private static final String TAG = "MealMainIngredientActiv";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_main_ingredient);
        ButterKnife.bind(this);

        createIngredientsList();
        buildRecyclerView();

        //Slide back to the Previous Activity
        Slidr.attach(this);
    }

    private void createIngredientsList() {
        //Meat
        meatList.add(new Ingredient("Chicken"));
        meatList.add(new Ingredient("Egg"));
        meatList.add(new Ingredient("Bacon"));
        meatList.add(new Ingredient("Beef"));
        meatList.add(new Ingredient("Salmon"));

        //Veggies
        vegetablesList.add(new Ingredient("lemon"));
        vegetablesList.add(new Ingredient("Mushrooms"));
        vegetablesList.add(new Ingredient("Potatoes"));
        vegetablesList.add(new Ingredient("Spinach"));
        vegetablesList.add(new Ingredient("Tomatoes"));
        vegetablesList.add(new Ingredient("Zucchini"));
        vegetablesList.add(new Ingredient("Spaghetti"));
        vegetablesList.add(new Ingredient("Avocado"));

        //Cheese
        cheeseList.add(new Ingredient("Parmesan"));
        cheeseList.add(new Ingredient("Mozzarella"));
        cheeseList.add(new Ingredient("Butter"));

    }

    private void buildRecyclerView() {
        meatRecView.setHasFixedSize(true);
        meatRecView.setLayoutManager(new GridLayoutManager(this,3));
        IngredientAdapter meatAdapter = new IngredientAdapter(this,meatList);
        meatRecView.setAdapter(meatAdapter);
        handleClickOnFlagEvent(meatAdapter,meatList);

        vegetablesRecView.setHasFixedSize(true);
        vegetablesRecView.setLayoutManager(new GridLayoutManager(this,3));
        IngredientAdapter vegetablesAdapter = new IngredientAdapter(this,vegetablesList);
        vegetablesRecView.setAdapter(vegetablesAdapter);
        handleClickOnFlagEvent(vegetablesAdapter,vegetablesList);

        cheeseRecView.setHasFixedSize(true);
        cheeseRecView.setLayoutManager(new GridLayoutManager(this,3));
        IngredientAdapter cheeseAdapter = new IngredientAdapter(this,cheeseList);
        cheeseRecView.setAdapter(cheeseAdapter);
        handleClickOnFlagEvent(cheeseAdapter,cheeseList);
    }

    private void handleClickOnFlagEvent(IngredientAdapter ingredientAdapter, final ArrayList<Ingredient> ingredientsList) {

        ingredientAdapter.setOnItemClickListener(new IngredientAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d(TAG, "onItemClick: clicked " + position);

                Toast toast = Toast.makeText(MealMainIngredientActivity.this,
                        "You touched on " + ingredientsList.get(position).getName(),
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();


                //Get Nationality informations of the clicked stove
                String imageUrl = ingredientsList.get(position).getIngredientImgUrl();
                String ingredientName = ingredientsList.get(position).getName();

                //Pass it to the intent
                Intent intent = new Intent(MealMainIngredientActivity.this, BrowseMainIngredientActivity.class);
                intent.putExtra("image_url",     imageUrl );
                intent.putExtra("ingredient_name", ingredientName);

                startActivity(intent);

            }
        });
    }


}

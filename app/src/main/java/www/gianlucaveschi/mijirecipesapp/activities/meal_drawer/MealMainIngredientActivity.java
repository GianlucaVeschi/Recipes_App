package www.gianlucaveschi.mijirecipesapp.activities.meal_drawer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.gianlucaveschi.load_json_images_picasso.R;
import com.r0adkll.slidr.Slidr;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import www.gianlucaveschi.mijirecipesapp.activities.meal_tabs.BrowseMealByIngredientActivity;
import www.gianlucaveschi.mijirecipesapp.adapters.IngredientAdapter;
import www.gianlucaveschi.mijirecipesapp.models.Ingredient;

public class MealMainIngredientActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)         Toolbar toolbar;
    @BindView(R.id.meat_rv)         RecyclerView meatRecView;
    @BindView(R.id.vegetables_rv)   RecyclerView vegetablesRecView;
    @BindView(R.id.cheese_rv)       RecyclerView cheeseRecView;

    private ArrayList<Ingredient> meatList      = new ArrayList<>();
    private ArrayList<Ingredient> veggiesList   = new ArrayList<>();
    private ArrayList<Ingredient> cheeseList    = new ArrayList<>();

    private IngredientAdapter meatAdapter;
    private IngredientAdapter veggiesAdapter;
    private IngredientAdapter cheeseAdapter;

    private static final String TAG = "MealMainIngredientActiv";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_main_ingredient);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        createIngredientsList();
        buildRecyclerView();

        //Slide back to the Previous Activity
        Slidr.attach(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_miji_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        Log.d(TAG, "onCreateOptionsMenu: ");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                meatAdapter.getFilter().filter(newText);
                veggiesAdapter.getFilter().filter(newText);
                cheeseAdapter.getFilter().filter(newText);
                Log.d(TAG, "onQueryTextChange:" );
                return false;
            }
        });
        return true;
    }

    private void createIngredientsList() {

        //Meat
        meatList.add(new Ingredient("Chicken"));
        meatList.add(new Ingredient("Egg"));
        meatList.add(new Ingredient("Bacon"));
        meatList.add(new Ingredient("Beef"));
        meatList.add(new Ingredient("Salmon"));

        //Veggies
        veggiesList.add(new Ingredient("lemon"));
        veggiesList.add(new Ingredient("Mushrooms"));
        veggiesList.add(new Ingredient("Potatoes"));
        veggiesList.add(new Ingredient("Spinach"));
        veggiesList.add(new Ingredient("Tomatoes"));
        veggiesList.add(new Ingredient("Zucchini"));
        veggiesList.add(new Ingredient("Spaghetti"));
        veggiesList.add(new Ingredient("Avocado"));

        //Cheese
        cheeseList.add(new Ingredient("Parmesan"));
        cheeseList.add(new Ingredient("Mozzarella"));
        cheeseList.add(new Ingredient("Butter"));

    }


    private void buildRecyclerView() {
        meatRecView.setHasFixedSize(true);
        meatRecView.setLayoutManager(new GridLayoutManager(this,3));
        meatAdapter = new IngredientAdapter(this,meatList);
        meatRecView.setAdapter(meatAdapter);
        handleClickOnFlagEvent(meatAdapter,meatList);

        vegetablesRecView.setHasFixedSize(true);
        vegetablesRecView.setLayoutManager(new GridLayoutManager(this,3));
        veggiesAdapter = new IngredientAdapter(this,veggiesList);
        vegetablesRecView.setAdapter(veggiesAdapter);
        handleClickOnFlagEvent(veggiesAdapter,veggiesList);

        cheeseRecView.setHasFixedSize(true);
        cheeseRecView.setLayoutManager(new GridLayoutManager(this,3));
        cheeseAdapter = new IngredientAdapter(this,cheeseList);
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
                Intent intent = new Intent(MealMainIngredientActivity.this, BrowseMealByIngredientActivity.class);
                intent.putExtra("image_url",     imageUrl );
                intent.putExtra("ingredient_name", ingredientName);

                startActivity(intent);

            }
        });
    }


}

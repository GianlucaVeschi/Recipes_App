package www.gianlucaveschi.mijirecipesapp.activities.details;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.gianlucaveschi.load_json_images_picasso.R;
import com.r0adkll.slidr.Slidr;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.gianlucaveschi.mijirecipesapp.adapters.MealAdapter;
import www.gianlucaveschi.mijirecipesapp.models.meals.MealContainer;
import www.gianlucaveschi.mijirecipesapp.models.meals.MealSimple;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.MealAPI;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.RetrofitNetworkManager;


public class BrowseMainIngredientActivity extends AppCompatActivity implements MealAdapter.OnItemClickListener {

    public static final String EXTRA_MEAL = "MealParcel";

    //Bind UI
    @BindView(R.id.meals_type_name)               TextView mainIngredientTextView;
    @BindView(R.id.browse_meals_recycler_view)  RecyclerView mealsRecyclerView;

    //Retrofit instance
    MealAPI mealAPI;

    //Distinguish between the two TypeViews
    private final static int HORIZONTAL_VIEW_TYPE = 1;
    private final static int VERTICAL_VIEW_TYPE = 2;

    //Logger
    private static final String TAG = "BrowseMainIngredientAct";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_meal);
        ButterKnife.bind(this);

        //Get Intent
        Intent intent = getIntent();
        String imgUrl   = intent.getStringExtra("image_url");
        String ingredientName  = intent.getStringExtra("ingredient_name");

        //Set UI
        String ingredientName_str = ingredientName + " Meals";
        mainIngredientTextView.setText(ingredientName_str);
        displayRecipesByIngredientWithRetrofit(ingredientName);

        //Slide back to the Previous Activity
        Slidr.attach(this);

    }

    private void displayRecipesByIngredientWithRetrofit(String ingredient){
        mealAPI = RetrofitNetworkManager.getClient().create(MealAPI.class);
        mealAPI.getMealsByIngredient(ingredient).enqueue(new Callback<MealContainer>() {
            @Override
            public void onResponse(Call<MealContainer> call, Response<MealContainer> response) {
                //Get Retrofit Response
                MealContainer mealContainer = response.body();

                //Set orientation for the rv
                mealContainer.setOrientation(VERTICAL_VIEW_TYPE);

                //Update UI
                updateRecyclerView(mealContainer);
            }

            @Override
            public void onFailure(Call<MealContainer> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

    private void updateRecyclerView(MealContainer mealContainer) {

        //Init RV
        mealsRecyclerView.setHasFixedSize(true);
        mealsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));

        ArrayList<MealSimple> mealsList = mealContainer.getMealSimples();
        MealAdapter mealAdapter = new MealAdapter(BrowseMainIngredientActivity.this, mealsList);
        mealsRecyclerView.setAdapter(mealAdapter);
        mealAdapter.setOnItemClickListener(BrowseMainIngredientActivity.this);
    }

    /**
     * OnItemClick
     * */
    @Override
    public void onItemClick(int position, ArrayList<MealSimple> mealsList) {
        Intent detailIntent = new Intent(this, MealDetailsActivity.class);
        MealSimple clickedItem = mealsList.get(position);

        //Send Parcel to the Details Activity
        detailIntent.putExtra(EXTRA_MEAL,clickedItem);
        startActivity(detailIntent);
    }
}

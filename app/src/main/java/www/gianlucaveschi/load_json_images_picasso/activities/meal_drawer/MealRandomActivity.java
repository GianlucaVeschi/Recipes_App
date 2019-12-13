package www.gianlucaveschi.load_json_images_picasso.activities.meal_drawer;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.gianlucaveschi.load_json_images_picasso.R;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.gianlucaveschi.load_json_images_picasso.MyApplication;
import www.gianlucaveschi.load_json_images_picasso.models.MealContainer;
import www.gianlucaveschi.load_json_images_picasso.networking.retrofit.MealAPI;
import www.gianlucaveschi.load_json_images_picasso.networking.retrofit.RetrofitNetworkManager;
import www.gianlucaveschi.load_json_images_picasso.utils.MyLogger;

public class MealRandomActivity extends AppCompatActivity {


    private static final String TAG = "MealRandomActivity";


    //Retrofit instance
    MealAPI mealAPI;

    @BindView(R.id.image_view_meal_detail) ImageView imageView;
    @BindView(R.id.name_meal_detail) TextView textViewName;
    @BindView(R.id.ingredient_list)  TextView textViewInstructionsTitle;
    @BindView(R.id.instructions_recipe) TextView textViewInstructions;
    @BindView(R.id.ingredient_quantity) TextView textViewQuantity;
    @BindView(R.id.ingredients_title ) TextView textViewIngredients;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_random);

        //Slide back to the Previous Activity
        Slidr.attach(this);

        getRandomMealWithRetrofit();
    }

    private void getRandomMealWithRetrofit(){
        mealAPI = RetrofitNetworkManager.getClient().create(MealAPI.class);
        Log.d(TAG, "getRandomMealWithRetrofit: ");
        mealAPI.getRandomMeal().enqueue(new Callback<MealContainer>() {
            @Override
            public void onResponse(Call<MealContainer> call, Response<MealContainer> response) {

                //Get Retrofit Response
                MealContainer mealContainer = response.body();

            }

            @Override
            public void onFailure(Call<MealContainer> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }
}

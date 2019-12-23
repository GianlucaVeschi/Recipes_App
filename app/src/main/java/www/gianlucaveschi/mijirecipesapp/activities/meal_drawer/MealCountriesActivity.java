package www.gianlucaveschi.mijirecipesapp.activities.meal_drawer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.gianlucaveschi.load_json_images_picasso.R;
import com.r0adkll.slidr.Slidr;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import www.gianlucaveschi.mijirecipesapp.activities.details.BrowseMealCountryActivity;
import www.gianlucaveschi.mijirecipesapp.adapters.CountryAdapter;
import www.gianlucaveschi.mijirecipesapp.models.countries.Country;

public class MealCountriesActivity extends AppCompatActivity {

    private static final String TAG = "MealCountriesActivity";

    @BindView(R.id.meal_countries_title)    TextView mealCountriesTitle;
    @BindView(R.id.flags_rv)                RecyclerView flagsRecView;

    private ArrayList<Country> countriesList = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_countries);
        ButterKnife.bind(this);

        createCountriesList();
        buildRecyclerView();

        //Slide back to the Previous Activity
        Slidr.attach(this);
    }

    private void createCountriesList(){
        countriesList.add(new Country("Italian",        "IT"));
        countriesList.add(new Country("Spanish",        "ES"));
        countriesList.add(new Country("American",       "US"));
        countriesList.add(new Country("French",         "FR"));
        countriesList.add(new Country("Canadian",       "CA"));
        countriesList.add(new Country("Jamaican",       "JM"));
        countriesList.add(new Country("Chinese",        "CN"));
        countriesList.add(new Country("Egyptian",       "EG"));
        countriesList.add(new Country("Greek",          "GR"));
        countriesList.add(new Country("Indian",         "IN"));
        countriesList.add(new Country("Kenyan",         "KE"));
        countriesList.add(new Country("Japanese",       "JP"));
        countriesList.add(new Country("Mexican",        "MX"));
        countriesList.add(new Country("Moroccan",       "MA"));
        countriesList.add(new Country("Portuguese",     "PT"));
        countriesList.add(new Country("Russian",        "RU"));
        countriesList.add(new Country("Thai",           "TH"));
        countriesList.add(new Country("Argentinian",    "AR"));
    }

    private void buildRecyclerView() {
        flagsRecView.setHasFixedSize(true);
        flagsRecView.setLayoutManager(new GridLayoutManager(this,3));
        CountryAdapter countryAdapter = new CountryAdapter(this,countriesList);
        flagsRecView.setAdapter(countryAdapter);
        handleClickOnFlagEvent(countryAdapter);
    }

    private void handleClickOnFlagEvent(CountryAdapter countryAdapter) {

        countryAdapter.setOnItemClickListener(new CountryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d(TAG, "onItemClick: clicked " + position);

                Toast toast = Toast.makeText(MealCountriesActivity.this,
                        "You touched on " + countriesList.get(position).getName(),
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();


                //Get Nationality informations of the clicked stove
                String flagUrl = countriesList.get(position).getFlag_img_url();
                String countryName = countriesList.get(position).getName();

                //Pass it to the intent
                Intent intent = new Intent(MealCountriesActivity.this, BrowseMealCountryActivity.class);
                intent.putExtra("flag_url",     flagUrl );
                intent.putExtra("country_name", countryName);

                startActivity(intent);

            }
        });
    }
}

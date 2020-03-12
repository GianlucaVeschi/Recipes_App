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
import www.gianlucaveschi.mijirecipesapp.activities.meal_tabs.BrowseMealsByCountryActivity;
import www.gianlucaveschi.mijirecipesapp.adapters.countries.CountryAdapter;
import www.gianlucaveschi.mijirecipesapp.models.Country;

import static www.gianlucaveschi.mijirecipesapp.utils.Constants.DEFAULT_SEARCH_COUNTRIES;
import static www.gianlucaveschi.mijirecipesapp.utils.Constants.DEFAULT_SEARCH_COUNTRIES_CODES;


public class MealCountriesFlagsActivity extends AppCompatActivity {

    private static final String TAG = "RecipeCountriesFlagsAct";

    @BindView(R.id.toolbar)                 Toolbar toolbar;
    @BindView(R.id.flags_rv)                RecyclerView flagsRecView;

    private ArrayList<Country> countriesList = new ArrayList<>();
    private CountryAdapter countryAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_countries_flags);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Choose a country");

        createCountriesList();
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
                countryAdapter.getFilter().filter(newText);
                Log.d(TAG, "onQueryTextChange:" );
                return false;
            }
        });
        return true;
    }

    private void createCountriesList(){
        if(DEFAULT_SEARCH_COUNTRIES.length == DEFAULT_SEARCH_COUNTRIES_CODES.length) {
            for (int i = 0; i < (DEFAULT_SEARCH_COUNTRIES.length - 1); i++) {
                countriesList.add(new Country(DEFAULT_SEARCH_COUNTRIES[i], DEFAULT_SEARCH_COUNTRIES_CODES[i]));
            }
        }
        else{
            Log.d(TAG, "createCountriesList: Some country doesn't have a flag or viceversa");
        }
    }

    private void buildRecyclerView() {
        flagsRecView.setHasFixedSize(true);
        flagsRecView.setLayoutManager(new GridLayoutManager(this,3));
        countryAdapter = new CountryAdapter(this,countriesList);
        flagsRecView.setAdapter(countryAdapter);
        handleClickOnFlagEvent(countryAdapter);
    }

    private void handleClickOnFlagEvent(CountryAdapter countryAdapter) {

        countryAdapter.setOnItemClickListener(new CountryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d(TAG, "onItemClick: clicked " + position);

                Toast toast = Toast.makeText(MealCountriesFlagsActivity.this,
                        "You touched on " + countriesList.get(position).getName(),
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();


                //Get Nationality informations of the clicked stove
                String flagUrl = countriesList.get(position).getFlag_img_url();
                String countryName = countriesList.get(position).getName();

                //Pass it to the intent
                Intent intent = new Intent(MealCountriesFlagsActivity.this, BrowseMealsByCountryActivity.class);
                intent.putExtra("flag_url",     flagUrl );
                intent.putExtra("country_name", countryName);

                startActivity(intent);

            }
        });
    }
}

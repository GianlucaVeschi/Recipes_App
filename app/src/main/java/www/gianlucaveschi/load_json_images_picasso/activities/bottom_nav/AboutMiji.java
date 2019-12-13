package www.gianlucaveschi.load_json_images_picasso.activities.bottom_nav;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.gianlucaveschi.load_json_images_picasso.R;

import www.gianlucaveschi.load_json_images_picasso.activities.details.StoveDetailsActivity;
import www.gianlucaveschi.load_json_images_picasso.adapters.StoveAdapter;
import www.gianlucaveschi.load_json_images_picasso.models.stoves.StoveProduct;
import www.gianlucaveschi.load_json_images_picasso.utils.BottomNavigationViewHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by sheck on 26/09/2019.
 */

public class AboutMiji extends AppCompatActivity {

    //MyLogger can be at most 23 chars
    private static final String TAG = "ProductsOverviewAct";

    //Stoves Array
    ArrayList<StoveProduct> mProductsList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_miji);
        Log.d(TAG, "onCreate: ");

        createProductList();
        buildRecyclerView();

        //Set the Bottom Navigation Bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView_Bar);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        //Highlight the touched button
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

    }

    private void createProductList() {
        Log.d(TAG, "createProductList: Adding products to the list");

        mProductsList.add(new StoveProduct(R.drawable.miji_gala_1,"Miji GALA", getString(R.string.Gala_El_1600W_desc)));
        mProductsList.add(new StoveProduct(R.drawable.miji_neo_1,"Miji NEO",getString(R.string.Neo_El_1600W_desc)));
        mProductsList.add(new StoveProduct(R.drawable.miji_gala_1,"Miji GALA", getString(R.string.Gala_El_1600W_desc)));
        mProductsList.add(new StoveProduct(R.drawable.miji_neo_1,"Miji NEO",getString(R.string.Neo_El_1600W_desc)));
        mProductsList.add(new StoveProduct(R.drawable.miji_gala_1,"Miji GALA", getString(R.string.Gala_El_1600W_desc)));
        mProductsList.add(new StoveProduct(R.drawable.miji_neo_1,"Miji NEO",getString(R.string.Neo_El_1600W_desc)));
        mProductsList.add(new StoveProduct(R.drawable.miji_gala_1,"Miji GALA", getString(R.string.Gala_El_1600W_desc)));
        mProductsList.add(new StoveProduct(R.drawable.miji_neo_1,"Miji NEO",getString(R.string.Neo_El_1600W_desc)));
        mProductsList.add(new StoveProduct(R.drawable.miji_gala_1,"Miji GALA", getString(R.string.Gala_El_1600W_desc)));
        mProductsList.add(new StoveProduct(R.drawable.miji_neo_1,"Miji NEO",getString(R.string.Neo_El_1600W_desc)));
    }

    private void buildRecyclerView() {

        RecyclerView mRecyclerView = findViewById(R.id.products_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        StoveAdapter mAdapter = new StoveAdapter(mProductsList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        /**
         * What actually happens when a ViewHolder is clicked
         * @param position
         */
        mAdapter.setOnItemClickListener(new StoveAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d(TAG, "onItemClick: clicked " + position);

                //Get StoveProduct Informations of the clicked stove
                String stoveTitle = mProductsList.get(position).getTitle();
                int stoveImgResource = mProductsList.get(position).getImageResource();

                //todo Add parcelable
                //Pass it to the intent
                Intent intent = new Intent(AboutMiji.this, StoveDetailsActivity.class);
                intent.putExtra("stove_image_resource", stoveImgResource);
                intent.putExtra("stove_title", stoveTitle );

                startActivity(intent);
            }
        });
    }

    /**
     * Bottom Navigation View
     * */
    //Handles the logic of the Bottom Navigation View
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch(item.getItemId()){
                case R.id.navigation_about_me:
                    Intent intentAboutMe = new Intent(AboutMiji.this, AboutMeActivity.class);
                    startActivity(intentAboutMe);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    break;

                case R.id.navigation_about_meals:
                    Intent intentAboutMeals = new Intent(AboutMiji.this, MainActivity.class);
                    startActivity(intentAboutMeals);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    break;

            }
            return false;
        }
    };
}

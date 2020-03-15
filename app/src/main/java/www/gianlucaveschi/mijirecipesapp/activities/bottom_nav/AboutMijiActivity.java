package www.gianlucaveschi.mijirecipesapp.activities.bottom_nav;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gianlucaveschi.load_json_images_picasso.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import www.gianlucaveschi.mijirecipesapp.activities.details.StoveDetailsActivity;
import www.gianlucaveschi.mijirecipesapp.adapters.stoves.OnStoveClickListener;
import www.gianlucaveschi.mijirecipesapp.adapters.stoves.StoveAdapter;
import www.gianlucaveschi.mijirecipesapp.bluetooth.MainBluetoothActivity;
import www.gianlucaveschi.mijirecipesapp.models.Stove;
import www.gianlucaveschi.mijirecipesapp.utils.UI.BottomNavigationViewHelper;
import www.gianlucaveschi.mijirecipesapp.utils.UI.HorizontalSpacingItemDecorator;

/**
 * Created by sheck on 26/09/2019.
 */

public class AboutMijiActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AboutMijiActivity";
    private ArrayList<Stove> mStovesList = new ArrayList<>();
    private ArrayList<Stove> mHobsList = new ArrayList<>();


    @BindView(R.id.bottom_nav_view) BottomNavigationView bottomNavigationView;
    @BindView(R.id.connectTemperatureSensor) Button scanMijiDevices;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_miji);
        ButterKnife.bind(this);
        scanMijiDevices.setOnClickListener(this);
        setBottomNavigation();

        createProductsList();
        buildStovesRecView();
        buildHobsRecView();

    }

    private void createProductsList() {

        //STOVES
        mStovesList.add(new Stove(R.drawable.miji_gala_1,"Miji GALA", getString(R.string.Gala_El_1600W)));
        mStovesList.add(new Stove(R.drawable.miji_star_5_chef_domino,"Miji STAR 5 CHEF",getString(R.string.Star_5_Chef_Domino)));
        mStovesList.add(new Stove(R.drawable.miji_twist,"Miji STAR 3 TWIST", getString(R.string.Star_3_Twist)));
        mStovesList.add(new Stove(R.drawable.miji_gourmet,"Miji GOURMET",getString(R.string.Gourmet_EITP_2800_FI)));

        //HOBS
        mHobsList.add(new Stove(R.drawable.miji_star_5_plus_domino,"Miji PLUS DOMINO",getString(R.string.Star_5_Plus_Domino)));
        mHobsList.add(new Stove(R.drawable.miji_star_5_miradur_v12,"Miji MIRADUR v12",getString(R.string.Star_5_Miradur_v12)));
        mHobsList.add(new Stove(R.drawable.miji_star_5_miradur_quadro,"Miji MIRADUR QUADRO",getString(R.string.Star_5_Miradur_Quadro)));

    }

    private void buildStovesRecView() {

        RecyclerView mRecyclerView = findViewById(R.id.products_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this,2);
        StoveAdapter stoveAdapter = new StoveAdapter(mStovesList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(stoveAdapter);

        stoveAdapter.setOnStoveClickListener(new OnStoveClickListener() {
            @Override
            public void onItemClick(int position) {

                //Get Stove Informations of the clicked stove
                String stoveTitle = mStovesList.get(position).getTitle();
                int stoveImgResource = mStovesList.get(position).getImageResource();

                //Pass it to the intent
                Intent intent = new Intent(AboutMijiActivity.this, StoveDetailsActivity.class);
                intent.putExtra("stove_image_resource", stoveImgResource);
                intent.putExtra("stove_title", stoveTitle );

                startActivity(intent);
            }
        });
    }
    
    private void buildHobsRecView(){
        RecyclerView hobsRecView = findViewById(R.id.hobs_rec_view);
        hobsRecView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this,RecyclerView.HORIZONTAL, false);
        StoveAdapter hobsAdapter = new StoveAdapter(mHobsList);

        HorizontalSpacingItemDecorator itemDecorator = new HorizontalSpacingItemDecorator(30);
        hobsRecView.addItemDecoration(itemDecorator);

        hobsRecView.setLayoutManager(mLayoutManager);
        hobsRecView.setAdapter(hobsAdapter);

        hobsAdapter.setOnStoveClickListener(new OnStoveClickListener() {
            @Override
            public void onItemClick(int position) {

                //Get Stove Informations of the clicked stove
                String hobTitle = mHobsList.get(position).getTitle();
                int hobImgResource = mHobsList.get(position).getImageResource();

                //Pass it to the intent
                Intent intent = new Intent(AboutMijiActivity.this, StoveDetailsActivity.class);
                intent.putExtra("stove_image_resource", hobImgResource);
                intent.putExtra("stove_title", hobTitle );

                startActivity(intent);
            }
        });
    }
    
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.connectTemperatureSensor) {
            Toast.makeText(this, "connect to miji device", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AboutMijiActivity.this, MainBluetoothActivity.class);
            startActivity(intent);
        }
    }


    /*--------------------------------- BOTTOM NAV VIEW ------------------------------------------*/
    private void setBottomNavigation() {
        //Set the Bottom Navigation Bar
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        //Highlight the touched button
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_about_me:
                    Intent intentAboutMe = new Intent(AboutMijiActivity.this, AboutMeActivity.class);
                    startActivity(intentAboutMe);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    break;

                case R.id.navigation_about_meals:
                    Intent intentAboutMeals = new Intent(AboutMijiActivity.this, AboutMealsActivity.class);
                    startActivity(intentAboutMeals);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    break;
            }
            return false;
        }
    };
}

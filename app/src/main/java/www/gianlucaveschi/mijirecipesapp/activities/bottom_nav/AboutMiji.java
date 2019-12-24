package www.gianlucaveschi.mijirecipesapp.activities.bottom_nav;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;

import com.gianlucaveschi.load_json_images_picasso.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import www.gianlucaveschi.mijirecipesapp.activities.bluetooth.MainBluetoothActivity;
import www.gianlucaveschi.mijirecipesapp.activities.details.MijiProductsOverview;
import www.gianlucaveschi.mijirecipesapp.utils.BottomNavigationViewHelper;

/**
 * Created by sheck on 26/09/2019.
 */

public class AboutMiji extends AppCompatActivity {

    private static final String TAG = "AboutMiji";

    @BindView(R.id.goToProducts_btn) Button goToProductsOverview;
    @BindView(R.id.goToMainBluetooth_btn) Button goToBtDiscovery;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_miji);
        ButterKnife.bind(this);

        //Set the Bottom Navigation Bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        //Highlight the touched button
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        goToProductsOverview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutMiji.this, MijiProductsOverview.class);
                startActivity(intent);
            }
        });

        goToBtDiscovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutMiji.this, MainBluetoothActivity.class);
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
                    Intent intentAboutMeals = new Intent(AboutMiji.this, AboutMeals.class);
                    startActivity(intentAboutMeals);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    break;

            }
            return false;
        }
    };
}

package www.gianlucaveschi.mijirecipesapp.activities.bottom_nav;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import www.gianlucaveschi.mijirecipesapp.utils.UI.BottomNavigationViewHelper;
import com.gianlucaveschi.load_json_images_picasso.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by sheck on 23/09/2019.
 */

public class AboutMeActivity extends BaseActivity {

    private static final String TAG = "AboutMeActivity";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        //Set the button
        Button aboutUsBtn = findViewById(R.id.about_us_btn);
        Button testGetData = findViewById(R.id.view_fav_rec_btn);

        aboutUsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: about us");
                if(baseActivityText.getVisibility() == View.VISIBLE){
                    showText(false);
                }
                else{
                    showText(true);
                }
            }
        });

        testGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AboutMeActivity.this, "NO EXIT PATH", Toast.LENGTH_SHORT).show();
            }
        });

        //removes the animation in the bottom bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Highlight the touched button
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

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
                case R.id.navigation_about_miji:
                    Intent intentAboutMiji = new Intent(AboutMeActivity.this, AboutMijiActivity.class);
                    startActivity(intentAboutMiji);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    break;

                case R.id.navigation_about_meals:
                    Intent intentAboutMeals = new Intent(AboutMeActivity.this, AboutMealsActivity.class);
                    startActivity(intentAboutMeals);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    break;
            }
            return false;
        }
    };

}
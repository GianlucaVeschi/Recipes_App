package www.gianlucaveschi.load_json_images_picasso.activities.bottom_nav;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import www.gianlucaveschi.load_json_images_picasso.adapters.StoveAdapter;
import www.gianlucaveschi.load_json_images_picasso.utils.BottomNavigationViewHelper;
import com.gianlucaveschi.load_json_images_picasso.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by sheck on 23/09/2019.
 */

public class AboutMeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        //Set the text View
        TextView title = findViewById(R.id.textViewAboutMe);

        //removes the animation in the bottom bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView_Bar);
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
                    Intent intentAboutMiji = new Intent(AboutMeActivity.this, AboutMiji.class);
                    startActivity(intentAboutMiji);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    break;

                case R.id.navigation_about_meals:
                    Intent intentAboutMeals = new Intent(AboutMeActivity.this, MainActivity.class);
                    startActivity(intentAboutMeals);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    break;

            }
            return false;
        }
    };

}
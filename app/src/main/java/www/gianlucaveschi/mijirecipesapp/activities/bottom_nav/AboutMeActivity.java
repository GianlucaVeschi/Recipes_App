package www.gianlucaveschi.mijirecipesapp.activities.bottom_nav;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import www.gianlucaveschi.mijirecipesapp.adapters.SectionsPagerAdapter;
import www.gianlucaveschi.mijirecipesapp.utils.UI.BottomNavigationViewHelper;
import com.gianlucaveschi.load_json_images_picasso.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by sheck on 23/09/2019.
 */

public class AboutMeActivity extends AppCompatActivity {

    private static final String TAG = "AboutMeActivity";

    @BindView(R.id.bottom_nav_view) BottomNavigationView mBottomNavigationView;
    @BindView(R.id.view_pager) ViewPager mViewPager;  //will host the section contents
    @BindView(R.id.tabs)    TabLayout mTabs;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        ButterKnife.bind(this);

        setBottomNavigation();
        setFragmentAdapter();
    }

    private void setFragmentAdapter() {
        mSectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mTabs.setupWithViewPager(mViewPager);
    }

    private void setBottomNavigation() {
        //Set the Bottom Navigation Bar
        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(mBottomNavigationView);

        //Highlight the touched button
        Menu menu = mBottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
    }

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

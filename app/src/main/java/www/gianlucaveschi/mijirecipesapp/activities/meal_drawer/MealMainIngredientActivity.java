package www.gianlucaveschi.mijirecipesapp.activities.meal_drawer;

import android.os.Bundle;
import android.widget.TextView;

import com.gianlucaveschi.load_json_images_picasso.R;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MealMainIngredientActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_main_ingredient);

        textView = (findViewById(R.id.nav_meal_main_ingredient));

        //Slide back to the Previous Activity
        SlidrInterface slidr = Slidr.attach(this);
    }
}

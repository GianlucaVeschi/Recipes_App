package www.gianlucaveschi.mijirecipesapp.activities.meal_drawer;

import android.os.Bundle;
import android.widget.TextView;

import com.gianlucaveschi.load_json_images_picasso.R;
import com.r0adkll.slidr.Slidr;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MealCategoriesActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_categories);
        textView = (findViewById(R.id.meal_categories_text_view));

        //Slide back to the Previous Activity
        Slidr.attach(this);
    }
}

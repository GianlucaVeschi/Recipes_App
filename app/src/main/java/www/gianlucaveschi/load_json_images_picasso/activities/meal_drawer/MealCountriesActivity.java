package www.gianlucaveschi.load_json_images_picasso.activities.meal_drawer;

import android.os.Bundle;
import android.widget.TextView;

import com.gianlucaveschi.load_json_images_picasso.R;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MealCountriesActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_countries);
        textView = (findViewById(R.id.meal_countries_text_view));

        //Slide back to the Previous Activity
        SlidrInterface slidr = Slidr.attach(this);
    }
}

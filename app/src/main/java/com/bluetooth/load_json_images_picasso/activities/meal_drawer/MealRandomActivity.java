package com.bluetooth.load_json_images_picasso.activities.meal_drawer;

import android.os.Bundle;
import android.widget.TextView;

import com.bluetooth.load_json_images_picasso.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MealRandomActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_random);

        textView = (findViewById(R.id.meal_random_text_view));
    }
}

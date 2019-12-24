package www.gianlucaveschi.mijirecipesapp.activities.bluetooth;

import android.os.Bundle;

import com.gianlucaveschi.load_json_images_picasso.R;
import com.r0adkll.slidr.Slidr;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PeripheralControlActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peripheral_control);


        //Slide back to the Previous Activity
        Slidr.attach(this);
    }
}

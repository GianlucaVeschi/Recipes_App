package www.gianlucaveschi.mijirecipesapp.activities.details;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.gianlucaveschi.load_json_images_picasso.R;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by sheck on 26/09/2019.
 */

public class StoveDetailsActivity extends AppCompatActivity {
    private static final String TAG = "StoveDetailsActivity";

    //Layout details
    ImageView image;
    TextView name;
    TextView desc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stove_details);
        Log.d(TAG, "onCreate: started");
        getIncomingIntent();

        //Slide back to the Previous Activity
        Slidr.attach(this);
    }

    private void getIncomingIntent(){
        //check if the intent actually has an extra otherwise the app will crash
        if(getIntent().hasExtra("stove_image_resource") && getIntent().hasExtra("stove_title")){

            int stoveImgResource = getIntent().getIntExtra("stove_image_resource",0);
            String stoveTitle = getIntent().getStringExtra("stove_title");

            setStoveDetails(stoveImgResource,stoveTitle);
        }
        else{
            Log.d(TAG, "getIncomingIntent: The intent has no extras");
        }
    }

    private void setStoveDetails(int imageResource, String imageName){

        image = findViewById(R.id.image_view_id);
        name = findViewById(R.id.image_title_id);
        desc = findViewById(R.id.image_desc_id);

        Log.d(TAG, "setStoveDetails: setting the image and name to widgets.");

        image.setImageResource(imageResource);
        name.setText(imageName);
        desc.setText(setProductDescription(imageName));

    }

    private String setProductDescription(String imageName){
        Log.d(TAG, "setProductDescription: for product " + imageName);
        switch (imageName){
            case "Miji GALA":
                return getString(R.string.Gala_El_2000W);
            case "Miji STAR 3 TWIST":
                return getString(R.string.Star_3_Twist);
            case "Miji STAR 5 CHEF":
                return getString(R.string.Star_5_Chef_Domino);
            case "Miji GOURMET":
                return getString(R.string.Gourmet_EITP_2800_FI);
            default:
                return "No description available yet";
        }

    }
}

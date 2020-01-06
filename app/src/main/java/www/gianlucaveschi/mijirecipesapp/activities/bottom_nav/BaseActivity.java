package www.gianlucaveschi.mijirecipesapp.activities.bottom_nav;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gianlucaveschi.load_json_images_picasso.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

// TODO: 06/01/2020 : implement the navigation bar logic in this class
public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";

    public TextView baseActivityText;

    @Override
    public void setContentView(int layoutResID) {

        RelativeLayout relativeLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.activity_base,null);
        FrameLayout frameLayout = relativeLayout.findViewById(R.id.activity_content);
        baseActivityText        = relativeLayout.findViewById(R.id.baseActivityText);

        getLayoutInflater().inflate(layoutResID, frameLayout, true);
        super.setContentView(relativeLayout);
    }

    public void showText(boolean visible){
        if(visible){
            baseActivityText.setVisibility(View.VISIBLE);
        }
        else{
            baseActivityText.setVisibility(View.INVISIBLE);
        }
    }
}

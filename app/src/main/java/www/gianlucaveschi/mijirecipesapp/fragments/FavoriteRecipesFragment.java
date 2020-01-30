package www.gianlucaveschi.mijirecipesapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gianlucaveschi.load_json_images_picasso.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FavoriteRecipesFragment extends Fragment {

    private static final String TAG = "FavoriteRecipesFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite_recipes, container, false);
    }
}

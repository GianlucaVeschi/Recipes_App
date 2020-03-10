package www.gianlucaveschi.mijirecipesapp.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.gianlucaveschi.load_json_images_picasso.R;

import java.io.FileNotFoundException;
import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class UserDetailsFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "UserDetailsFragment";
    private static final int RESULT_LOAD_IMAGE = 1;

    private ImageView profilePicture;
    private ImageView editInformation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profilePicture = getView().findViewById(R.id.profile);
        profilePicture.setOnClickListener(this);

        editInformation = getView().findViewById(R.id.editInformation);
        editInformation.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case(R.id.profile):
                Log.d(TAG, "onClick: setting profile picture");
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                break;
            case(R.id.editInformation):
                Toast.makeText(getContext(), "You can't edit the info yet", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //Method called when a User has selected a picture from the gallery
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            profilePicture.setImageURI(selectedImage);
        }
    }
}

package www.gianlucaveschi.mijirecipesapp.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gianlucaveschi.load_json_images_picasso.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import www.gianlucaveschi.mijirecipesapp.login.ui.LoginActivity;

public class UserDetailsFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "UserDetailsFragment";
    private static final int RESULT_LOAD_IMAGE = 1;

    private ImageView profilePicture;
    private ImageView editInformation;
    private View userDetailsContent;
    private View userDetailsSetterView;
    private EditText lastEditText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        //update Infos when Enter is pressed in last edit text
//        lastEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    Toast.makeText(getActivity(), "User infos updated", Toast.LENGTH_SHORT).show();
//                }
//                return false;
//            }
//        });
        return inflater.inflate(R.layout.fragment_user_details, container, false);
    }

    private void attachUIcomponents() {
        userDetailsContent = getActivity().findViewById(R.id.user_details_content);
        userDetailsSetterView = getActivity().findViewById(R.id.user_details_setter);
        lastEditText = getActivity().findViewById(R.id.lastEditText);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userDetailsContent = getActivity().findViewById(R.id.user_details_content);
        userDetailsSetterView = getActivity().findViewById(R.id.user_details_setter);
        lastEditText = getActivity().findViewById(R.id.lastEditText);
        profilePicture = getView().findViewById(R.id.profile);
        editInformation = getView().findViewById(R.id.editInformation);

        profilePicture.setOnClickListener(this);
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
                if(userDetailsContent.getVisibility() == View.VISIBLE){
                    userDetailsContent.setVisibility(View.GONE);
                    userDetailsSetterView.setVisibility(View.VISIBLE);
                }
                else{
                    userDetailsContent.setVisibility(View.VISIBLE);
                    userDetailsSetterView.setVisibility(View.GONE);
                }
                break;
        }
    }

    //Method called when a User has selected a picture from the gallery
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && data != null){
            Uri selectedImageURI = data.getData();
            profilePicture.setImageURI(selectedImageURI);
            Log.d(TAG, "onActivityResult: Image URI : " + selectedImageURI.toString());

            //Save the picture in the memory

        }
    }

    // TODO: 15/03/2020 Save picture in memory
    private void addToGallery(Uri picUri) {
        Intent galleryIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        galleryIntent.setData(picUri);
        getContext().sendBroadcast(galleryIntent);
    }
}

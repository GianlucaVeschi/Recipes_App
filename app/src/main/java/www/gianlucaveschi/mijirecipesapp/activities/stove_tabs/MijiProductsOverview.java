package www.gianlucaveschi.mijirecipesapp.activities.stove_tabs;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.gianlucaveschi.load_json_images_picasso.R;

import www.gianlucaveschi.mijirecipesapp.activities.details.StoveDetailsActivity;
import www.gianlucaveschi.mijirecipesapp.adapters.StoveAdapter;
import www.gianlucaveschi.mijirecipesapp.models.stoves.StoveProduct;

import com.r0adkll.slidr.Slidr;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by sheck on 26/09/2019.
 */

public class MijiProductsOverview extends AppCompatActivity {

    //MyLogger can be at most 23 chars
    private static final String TAG = "ProductsOverviewAct";

    //Stoves Array
    ArrayList<StoveProduct> mProductsList = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_overview);
        Log.d(TAG, "onCreate: ");

        createProductList();
        buildRecyclerView();

        //Slide back to the Previous Activity
        Slidr.attach(this);

    }

    private void createProductList() {
        Log.d(TAG, "createProductList: Adding products to the list");

        mProductsList.add(new StoveProduct(R.drawable.miji_gala_1,"Miji GALA", getString(R.string.Gala_El_1600W_desc)));
        mProductsList.add(new StoveProduct(R.drawable.miji_neo_1,"Miji NEO",getString(R.string.Neo_El_1600W_desc)));
        mProductsList.add(new StoveProduct(R.drawable.miji_gala_1,"Miji GALA", getString(R.string.Gala_El_1600W_desc)));
        mProductsList.add(new StoveProduct(R.drawable.miji_neo_1,"Miji NEO",getString(R.string.Neo_El_1600W_desc)));
        mProductsList.add(new StoveProduct(R.drawable.miji_gala_1,"Miji GALA", getString(R.string.Gala_El_1600W_desc)));
        mProductsList.add(new StoveProduct(R.drawable.miji_neo_1,"Miji NEO",getString(R.string.Neo_El_1600W_desc)));
        mProductsList.add(new StoveProduct(R.drawable.miji_gala_1,"Miji GALA", getString(R.string.Gala_El_1600W_desc)));
        mProductsList.add(new StoveProduct(R.drawable.miji_neo_1,"Miji NEO",getString(R.string.Neo_El_1600W_desc)));
        mProductsList.add(new StoveProduct(R.drawable.miji_gala_1,"Miji GALA", getString(R.string.Gala_El_1600W_desc)));
        mProductsList.add(new StoveProduct(R.drawable.miji_neo_1,"Miji NEO",getString(R.string.Neo_El_1600W_desc)));
    }

    private void buildRecyclerView() {

        RecyclerView mRecyclerView = findViewById(R.id.products_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        StoveAdapter mAdapter = new StoveAdapter(mProductsList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        /**
         * What actually happens when a ViewHolder is clicked
         * @param position
         */
        mAdapter.setOnItemClickListener(new StoveAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {


                //Get StoveProduct Informations of the clicked stove
                String stoveTitle = mProductsList.get(position).getTitle();
                int stoveImgResource = mProductsList.get(position).getImageResource();

                //todo Add parcelable
                //Pass it to the intent
                Intent intent = new Intent(MijiProductsOverview.this, StoveDetailsActivity.class);
                intent.putExtra("stove_image_resource", stoveImgResource);
                intent.putExtra("stove_title", stoveTitle );

                startActivity(intent);
            }
        });
    }
}

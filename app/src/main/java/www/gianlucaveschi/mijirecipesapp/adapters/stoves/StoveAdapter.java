package www.gianlucaveschi.mijirecipesapp.adapters.stoves;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.gianlucaveschi.load_json_images_picasso.R;

import www.gianlucaveschi.mijirecipesapp.models.stoves.StoveProduct;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by sheck on 25/09/2019.
 */

public class StoveAdapter extends RecyclerView.Adapter<StoveViewHolder> {

    private ArrayList<StoveProduct> mExampleList;
    private OnStoveClickListener mListener;

    //CONSTRUCTOR
    public StoveAdapter(ArrayList<StoveProduct> exampleList) {
        mExampleList = exampleList;
    }

    public void setOnStoveClickListener(OnStoveClickListener listener) {
        mListener = listener;
    }

    @Override
    public StoveViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_stove, parent, false);
        StoveViewHolder evh = new StoveViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(StoveViewHolder holder, int position) {
        StoveProduct currentItem = mExampleList.get(position);

        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mTextView1.setText(currentItem.getTitle());
        holder.mTextView2.setText(currentItem.getDescription());
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
}

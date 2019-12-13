package www.gianlucaveschi.load_json_images_picasso.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gianlucaveschi.load_json_images_picasso.R;
import www.gianlucaveschi.load_json_images_picasso.models.stoves.StoveProduct;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by sheck on 25/09/2019.
 */

public class StoveAdapter extends RecyclerView.Adapter<StoveAdapter.StoveViewHolder> {
    private ArrayList<StoveProduct> mExampleList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    //Inner ViewHolder
    public static class StoveViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;

        public StoveViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mTextView1 = itemView.findViewById(R.id.textViewTitle);
            mTextView2 = itemView.findViewById(R.id.textViewDesc);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    //CONSTRUCTOR
    public StoveAdapter(ArrayList<StoveProduct> exampleList) {
        mExampleList = exampleList;
    }

    @Override
    public StoveViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_product_overview, parent, false);
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

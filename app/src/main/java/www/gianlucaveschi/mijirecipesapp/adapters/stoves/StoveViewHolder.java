package www.gianlucaveschi.mijirecipesapp.adapters.stoves;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gianlucaveschi.load_json_images_picasso.R;

import androidx.recyclerview.widget.RecyclerView;

//Inner ViewHolder
public class StoveViewHolder extends RecyclerView.ViewHolder {
    public ImageView mImageView;
    public TextView mTextView1;
    public TextView mTextView2;

    public StoveViewHolder(View itemView, final OnStoveClickListener listener) {
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
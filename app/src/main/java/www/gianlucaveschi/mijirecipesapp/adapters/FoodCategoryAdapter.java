package www.gianlucaveschi.mijirecipesapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gianlucaveschi.load_json_images_picasso.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class FoodCategoryAdapter extends RecyclerView.Adapter<FoodCategoryAdapter.FoodCategoryViewHolder> {

    private ArrayList<String> mFoodCategories;
    private OnFoodCategoryClickListener mListener;

    public FoodCategoryAdapter(ArrayList<String> mFoodCategories) {
        this.mFoodCategories = mFoodCategories;
    }

    @NonNull
    @Override
    public FoodCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_food_category, parent, false);
        FoodCategoryViewHolder vh = new FoodCategoryViewHolder(view, mListener,mFoodCategories);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull FoodCategoryViewHolder holder, int position) {
        String currentItem = mFoodCategories.get(position);
        holder.foodCategoryTextView.setText(currentItem);
    }

    @Override
    public int getItemCount() {
        if(mFoodCategories != null){
            return mFoodCategories.size();
        }
        return 0;
    }

    /**
     * Inner View Holder
     * */
    public static class FoodCategoryViewHolder extends RecyclerView.ViewHolder {
        TextView foodCategoryTextView;

        public FoodCategoryViewHolder(View itemView, final OnFoodCategoryClickListener listener, final ArrayList<String> foodCategories) {
            super(itemView);
            foodCategoryTextView = itemView.findViewById(R.id.foodCategoryTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onFoodCategoryClick(position,foodCategories);
                        }
                    }
                }
            });
        }
    }

    public interface OnFoodCategoryClickListener {
        void onFoodCategoryClick(int position, ArrayList<String> arrayList);
    }

    public void setOnFoodCategoryClickListener(OnFoodCategoryClickListener listener){
        mListener = listener;
    }

}

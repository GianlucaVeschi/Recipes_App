package com.bluetooth.load_json_images_picasso.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bluetooth.load_json_images_picasso.Meal;
import com.bluetooth.load_json_images_picasso.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HorizontalMealAdapter extends RecyclerView.Adapter<HorizontalMealAdapter.MealViewHolder> {

    private Context mContext;
    private ArrayList<Meal> mMealsList;

    /**ON CLICK LISTENER utils*/
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position, ArrayList<Meal> mealsList);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    /**
     * CLASS CONSTRUCTOR
     * */
    public HorizontalMealAdapter(Context context, ArrayList<Meal> mealsList) {
        mContext = context;
        mMealsList = mealsList;
    }

    /**
     * INNER VIEW HOLDER
     * */
    public class MealViewHolder extends RecyclerView.ViewHolder{

        public ImageView mImageView;
        public TextView m_tv_RecipeName;
        public TextView m_tv_RecipeID;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.image_view);
            m_tv_RecipeName = itemView.findViewById(R.id.recipe_name);
            m_tv_RecipeID = itemView.findViewById(R.id.recipe_id);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            mListener.onItemClick(position,mMealsList);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.meal_horizontal_item, parent, false);
        return new MealViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {

        Meal currentItem = mMealsList.get(position);

        String imageUrl = currentItem.getImgUrl();
        String mealName = currentItem.getMealName();
        String idMeal = currentItem.getIdMeal();

        holder.m_tv_RecipeName.setText(mealName);
        holder.m_tv_RecipeID.setText("ID : " + idMeal);
        Picasso.get()
                .load(imageUrl)
                .fit()
                .centerInside()
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mMealsList.size();
    }

}

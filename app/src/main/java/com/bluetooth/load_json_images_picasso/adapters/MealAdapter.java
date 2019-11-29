package com.bluetooth.load_json_images_picasso.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bluetooth.load_json_images_picasso.models.Meal;
import com.bluetooth.load_json_images_picasso.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MealAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<Meal> mMealsList;

    //Distinguish between the two TypeViews
    private final static int HORIZONTAL_VIEW_TYPE = 1;
    private final static int VERTICAL_VIEW_TYPE = 2;

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
    public MealAdapter(Context context, ArrayList<Meal> mealsList) {
        mContext = context;
        mMealsList = mealsList;
    }

    /**
     * INNER HORIZONTAL VIEW HOLDER
     * */
    public class HorizontalViewHolder extends RecyclerView.ViewHolder{

        public ImageView mImageView;
        public TextView m_tv_RecipeName;
        public TextView m_tv_RecipeID;

        public HorizontalViewHolder(@NonNull View itemView) {
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

    /**
     * INNER VERTICAL VIEW HOLDER
     * */
    public class VerticalViewHolder extends RecyclerView.ViewHolder{

        public ImageView mImageView;
        public TextView m_tv_RecipeName;
        public TextView m_tv_RecipeID;

        public VerticalViewHolder(@NonNull View itemView) {
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

    /**
     * Overridden Methods of the ViewHolders
     * */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == HORIZONTAL_VIEW_TYPE) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.meal_horizontal_item, parent, false);
            return new HorizontalViewHolder(v);
        }
        else if (viewType == VERTICAL_VIEW_TYPE){
            View v = LayoutInflater.from(mContext).inflate(R.layout.meal_vertical_item, parent, false);
            return new VerticalViewHolder(v);
        }
        else {
            throw new RuntimeException("The type has to be VERTICAL or HORIZONTAL");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == HORIZONTAL_VIEW_TYPE) {
            HorizontalViewHolder horizontalViewHolder = (HorizontalViewHolder) holder;
            initHorizLayout(horizontalViewHolder,position);
        }
        else {
            VerticalViewHolder verticalViewHolder = (VerticalViewHolder) holder;
            initVertLayout(verticalViewHolder,position);
        }

    }

    @Override
    public int getItemCount() {
        return mMealsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mMealsList.get(position).getorientationType() == HORIZONTAL_VIEW_TYPE) {
            return HORIZONTAL_VIEW_TYPE;

        } else {
            return VERTICAL_VIEW_TYPE;
        }
    }

    private void initHorizLayout(HorizontalViewHolder horizontalViewHolder, int position) {
        Meal currentItem = mMealsList.get(position);

        String imageUrl = currentItem.getImgUrl();
        String mealName = currentItem.getMealName();
        String idMeal = currentItem.getIdMeal();

        horizontalViewHolder.m_tv_RecipeName.setText(mealName);
        horizontalViewHolder.m_tv_RecipeID.setText("ID : " + idMeal);
        Picasso.get()
                .load(imageUrl)
                .fit()
                .centerInside()
                .into(horizontalViewHolder.mImageView);
    }

    private void initVertLayout(VerticalViewHolder verticalViewHolder, int position) {
        Meal currentItem = mMealsList.get(position);

        String imageUrl = currentItem.getImgUrl();
        String mealName = currentItem.getMealName();
        String idMeal = currentItem.getIdMeal();

        verticalViewHolder.m_tv_RecipeName.setText(mealName);
        verticalViewHolder.m_tv_RecipeID.setText("ID : " + idMeal);
        Picasso.get()
                .load(imageUrl)
                .fit()
                .centerInside()
                .into(verticalViewHolder.mImageView);
    }
}

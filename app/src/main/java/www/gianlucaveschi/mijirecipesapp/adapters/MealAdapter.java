package www.gianlucaveschi.mijirecipesapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.gianlucaveschi.load_json_images_picasso.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import www.gianlucaveschi.mijirecipesapp.models.meals.MealSimple;
import www.gianlucaveschi.mijirecipesapp.models.others.Country;

public class MealAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private Context mContext;
    private ArrayList<MealSimple> mMealsList;
    private ArrayList<MealSimple> mealsListFull;

    //Distinguish between the two TypeViews
    private final static int HORIZONTAL_VIEW_TYPE = 1;
    private final static int VERTICAL_VIEW_TYPE = 2;


    /**ON CLICK LISTENER utils*/

    //Create internal Interface
    public interface OnItemClickListener{
        void onItemClick(int position, ArrayList<MealSimple> mealsList);
    }

    //Create Member variable
    private OnItemClickListener mListener;

    //
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    /**
     * ADAPTER CONSTRUCTOR
     * */
    public MealAdapter(Context context, ArrayList<MealSimple> mealsList) {
        mContext = context;
        mMealsList = mealsList;
        mealsListFull = new ArrayList<>(mMealsList);
    }

    /**
     * INNER HORIZONTAL VIEW HOLDER
     * */
    public class HorizontalViewHolder extends RecyclerView.ViewHolder{

        ImageView mImageView;
        TextView m_tv_RecipeName;
        TextView m_tv_RecipeID;
        ProgressBar progressBar;

        private HorizontalViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.image_view);
            m_tv_RecipeName = itemView.findViewById(R.id.recipe_name);
            m_tv_RecipeID = itemView.findViewById(R.id.recipe_id);
            progressBar = itemView.findViewById(R.id.progress_bar);


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

        ImageView mImageView;
        TextView m_tv_RecipeName;
        TextView m_tv_RecipeID;
        ProgressBar progressBar;

        private VerticalViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.image_view);
            m_tv_RecipeName = itemView.findViewById(R.id.recipe_name);
            m_tv_RecipeID = itemView.findViewById(R.id.recipe_id);
            progressBar = itemView.findViewById(R.id.progress_bar);


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
        if (mMealsList.get(position).getOrientationType() == HORIZONTAL_VIEW_TYPE) {
            return HORIZONTAL_VIEW_TYPE;

        } else {
            return VERTICAL_VIEW_TYPE;
        }
    }

    private void initHorizLayout(HorizontalViewHolder horizontalViewHolder, int position) {
        MealSimple currentItem = mMealsList.get(position);

        String imageUrl = currentItem.getImgUrl();
        String mealName = currentItem.getMealName();
        String idMeal = currentItem.getIdMeal();

        horizontalViewHolder.progressBar.setVisibility(View.INVISIBLE);
        horizontalViewHolder.m_tv_RecipeName.setText(mealName);
        horizontalViewHolder.m_tv_RecipeID.setText("ID : " + idMeal);
        Picasso.get()
                .load(imageUrl)
                .fit()
                .centerInside()
                .into(horizontalViewHolder.mImageView);
    }

    private void initVertLayout(VerticalViewHolder verticalViewHolder, int position) {
        MealSimple currentItem = mMealsList.get(position);

        String imageUrl = currentItem.getImgUrl();
        String mealName = currentItem.getMealName();
        String idMeal = currentItem.getIdMeal();

        verticalViewHolder.progressBar.setVisibility(View.INVISIBLE);
        verticalViewHolder.m_tv_RecipeName.setText(mealName);
        verticalViewHolder.m_tv_RecipeID.setText("ID : " + idMeal);
        Picasso.get()
                .load(imageUrl)
                .fit()
                .centerInside()
                .into(verticalViewHolder.mImageView);
    }

    @Override
    public Filter getFilter() {
        return mealFilter;
    }

    private Filter mealFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence searchedText) {
            Log.d("mealAdapter", "PerformFiltering: ");
            ArrayList<MealSimple> filteredMealsList = new ArrayList<>();
            if (searchedText == null || searchedText.length() == 0) {
                filteredMealsList.addAll(mealsListFull);
            } else {

                //Get what the User wants to search
                String filterPattern = searchedText.toString().toLowerCase().trim();
                for (MealSimple item : mealsListFull) {
                    if (item.getMealName().toLowerCase().contains(filterPattern)) {
                        Log.d("CountryAdapter", "performFiltering: " + item.getMealName());
                        filteredMealsList.add(item);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredMealsList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence searchedText, FilterResults results) {
            mMealsList.clear();
            mMealsList.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };


}

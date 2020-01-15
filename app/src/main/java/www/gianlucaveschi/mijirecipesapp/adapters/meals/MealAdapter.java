package www.gianlucaveschi.mijirecipesapp.adapters.meals;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.gianlucaveschi.load_json_images_picasso.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import www.gianlucaveschi.mijirecipesapp.models.meals.MealSimple;

public class MealAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private Context mContext;
    private ArrayList<MealSimple> mMealsList;
    private ArrayList<MealSimple> mealsListFull;
    private OnMealClickListener mListener;

    //Distinguish between the two TypeViews
    private final static int HORIZONTAL_VIEW_TYPE = 1;
    private final static int VERTICAL_VIEW_TYPE = 2;

    /**
     * ADAPTER CONSTRUCTOR
     * */
    public MealAdapter(Context context, ArrayList<MealSimple> mealsList) {
        mContext = context;
        mMealsList = mealsList;
        mealsListFull = new ArrayList<>(mMealsList);
    }

    /**
     * Overridden Methods of the ViewHolders
     * */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch(viewType){
            case HORIZONTAL_VIEW_TYPE :
               view = LayoutInflater.from(mContext).inflate(R.layout.list_item_meal_horizontal, parent, false);
                return new HorizontalViewHolder(view,mListener,mMealsList);
            case VERTICAL_VIEW_TYPE :
                view = LayoutInflater.from(mContext).inflate(R.layout.list_item_meal_vertical, parent, false);
                return new VerticalViewHolder(view,mListener,mMealsList);
            default:
                throw new RuntimeException("The type has to be VERTICAL or HORIZONTAL");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == HORIZONTAL_VIEW_TYPE) {
            HorizontalViewHolder horizontalViewHolder = (HorizontalViewHolder) holder;
            horizontalViewHolder.initHorizLayout(horizontalViewHolder,position,mMealsList);
        }
        else {
            VerticalViewHolder verticalViewHolder = (VerticalViewHolder) holder;
            verticalViewHolder.initVertLayout(verticalViewHolder,position,mMealsList);
        }
    }

    @Override
    public int getItemCount() {
        if(mMealsList != null){
            return mMealsList.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (mMealsList.get(position).getOrientationType() == HORIZONTAL_VIEW_TYPE) {
            return HORIZONTAL_VIEW_TYPE;

        } else {
            return VERTICAL_VIEW_TYPE;
        }
    }



    /**ON CLICK LISTENER utils*/
    public void setOnMealClickListener(OnMealClickListener listener){
        mListener = listener;
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

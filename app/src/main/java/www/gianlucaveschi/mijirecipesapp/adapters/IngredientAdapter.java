package www.gianlucaveschi.mijirecipesapp.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gianlucaveschi.load_json_images_picasso.R;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import www.gianlucaveschi.mijirecipesapp.models.others.Ingredient;

/**
 * Created by sheck on 25/09/2019.
 */

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> implements Filterable {

    private Context context;
    private IngredientAdapter.OnItemClickListener mListener;

    private ArrayList<Ingredient> ingredientsList;
    private ArrayList<Ingredient> ingredientsListFull;

    //Click variables
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public IngredientAdapter(Context context, ArrayList<Ingredient> ingredientsList) {
        this.context = context;
        this.ingredientsList = ingredientsList;
        ingredientsListFull = new ArrayList<>(ingredientsList);

    }

    public void setOnItemClickListener(IngredientAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    /**
     * Inner View Holder
     * */
    public static class IngredientViewHolder extends RecyclerView.ViewHolder {
        ImageView flagImage;
        TextView IngredientName;

        public IngredientViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            flagImage = itemView.findViewById(R.id.ingredientImageView);
            IngredientName = itemView.findViewById(R.id.ingredientName);

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

    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_ingredient, parent, false);
        IngredientViewHolder cvh = new IngredientViewHolder(view, mListener);
        return cvh;
    }

    @Override
    public void onBindViewHolder(IngredientViewHolder holder, int position) {
        Ingredient currentItem = ingredientsList.get(position);

        Glide.with(context)
                .load(currentItem.getIngredientImgUrl())
                .into(holder.flagImage);

        holder.IngredientName.setText(currentItem.getName());
    }

    @Override
    public int getItemCount() {
        return ingredientsList.size();
    }

    @Override
    public Filter getFilter() {
        return ingredientFilter;
    }

    private Filter ingredientFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence searchedText) {
            ArrayList<Ingredient> filteredIngredientList = new ArrayList<>();
            if (searchedText == null || searchedText.length() == 0) {
                filteredIngredientList.addAll(ingredientsListFull);
            } else {

                //Get what the User wants to search
                String filterPattern = searchedText.toString().toLowerCase().trim();
                for (Ingredient item : ingredientsListFull) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredIngredientList.add(item);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredIngredientList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence searchedText, FilterResults results) {
            ingredientsList.clear();
            ingredientsList.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };
}

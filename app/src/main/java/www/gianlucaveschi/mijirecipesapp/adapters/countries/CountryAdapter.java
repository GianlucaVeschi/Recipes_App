package www.gianlucaveschi.mijirecipesapp.adapters.countries;


import android.content.Context;
import android.util.Log;
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
import www.gianlucaveschi.mijirecipesapp.models.others.Country;

/**
 * Created by sheck on 25/09/2019.
 */

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryViewHolder> implements Filterable {

    private Context context;
    private CountryAdapter.OnItemClickListener mListener;

    private ArrayList<Country> countriesList;
    private ArrayList<Country> countriesListFull;


    //Click variables
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public CountryAdapter(Context context, ArrayList<Country> countriesList) {
        this.context = context;
        this.countriesList = countriesList;
        countriesListFull = new ArrayList<>(countriesList);
    }

    public void setOnItemClickListener(CountryAdapter.OnItemClickListener listener) {
        mListener = listener;
    }


    /**
     * Inner View Holder
     * */
    public static class CountryViewHolder extends RecyclerView.ViewHolder {
        ImageView flagImage;
        TextView countryName;

        public CountryViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            flagImage = itemView.findViewById(R.id.countryImageView);
            countryName = itemView.findViewById(R.id.countryName);

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
    public CountryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_cardview, parent, false);
        CountryViewHolder cvh = new CountryViewHolder(view, mListener);
        return cvh;
    }

    @Override
    public void onBindViewHolder(CountryViewHolder holder, int position) {
        Country currentItem = countriesList.get(position);

        Glide.with(context)
                .load(currentItem.getFlag_img_url())
                .into(holder.flagImage);

        holder.countryName.setText(currentItem.getName());
    }

    @Override
    public int getItemCount() {
        return countriesList.size();
    }

    //Search Results
    @Override
    public Filter getFilter() {
        return countryFilter;
    }

    private Filter countryFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence searchedText) {
            Log.d("CountryAdapter", "PerformFiltering: ");
            ArrayList<Country> filteredCountriesList = new ArrayList<>();
            if (searchedText == null || searchedText.length() == 0) {
                filteredCountriesList.addAll(countriesListFull);
            } else {

                //Get what the User wants to search
                String filterPattern = searchedText.toString().toLowerCase().trim();
                for (Country item : countriesListFull) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        Log.d("CountryAdapter", "performFiltering: " + item.getName());
                        filteredCountriesList.add(item);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredCountriesList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence searchedText, FilterResults results) {
            countriesList.clear();
            countriesList.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };
}

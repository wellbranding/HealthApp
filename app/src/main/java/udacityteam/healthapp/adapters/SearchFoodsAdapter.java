package udacityteam.healthapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import udacityteam.healthapp.R;
import udacityteam.healthapp.activities.FoodNutritiensDisplay;
import udacityteam.healthapp.activities.FoodSearchActivity;
import udacityteam.healthapp.models.Model;


/**
 * Created by vvost on 11/16/2017.
 */

public class SearchFoodsAdapter extends RecyclerView.Adapter<SearchFoodsAdapter.ViewHolder>  {
    private static final String TAG = "CustomAdapter";

    private List<Model> mDataSet = new ArrayList<>();
    private Context context;

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(View v) {
            super(v);
            context = v.getContext();

            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // Toast.makeText(SearchFoodsAdapter.this, models.get(i).getId(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context, FoodNutritiensDisplay.class);
                    StringBuilder amm = new StringBuilder();
                    amm.append("https://api.nal.usda.gov/ndb/V2/reports?ndbno=");
                    amm.append(mDataSet.get(getAdapterPosition()).getId());
                    amm.append("&type=f&format=json&api_key=HXLecTDsMqy1Y6jNoYPw2n3DQ30FeGXxD2XBZqJh");
                    //new JSONTask().execute(amm.toString());
                    intent.putExtra("id", mDataSet.get(getAdapterPosition()).getId());
                    intent.putExtra("foodname", mDataSet.get(getAdapterPosition()).getName());
                    intent.putExtra("foodselection", FoodSearchActivity.foodselection);

                    context.startActivity(intent);
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                }
            });
                    textView = v.findViewById(R.id.textView);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    public SearchFoodsAdapter(List<Model> dataSet) {
        mDataSet = dataSet;


    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.food_list_item, viewGroup, false);

        return new ViewHolder(v);
    }

    // Replace the conten nts of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that elemen/
        viewHolder.getTextView().setText(mDataSet.get(position).getName());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
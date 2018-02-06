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

import udacityteam.healthapp.Model.SelectedFoodretrofit;
import udacityteam.healthapp.R;
import udacityteam.healthapp.activities.FoodNutritiensDisplayPrieview;
import udacityteam.healthapp.activities.CommunityActivities.CommunityFoodListsDisplayFragment0;

/**
 * Created by vvost on 11/16/2017.
 */

public class FoodListRetrofitAdapter extends RecyclerView.Adapter<FoodListRetrofitAdapter.ViewHolder> {
    private static final String TAG = "CustomAdapter";

    private List<SelectedFoodretrofit> mDataSet = new ArrayList<>();
    Context context;

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public  class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(View v) {
            super(v);
            context = v.getContext();
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FoodNutritiensDisplayPrieview.class);
                    intent.putExtra("id",mDataSet.get(getAdapterPosition()).getFoodid());
                    intent.putExtra("foodname", mDataSet.get(getAdapterPosition()).getFoodid());
                    intent.putExtra("foodselection", CommunityFoodListsDisplayFragment0.value); // will need to add own

                    context.startActivity(intent);
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
    public FoodListRetrofitAdapter(List<SelectedFoodretrofit> dataSet) {
        mDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);

        return new ViewHolder(v);
    }

    // Replace the conten nts of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that elemen/
        viewHolder.getTextView().setText(String.valueOf(mDataSet.get(position).getCalories()));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
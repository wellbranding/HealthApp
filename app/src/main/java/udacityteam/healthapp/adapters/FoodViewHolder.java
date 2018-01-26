package udacityteam.healthapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import udacityteam.healthapp.R;
import udacityteam.healthapp.activities.ItemClickListener;


/**
 * Created by kunda on 10/2/2017.
 */

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView food_name;
    public ImageView food_image;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public FoodViewHolder(View itemView) {
        super(itemView);

        food_name = (TextView)itemView.findViewById(R.id.food_name);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }
}

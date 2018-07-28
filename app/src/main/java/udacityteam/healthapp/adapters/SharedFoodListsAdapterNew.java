package udacityteam.healthapp.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import udacityteam.healthapp.Model.OneSharedFoodProductsListRetrofit;
import udacityteam.healthapp.R;
import udacityteam.healthapp.activities.SharedFoodListItemViewModel;
import udacityteam.healthapp.databinding.SharedFoodsListItemBinding;

/**
 * Created by vvost on 11/16/2017.
 */

public class SharedFoodListsAdapterNew extends RecyclerView.Adapter<SharedFoodListsAdapterNew.RepositoryViewHolder> {
    private static final String TAG = "CustomAdapter";

    private List<OneSharedFoodProductsListRetrofit> oneSharedFoodProductsListRetrofits;
    private String DatabaseStorage;
    private String foodselection;
    Context context;

    public SharedFoodListsAdapterNew() {
        this.oneSharedFoodProductsListRetrofits = Collections.emptyList();
    }
    public SharedFoodListsAdapterNew(String foodselection) {
        this.oneSharedFoodProductsListRetrofits = new ArrayList<OneSharedFoodProductsListRetrofit>();
        this.foodselection = foodselection;
    }
    public void setSelectedFoods(List<OneSharedFoodProductsListRetrofit> repositories) {
        this.oneSharedFoodProductsListRetrofits= repositories;
    }

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */

    @Override
    public RepositoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SharedFoodsListItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.shared_foods_list_item,
                parent,
                false);
        return new RepositoryViewHolder(binding, foodselection);
    }

    @Override
    public void onBindViewHolder(RepositoryViewHolder holder, int position) {
        holder.bindRepository(oneSharedFoodProductsListRetrofits.get(position));
    }


    @Override
    public int getItemCount() {
        if(oneSharedFoodProductsListRetrofits!=null)
        return oneSharedFoodProductsListRetrofits.size();
        else return 0;
    }

    public static class RepositoryViewHolder extends RecyclerView.ViewHolder {
        final SharedFoodsListItemBinding binding;
        String foodselection;

        public RepositoryViewHolder(SharedFoodsListItemBinding binding, String foodselection) {
            super(binding.layout);
            this.binding = binding;
            this.foodselection = foodselection;
        }

        void bindRepository(OneSharedFoodProductsListRetrofit repository) {
            if (binding.getViewModel() == null) {
                binding.setViewModel(new SharedFoodListItemViewModel(itemView.getContext(), repository, foodselection));
            } else {
                //  binding.getViewModel().
                //  binding.se

                binding.getViewModel().setSelectectedFoood(repository);
            }
        }
    }
}
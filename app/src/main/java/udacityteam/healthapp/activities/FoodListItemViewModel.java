package udacityteam.healthapp.activities;

import android.content.Context;
import android.databinding.BaseObservable;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import udacityteam.healthapp.Model.SelectedFoodretrofit;
/**
 * View model for each item in the repositories RecyclerView
 */
public class FoodListItemViewModel extends BaseObservable implements ViewModel {

    private SelectedFoodretrofit selectedFoodretrofit;
    private Context context;
    private String foodId;
    private String foodName;
    private String UserId= FirebaseAuth.getInstance().getCurrentUser().getUid();
    private String SendDate;
    private float Calories;
    private float Protein;
    private float Fat;
    private float Carbohydrates;

    public FoodListItemViewModel(Context context, SelectedFoodretrofit repository) {
        this.selectedFoodretrofit = repository;
        this.context = context;
    }

    public String getName() {
        return selectedFoodretrofit.getFoodid();
    }
//
//    public String getDescription() {
//        return selectedFoodretrofit.description;
//    }
//
//    public String getStars() {
//        return context.getString(R.string.text_stars, selectedFoodretrofit.stars);
//    }
//
//    public String getWatchers() {
//        return context.getString(R.string.text_watchers, selectedFoodretrofit.watchers);
//    }
//
//    public String getForks() {
//        return context.getString(R.string.text_forks, selectedFoodretrofit.forks);
//    }

//    public void onItemClick(View view) {
//        context.startActivity(RepositoryActivity.newIntent(context, selectedFoodretrofit));
//    }

    // Allows recycling ItemRepoViewModels within the recyclerview adapter
    public void setSelectectedFoood(SelectedFoodretrofit selectedFoodretrofit) {
        this.selectedFoodretrofit = selectedFoodretrofit;
//        notify();
    }
    public void onItemClick(View view) {
        context.startActivity(FoodNutritiensDisplayPrieview.newIntent(context, selectedFoodretrofit));
    }

    @Override
    public void destroy() {
        //In this case destroy doesn't need to do anything because there is not async calls
    }

}

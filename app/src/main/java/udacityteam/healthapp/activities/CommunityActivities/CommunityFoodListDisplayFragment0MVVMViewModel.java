package udacityteam.healthapp.activities.CommunityActivities;

import android.content.Context;
import android.databinding.BaseObservable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import udacityteam.healthapp.Model.OneSharedFoodProductsListRetrofit;
import udacityteam.healthapp.Model.SelectedFoodretrofit;
import udacityteam.healthapp.Model.SelectedFoodretrofitarray;
import udacityteam.healthapp.Model.SharedFoodProductsRetrofit;
import udacityteam.healthapp.Network.PHPService;
import udacityteam.healthapp.activities.FoodListViewModel;
import udacityteam.healthapp.activities.FoodNutritiensDisplayPrieview;
import udacityteam.healthapp.activities.ViewModel;
import udacityteam.healthapp.app.ApplicationController;
import udacityteam.healthapp.models.SharedFoodProducts;

/**
 * View model for each item in the repositories RecyclerView
 */
public class CommunityFoodListDisplayFragment0MVVMViewModel extends BaseObservable implements ViewModel {

    private static final String TAG ="trxt" ;
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
    private Subscription subscription;
    private  DataListener dataListener;
    List<OneSharedFoodProductsListRetrofit> selectedFoodretrofits;

    public CommunityFoodListDisplayFragment0MVVMViewModel(Context context, DataListener dataListener) {
     //  this.selectedFoodretrofit = repository;
        this.dataListener = dataListener;
        this.context = context;
    }

    public String getName() {
        return selectedFoodretrofit.getFoodid();
    }

    public void LoadFoodList( String SharedFoodListDatabase)
    {
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
        ApplicationController application = ApplicationController.get(context);
        PHPService phpService = application.getPHPService();
        subscription = phpService.getAllSharedDiets(((ApplicationController)context.getApplicationContext()).getId(),
                SharedFoodListDatabase)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(application.defaultSubscribeScheduler())
                .subscribe(new Subscriber<SharedFoodProductsRetrofit>() {
                    @Override
                    public void onCompleted() {
                        Log.d("aryra", String.valueOf(selectedFoodretrofits.size()));
                        dataListener.onRepositoriesChanged(selectedFoodretrofits);
//                        if(!selectedFoodretrofits.isEmpty())
//                        //    recyclerViewVisibility.set(View.VISIBLE);
//                        else
//                        {
//                            Toast.makeText(application, "Is empty", Toast.LENGTH_SHORT).show();
//                        }


                    }

                    @Override
                    public void onError(Throwable error) {
                        Log.e(TAG, "Error loading GitHub repos ", error);
//                        if (isHttp404(error)) {
//                            infoMessage.set("notfound");
//                        } else {
//                            infoMessage.set("good");
//                        }
//                        infoMessageVisibility.set(View.VISIBLE);
                    }

                    @Override
                    public void onNext(SharedFoodProductsRetrofit repositories) {

                        Log.i(TAG, "Repos loaded " + repositories);
                        Log.d("aryra1", String.valueOf(repositories));
                       // CommunityFoodListsDisplayFragment0.this.selectedFoodretrofits = repositories.getUsers().getClass();
                        CommunityFoodListDisplayFragment0MVVMViewModel.this.selectedFoodretrofits = repositories.getSelectedFoodretrofits();
                    }
                });
    }
    public void setSelectectedFoood(SelectedFoodretrofit selectedFoodretrofit) {
        this.selectedFoodretrofit = selectedFoodretrofit;
        notifyChange();
    }
    public void onItemClick(View view) {
        context.startActivity(FoodNutritiensDisplayPrieview.newIntent(context, selectedFoodretrofit));
    }

    @Override
    public void destroy() {

        //In this case destroy doesn't need to do anything because there is not async calls
    }
    public interface DataListener {
        void onRepositoriesChanged(List<OneSharedFoodProductsListRetrofit> repositories);
    }

}

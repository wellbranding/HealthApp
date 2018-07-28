package udacityteam.healthapp.activities.CommunityActivities;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.ObservableList;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Timestamp;
import java.util.List;

import io.apptik.widget.MultiSlider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import udacityteam.healthapp.Model.OneSharedFoodProductsListRetrofit;
import udacityteam.healthapp.Model.SelectedFoodretrofit;
import udacityteam.healthapp.Model.SelectedFoodretrofitarray;
import udacityteam.healthapp.Model.SharedFoodProductsRetrofit;
import udacityteam.healthapp.Model.UserProfile;
import udacityteam.healthapp.Network.PHPService;
import udacityteam.healthapp.PHP_Retrofit_API.APIService;
import udacityteam.healthapp.PHP_Retrofit_API.APIUrl;
import udacityteam.healthapp.activities.ApplicationClass;
import udacityteam.healthapp.activities.FoodListViewModel;
import udacityteam.healthapp.activities.FoodNutritiensDisplayPrieview;
import udacityteam.healthapp.activities.ViewModel;
import udacityteam.healthapp.app.ApplicationController;
import udacityteam.healthapp.models.SharedFoodProducts;

/**
 * View model for each item in the repositories RecyclerView
 */
public class CommunityFoodListDisplayFragment0MVVMViewModel extends AndroidViewModel implements ViewModel {

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

    public MutableLiveData<List<OneSharedFoodProductsListRetrofit>> mutableLiveData = new MutableLiveData<>();
    ApplicationController application = null;

    public CommunityFoodListDisplayFragment0MVVMViewModel(Application application)
    {
        super(application);
        this.application = ApplicationController.get(application.getBaseContext());

    }

    public String getName() {
        return selectedFoodretrofit.getFoodid();
    }

    public void LoadFoodList( String SharedFoodListDatabase)
    {

        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();

        PHPService phpService = application.getPHPService();
        subscription = phpService.getAllSharedDiets(application.getId(),
                SharedFoodListDatabase)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(application.defaultSubscribeScheduler())
                .subscribe(new Subscriber<SharedFoodProductsRetrofit>() {
                    @Override
                    public void onCompleted() {
                       for(int i=0;i< selectedFoodretrofits.size(); i++)
                       {
                           selectedFoodretrofits.get(i).setUserProfile(new UserProfile(
                                   selectedFoodretrofits.get(i).getMail(), selectedFoodretrofits.get(i).getDisplayname()
                           ));
                       }
                        Log.d("aryra", String.valueOf(selectedFoodretrofits.size()));
                        InitSelectedfoods(selectedFoodretrofits);
                     //   dataListener.onRepositoriesChanged(selectedFoodretrofits);
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
                        CommunityFoodListDisplayFragment0MVVMViewModel.this.selectedFoodretrofits =  repositories.getSelectedFoodretrofits();
                    }
                });
    }
    public void InitSelectedfoods(List<OneSharedFoodProductsListRetrofit> ones)
    {
        mutableLiveData.setValue(ones);
    }
    public MutableLiveData<List<OneSharedFoodProductsListRetrofit>> getMutableLiveData()
    {
        return mutableLiveData;
    }
    public void setSelectectedFoood(SelectedFoodretrofit selectedFoodretrofit) {
        this.selectedFoodretrofit = selectedFoodretrofit;
      notify();
    }
    public void onItemClick(View view) {
        application.getApplicationContext().startActivity(FoodNutritiensDisplayPrieview.newIntent(context, selectedFoodretrofit));
    }
    public void Hello()
    {
        Toast.makeText(application.getApplicationContext(), "heeeeeeeeee", Toast.LENGTH_LONG).show();
    }
    public  void GetFilteredSharedDiets(MultiSlider protein, MultiSlider calories,
                                        MultiSlider carbohydrates, MultiSlider fats
    , String SharedFoodListDatabase) //only if today
    {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        //Defining retrofit api service
        APIService service = retrofit.create(APIService.class);

        Call<SharedFoodProductsRetrofit> call = service.getAllFilteredSharedDiets(application.getId()
                , SharedFoodListDatabase, protein.getThumb(0).getValue(), protein.getThumb(1).getValue(),
                calories.getThumb(0).getValue(), calories.getThumb(1).getValue(),
                carbohydrates.getThumb(0).getValue(), carbohydrates.getThumb(1).getValue(),
                fats.getThumb(0).getValue(), fats.getThumb(1).getValue()
        );
        call.enqueue(new Callback<SharedFoodProductsRetrofit>() {
            @Override
            public void onResponse(Call<SharedFoodProductsRetrofit> call, Response<SharedFoodProductsRetrofit> response) {
                List<OneSharedFoodProductsListRetrofit> selectedFoodretrofits = response.body().
                        getSelectedFoodretrofits();
                mutableLiveData.setValue(selectedFoodretrofits);
                //notify();

                if (selectedFoodretrofits.size() != 0) {
                    Toast.makeText(application.getApplicationContext(), String.valueOf(selectedFoodretrofits.get(0).getCalories()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SharedFoodProductsRetrofit> call, Throwable t) {

            }

        });

    }

    @Override
    public void destroy() {

        //In this case destroy doesn't need to do anything because there is not async calls
    }
    public interface DataListener {
        void onRepositoriesChanged(List<OneSharedFoodProductsListRetrofit> repositories);
    }

}

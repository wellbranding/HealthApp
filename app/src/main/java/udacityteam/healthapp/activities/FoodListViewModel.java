package udacityteam.healthapp.activities;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.android.schedulers.AndroidSchedulers;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import udacityteam.healthapp.Model.Result;
import udacityteam.healthapp.Model.SelectedFoodretrofit;
import udacityteam.healthapp.Model.SelectedFoodretrofitarray;
import udacityteam.healthapp.Network.PHPService;
import udacityteam.healthapp.PHP_Retrofit_API.APIService;
import udacityteam.healthapp.PHP_Retrofit_API.APIUrl;
import udacityteam.healthapp.app.ApplicationController;

import static udacityteam.healthapp.activities.FoodSearchActivity.foodselection;

/**
 * View model for the MainActivity
 */
public class FoodListViewModel extends android.arch.lifecycle.ViewModel implements ViewModel {

    private static final String TAG = "MainViewModel";

    public ObservableInt infoMessageVisibility;
    public ObservableInt recyclerViewVisibility;
    public ObservableInt searchButtonVisibility;
    public ObservableField<String> infoMessage;
    public ObservableField<String> canshare;
    private DataListener dataListener;
    public  ObservableField<Boolean> isshared;
    public  ObservableField<String> caloriesCount;
    public  ObservableField<String> proteinCount;
    public  ObservableField<String> fatsCount;
    public  ObservableField<String> carbosCount;

    private Context context;
    private Subscription subscription;
    String foodselection;
    String sharedFoodListDatabase;;
    public  List<SelectedFoodretrofit> selectedFoodretrofits;
    public MutableLiveData<List<SelectedFoodretrofit>> mutableLiveData;
    //private List<Repository> repositories;
    private String editTextUsernameValue;
    public  Float verte;

    public FoodListViewModel(Context context, DataListener dataListener, String foodselection,
                             String SharedFoodListDatabase ) {
        this.context = context;
        this.dataListener = dataListener;
        isshared = new ObservableField<>();
        isshared.set(false);
        mutableLiveData  = new MutableLiveData<>();
        infoMessageVisibility = new ObservableInt(View.VISIBLE);
        recyclerViewVisibility = new ObservableInt(View.VISIBLE);
        searchButtonVisibility = new ObservableInt(View.GONE);
        caloriesCount = new ObservableField<>("0.0");
        carbosCount = new ObservableField<>("0.0");
        fatsCount = new ObservableField<>("0.0");
        proteinCount = new ObservableField<>("0.0");

       this.foodselection = foodselection;
       this.sharedFoodListDatabase = SharedFoodListDatabase;

        infoMessage = new ObservableField<>("message");
        canshare = new ObservableField<>("message");
    }

    public void LoadFoodList( String foodselection, String year, String month, String day)
    {

        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
        ApplicationController application = ApplicationController.get(context);
        Toast.makeText(context, ((ApplicationController)context.getApplicationContext()).getId().toString(), Toast.LENGTH_LONG).show();
        PHPService phpService = application.getPHPService();
        subscription = phpService.getselectedfoods(((ApplicationController)context.getApplicationContext()).getId(),
                foodselection, year, month, day)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(application.defaultSubscribeScheduler())
                .subscribe(this::handleResponse,this::handleError);
//
    }
    public void CalculateNutritionsDisplay(List<SelectedFoodretrofit> selectedFoodretrofits)
    {
        float calories = 0;
        float protein = 0;
        float carbos = 0;
        float fats = 0;
        for(int i = 0; i<selectedFoodretrofits.size(); i++)
        {
            calories+=selectedFoodretrofits.get(i).getCalories();
            protein+=selectedFoodretrofits.get(i).getProtein();
            carbos+=selectedFoodretrofits.get(i).getCarbohydrates();
            fats+=selectedFoodretrofits.get(i).getFat();


        }
        caloriesCount.set(String.valueOf(Math.round(calories*100.0)/100.0));
        proteinCount.set(String.valueOf(Math.round(protein*100.0)/100.0));
        carbosCount.set(String.valueOf(Math.round(carbos*100.0)/100.0));
        fatsCount.set(String.valueOf(Math.round(fats*100.0)/100.0));



    }
    private void handleResponse(SelectedFoodretrofitarray androidList) {
        Log.d("kietass", "jauu");

        selectedFoodretrofits = new ArrayList<>(androidList.getUsers());
        mutableLiveData.setValue(selectedFoodretrofits);
        dataListener.onRepositoriesChanged(mutableLiveData);
        CalculateNutritionsDisplay(selectedFoodretrofits);
    }
    private void handleError(Throwable error) {

     // Toast.makeText(this, "Error "+error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

        Log.d("erroraa", error.getMessage());
}

    public void IsShared(String foodselection)
    {
        Log.d("helaalo", "haa");
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

        Call<Result> call = service.getIsShared(
                ((ApplicationController)context.getApplicationContext()).getId(),
                timestamp, foodselection
        );
      //  Log.d("helomz", (((ApplicationClass)context.getApplicationContext()).getId()).toString() );
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                //  if(response.body().getMessage().equals("notfound"));
                if(response.body().getMessage().equals("Some error occurred")) {
                    canshare.set("UPDATE YOUR DIET");
                    isshared.set(false);
                }
                else {
                    Toast.makeText(context, "sssssssss", Toast.LENGTH_SHORT).show();
                    canshare.set("SHARE YOUR DIET");
                    isshared.set(true);

                }

            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                //  Log.d("resulttt", t.getMessage());
            }

        });
    }

    public void ShareFoodList() //only if today
    {
        Toast.makeText(context, "blaaaa", Toast.LENGTH_SHORT).show();
        Log.d("ggeee", "ggee");


            float protein = 0.0f, carbohydrates = 0.0f, fats = 0.0f, calories = 0.0f;
            for (SelectedFoodretrofit food : selectedFoodretrofits
                    ) {
                protein += food.getProtein();
                carbohydrates += food.getCarbohydrates();
                fats += food.getFat();
                calories += food.getCalories();
            }
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            ApplicationController application = ApplicationController.get(context);
            PHPService phpService = application.getPHPService();
            subscription = phpService.addSharedList(((ApplicationController) context.getApplicationContext()).getId(),
                    timestamp,
                    sharedFoodListDatabase, foodselection,

                    calories, protein, fats, carbohydrates
            )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(application.defaultSubscribeScheduler())
                    .subscribe(this::handleResponse, this::handleError);





    }

    private void handleResponse(Result result) {

        Log.d("pavyko", "pavyko");
        if (isshared.get()) {
            Toast.makeText(context, "Shared Successfuly", Toast.LENGTH_SHORT).show();
        }
        if (!isshared.get()) {
            Toast.makeText(context, "Updated Successfuly", Toast.LENGTH_SHORT).show();
        }
        Log.d("vertee", isshared.get().toString());
    }


    @Override
    public void destroy() {
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
        subscription = null;
        context = null;
        dataListener = null;
      //  dataListener = null;
    }



    private static boolean isHttp404(Throwable error) {
        return error instanceof HttpException && ((HttpException) error).code() == 404;
    }
    public interface DataListener {
        ///
        @SuppressWarnings("StatementWithEmptyBody")
        boolean onNavigationItemSelected(MenuItem item);

        void onRepositoriesChanged( MutableLiveData<List<SelectedFoodretrofit>> repositories);
    }


}

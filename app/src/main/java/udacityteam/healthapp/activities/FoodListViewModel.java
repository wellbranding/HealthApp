package udacityteam.healthapp.activities;

import android.content.Context;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
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
public class FoodListViewModel implements ViewModel {

    private static final String TAG = "MainViewModel";

    public ObservableInt infoMessageVisibility;
    public ObservableInt recyclerViewVisibility;
    public ObservableInt searchButtonVisibility;
    public ObservableField<String> sharedFoodListDatabase;
    public ObservableField<String> foodselection;
    public ObservableField<String> infoMessage;
    public ObservableField<String> canshare;
    private DataListener dataListener;

    private Context context;
    private Subscription subscription;
    public  List<SelectedFoodretrofit> selectedFoodretrofits;
    //private List<Repository> repositories;
    private String editTextUsernameValue;
    public  Float verte;

    public FoodListViewModel(Context context, DataListener dataListener, String foodselection,
                             String SharedFoodListDatabase ) {
        this.context = context;
        this.dataListener = dataListener;
        infoMessageVisibility = new ObservableInt(View.VISIBLE);
        recyclerViewVisibility = new ObservableInt(View.VISIBLE);
        searchButtonVisibility = new ObservableInt(View.GONE);
       this.foodselection.set(foodselection);

        infoMessage = new ObservableField<>("message");
        canshare = new ObservableField<>("message");
    }

    public void LoadFoodList( String foodselection, String year, String month, String day)
    {
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
        ApplicationController application = ApplicationController.get(context);
        PHPService phpService = application.getPHPService();
        subscription = phpService.getselectedfoods(((ApplicationController)context.getApplicationContext()).getId(),
                foodselection, year, month, day)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(application.defaultSubscribeScheduler())
                .subscribe(this::handleResponse,this::handleError);
//
    }
    private void handleResponse(SelectedFoodretrofitarray androidList) {
        Log.d("kietass", "jauu");

        selectedFoodretrofits = new ArrayList<>(androidList.getUsers());

        dataListener.onRepositoriesChanged(selectedFoodretrofits);
    }
    private void handleError(Throwable error) {

     // Toast.makeText(this, "Error "+error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        Log.d("error", "error");
}

    public void IsShared(String foodselection)
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

        Call<Result> call = service.getIsShared(
                ((ApplicationController)context.getApplicationContext()).getId(),
                timestamp, foodselection
        );
      //  Log.d("helomz", (((ApplicationClass)context.getApplicationContext()).getId()).toString() );
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                //  if(response.body().getMessage().equals("notfound"));
                if(response.body().getMessage().equals("Some error occurred"))
                    canshare.set("UPDATE YOUR DIET");
                else
                    canshare.set("SHARE YOUR DIET");

            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                //  Log.d("resulttt", t.getMessage());
            }

        });
    }
//    private void ShareFoodList() //only if today
//    {
//
//        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//        Gson gson = new GsonBuilder()
//                .setLenient()
//                .create();
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(APIUrl.BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();
//
//        //Defining retrofit api service
//        APIService service = retrofit.create(APIService.class);
//
//        Toast.makeText(this, Float.toString(protein), Toast.LENGTH_SHORT).show();
//        Call<Result> call = service.addSharedList(((ApplicationClass)getApplicationContext()).getId()
//                ,
//                timestamp,
//                SharedFoodListDatabase, foodselection,calories, protein, fats, carbohydrates
//        );
//        call.enqueue(new Callback<Result>() {
//            @Override
//            public void onResponse(Call<Result> call, Response<Result> response) {
//                Log.d("tavo", response.message());
//                Toast.makeText(FoodList.this, "Shared Successfully!", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(Call<Result> call, Throwable t) {
//                Toast.makeText(FoodList.this, "Some error occurred...", Toast.LENGTH_SHORT).show();
//            }
//
//        });
//
//    }

    public void ShareFoodList(String foodselection, String SharedFoodListDatabase) //only if today
    {
       float protein=0.0f, carbohydrates=0.0f, fats=0.0f, calories = 0.0f;
        for (SelectedFoodretrofit food:selectedFoodretrofits
                ) {
            protein +=food.getProtein();
            carbohydrates+=food.getCarbohydrates();
            fats +=food.getFat();
            calories += food.getCalories();
        }
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        ApplicationController application = ApplicationController.get(context);
        PHPService phpService = application.getPHPService();
        subscription = phpService.addSharedList(((ApplicationController)context.getApplicationContext()).getId(),
                timestamp,
                SharedFoodListDatabase, foodselection,calories, protein, fats, carbohydrates
        )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(application.defaultSubscribeScheduler())
                .subscribe(this::handleResponse,this::handleError);

    }

    private void handleResponse(Result result) {

        Log.d("pavyko", "pavyko");
    }


    @Override
    public void destroy() {
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
        subscription = null;
        context = null;
      //  dataListener = null;
    }



    private static boolean isHttp404(Throwable error) {
        return error instanceof HttpException && ((HttpException) error).code() == 404;
    }
    public interface DataListener {
        void onRepositoriesChanged(List<SelectedFoodretrofit> repositories);
    }


}

package udacityteam.healthapp.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.design.widget.FloatingActionButton;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import udacityteam.healthapp.R;
import udacityteam.healthapp.adapters.CustomAdapter;
import udacityteam.healthapp.app.ApplicationController;

import static udacityteam.healthapp.activities.FoodSearchActivity.foodselection;

/**
 * View model for the MainActivity
 */
public class MainActivityViewModel implements ViewModel {

    private static final String TAG = "MainViewModel";

    public ObservableInt infoMessageVisibility;
    public ObservableInt recyclerViewVisibility;
    public ObservableInt lunchvisibility;
    public ObservableInt dinnervisibility;
    public ObservableInt snacksvisibility;
    public ObservableInt drinksvisibility;
    public ObservableInt breakfastvisibility;
    public ObservableInt searchButtonVisibility;
    public String sharedFoodListDatabase;
    public String foodselection;
    public ObservableField<String> infoMessage;
    public ObservableField<String> canshare;
    public ObservableField<MaterialCalendarView> calendar;
    public  ObservableField<Boolean> initfloating;
    public ObservableField<FloatingActionButton> floatingActionButtonObservableField;
    private DataListener dataListener;

    private Context context;
    private Subscription subscription;
    public  List<SelectedFoodretrofit> selectedFoodretrofits;

    SimpleDateFormat format;
    //private List<Repository> repositories;
    private String editTextUsernameValue;
    public  Float verte;

    public MainActivityViewModel(Context context, DataListener dataListener ) {
        this.context = context;
        this.dataListener = dataListener;
        calendar = new ObservableField<>();
        format =  new SimpleDateFormat("yyyy-MM-dd");
        floatingActionButtonObservableField = new ObservableField<>();
        initfloating = new ObservableField<>(false);
        infoMessageVisibility = new ObservableInt(View.VISIBLE);
        recyclerViewVisibility = new ObservableInt(View.VISIBLE);
        searchButtonVisibility = new ObservableInt(View.GONE);
        lunchvisibility = new ObservableInt(View.INVISIBLE);
        dinnervisibility = new ObservableInt(View.INVISIBLE);
        snacksvisibility = new ObservableInt(View.INVISIBLE);
        drinksvisibility = new ObservableInt(View.INVISIBLE);

        breakfastvisibility = new ObservableInt(View.INVISIBLE);


        infoMessage = new ObservableField<>("message");
        canshare = new ObservableField<>("message");
    }
    public void SetCalendar(MaterialCalendarView calendarView)
    {
        this.calendar.set(calendarView);
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
    public void clickbtndinner(View view)
    {
        Log.d("works", "works");
        Toast.makeText(context, "hello", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, FoodList.class);
        intent.putExtra("foodselection", "Dinner");
        intent.putExtra("SharedFoodListDatabase", "SharedDinners");
        Log.d("calendarrr", format.format(this.calendar.get().getSelectedDate().getDate()));
        Toast.makeText(context, format.format(this.calendar.get().getSelectedDate().getDate()), Toast.LENGTH_SHORT).show();
        intent.putExtra("requestdate", format.format(this.calendar.get().getSelectedDate().getDate()));
        context.startActivity(intent);
    }
    public void initifloatingfield(FloatingActionButton button)
    {
        floatingActionButtonObservableField.set(button);
    }
    public void initfloatingbutton(View view)
    {
        Log.d("error", "error");
        if (initfloating.get()) {
            closeSubMenusFab();
        } else {
            openSubMenusFab();
        }
    }
    private void openSubMenusFab(){
        Log.d("hello", "iejo");
        // layoutFabSave.setVisibility(View.VISIBLE);

        snacksvisibility.set(View.VISIBLE);
        drinksvisibility.set(View.VISIBLE);
        breakfastvisibility.set(View.VISIBLE);
        dinnervisibility.set(View.VISIBLE);
        lunchvisibility.set(View.VISIBLE);
        initfloating.set(true);
        floatingActionButtonObservableField.get().setImageResource(R.drawable.ic_close_black_24dp);
    }



    private void closeSubMenusFab() {
        Log.d("hello", "iseejo");
        snacksvisibility.set(View.INVISIBLE);
        drinksvisibility.set(View.INVISIBLE);
        breakfastvisibility.set(View.INVISIBLE);
        dinnervisibility.set(View.INVISIBLE);
        lunchvisibility.set(View.INVISIBLE);
        floatingActionButtonObservableField.get().setImageResource(R.drawable.ic_settings_black_24dp);
        initfloating.set(false);
    }
    public void searchdrinks(View view)
    {
        Toast.makeText(context, "Drunkjs", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, FoodSearchActivity.class);
        intent.putExtra("SharedFoodListDatabase", "SharedDrinks");
        intent.putExtra("foodselection", "Drinks");
        context.startActivity(intent);
    }
    public void searchbreakfasts(View view)
    {
        Toast.makeText(context, "Drunkjs", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, FoodSearchActivity.class);
        intent.putExtra("SharedFoodListDatabase", "SharedBreakfasts");
        intent.putExtra("foodselection", "Breakfast");
        context.startActivity(intent);
    }
    public void searchlunches(View view)
    {
        Toast.makeText(context, "Drunkjs", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, FoodSearchActivity.class);
        intent.putExtra("SharedFoodListDatabase", "SharedLunches");
        intent.putExtra("foodselection", "Lunch");
        context.startActivity(intent);
    }
    public void searchdinners(View view)
    {
        Toast.makeText(context, "Drunkjs", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, FoodSearchActivity.class);
        intent.putExtra("SharedFoodListDatabase", "SharedDinners");
        intent.putExtra("foodselection", "Dinner");
        context.startActivity(intent);
    }
    public void searchsnacks(View view)
    {
        Toast.makeText(context, "Drunkjs", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, FoodSearchActivity.class);
        intent.putExtra("SharedFoodListDatabase", "SharedSnacks");
        intent.putExtra("foodselection", "Snacks");
        context.startActivity(intent);
    }

    public void InitUser()
    {
        ApplicationController application = ApplicationController.get(context);
        PHPService phpService = application.getPHPService();
        subscription = phpService.getCurrentUser(
                FirebaseAuth.getInstance().getCurrentUser().getUid()
        )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(application.defaultSubscribeScheduler())
                .subscribe(this::handleResponse,this::handleError);

    }
    public void GetCalendarTime()
    {
       String aki = String.valueOf(calendar.get().getSelectedDate().getDate());
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.calendar.get().getSelectedDate().getDate());
        Log.d("error", format.format(calendar.getTime()));
  //      Log.d("calendarrr", format.format(this.calendar.get().getSelectedDate().getDate()));
//        calendar.setTime(binding.appBarMain.contentMain.calendarView.getSelectedDate().getDate());
//        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//        System.out.println();
//        Log.d("ajaaz", format.format(calendar.getTime()));
////        dinnerbtn.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                Intent intent = new Intent(MainActivity.this, FoodList.class);
////                intent.putExtra("foodselection", "Dinner");
////                intent.putExtra("SharedFoodListDatabase", "SharedDinners");
////                intent.putExtra("requestdate", format.format(calendar.getTime()));
////                startActivity(intent);
////            }
////        });
//        lunchbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, FoodList.class);
//                intent.putExtra("foodselection", "Lunch");
//                intent.putExtra("SharedFoodListDatabase", "SharedLunches");
//                intent.putExtra("requestdate", format.format(calendar.getTime()));
//
//                startActivity(intent);
//            }
//        });
//        breakfastbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, FoodList.class);
//                intent.putExtra("foodselection", "Breakfast");
//                intent.putExtra("SharedFoodListDatabase", "SharedBreakfasts");
//                intent.putExtra("requestdate", format.format(calendar.getTime()));
//                startActivity(intent);
//            }
//        });
//        snacksbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, FoodList.class);
//                intent.putExtra("foodselection", "Snacks");
//                intent.putExtra("SharedFoodListDatabase", "SharedSnacks");
//                intent.putExtra("requestdate", format.format(calendar.getTime()));
//                startActivity(intent);
//            }
//        });
//        drinksbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, FoodList.class);
//                intent.putExtra("foodselection", "Drinks");
//                intent.putExtra("UserId", currentUser.getId());
//                intent.putExtra("SharedFoodListDatabase", "SharedDrinks");
//                intent.putExtra("requestdate", format.format(calendar.getTime()));
//                startActivity(intent);
//            }
//        });
//        dailybtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(MainActivity.this, "Currently not Available", Toast.LENGTH_SHORT).show();
//            }
//        });


    }



    public void ShareFoodList() //only if today
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
                sharedFoodListDatabase, foodselection,calories, protein, fats, carbohydrates
        )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(application.defaultSubscribeScheduler())
                .subscribe(this::handleResponse,this::handleError);

    }

    private void handleResponse(Result result) {
        ((ApplicationController)context.getApplicationContext()).setId(result.getUser().getId());

        Log.d("works", "works");
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

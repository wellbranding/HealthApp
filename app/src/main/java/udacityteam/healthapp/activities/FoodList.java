package udacityteam.healthapp.activities;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import udacityteam.healthapp.PHP_Retrofit_API.APIService;
import udacityteam.healthapp.PHP_Retrofit_API.APIUrl;
import udacityteam.healthapp.Model.Result;
import udacityteam.healthapp.Model.SelectedFoodretrofit;
import udacityteam.healthapp.Model.SelectedFoodretrofitarray;
import udacityteam.healthapp.R;
import udacityteam.healthapp.activities.CommunityActivities.CommunityList;
import udacityteam.healthapp.adapters.FoodListRetrofitAdapter;
import udacityteam.healthapp.adapters.FoodListRetrofitAdapterNew;
import udacityteam.healthapp.adapters.FoodViewHolder;
import udacityteam.healthapp.app.ApplicationController;
import udacityteam.healthapp.databinding.ActivityFoodListBinding;
import udacityteam.healthapp.models.SelectedFood;
import okhttp3.Interceptor;

public class FoodList extends AppCompatActivity implements Currentuser, FoodListViewModel.DataListener, NavigationView.OnNavigationItemSelectedListener  {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference foodList;
    DatabaseReference usersdatabase;
    String foodselection;

    String catergoryId = "ff";
    FirebaseRecyclerAdapter<SelectedFood, FoodViewHolder> adapter;
    String stringdate;
    TextView message;
    String requestedString;
    String SharedFoodListDatabase;
    Button share;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    CollectionReference userstorage;
   FirebaseFirestore storage;
    String newstring=null;
    TextView caloriescounter, proteincounter, fatcounter, carbohycounter;
    float carbohydrates = 0, protein=0, fats =0, calories =0;
    private static final String CACHE_CONTROL = "Cache-Control";
    Integer UserId=0;

    int cacheSize = 10 * 1024 * 1024; // 10 MiB
    ArrayList<SelectedFoodretrofit> nauji;
    ArrayList<SelectedFood> selectedFoods;
    private FoodListViewModel foodListViewModel;
    private ActivityFoodListBinding activityFoodListBinding;
    List<SelectedFoodretrofit> receivedSelectedFoods;
    FoodListRetrofitAdapterNew customAdapterFoodListPrievew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        foodselection = (String) b.get("foodselection");
        requestedString = (String) b.get("requestdate");
        SharedFoodListDatabase = (String) b.get("SharedFoodListDatabase");
        foodListViewModel = new FoodListViewModel(this, this, foodselection, SharedFoodListDatabase);
        activityFoodListBinding = DataBindingUtil.setContentView(this, R.layout.activity_food_list);
        caloriescounter = findViewById(R.id.caloriescount);
        proteincounter = findViewById(R.id.proteincount);
        carbohycounter = findViewById(R.id.carbohncount);
        fatcounter = findViewById(R.id.fatcount);

        message = findViewById(R.id.message);
        share = findViewById(R.id.share);

        activityFoodListBinding.setViewModel(foodListViewModel);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("MainActivity");
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setTitle(foodselection);
        share.setEnabled(true);

        if (requestedString != null)
            stringdate = requestedString;
        else {
            Date date = new Date();
            Date newDate = new Date(date.getTime());
            SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
            stringdate = dt.format(newDate);
        }
        Log.d("reqss", stringdate);

        selectedFoods = new ArrayList<>();
        foodListViewModel.IsShared(foodselection);

        String year = requestedString.substring(0, 4);
        String month = requestedString.substring(5, 7);
        String day = requestedString.substring(8, 10);
        setupRecyclerView();
        foodListViewModel.LoadFoodList(foodselection, year, month, day);



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        foodListViewModel.destroy();
    }



//    private void RetrofitList()
//    {
//        //rion Cacheprovided
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor( provideOfflineCacheInterceptor() )
//                .addNetworkInterceptor( provideCacheInterceptor() )
//                .cache( provideCache() )
//                .build();
//        //endregion
//        String year = requestedString.substring(0, 4);
//        String month = requestedString.substring(5, 7);
//        String day = requestedString.substring(8, 10);
//       Log.d("rezas",year);
//
//        Gson gson = new GsonBuilder()
//                .setLenient()
//                .create();
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(APIUrl.BASE_URL)
//                .client(okHttpClient)
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();
//
//        //Defining retrofit api service
//        APIService service = retrofit.create(APIService.class);
//
//        Toast.makeText(this,   ((ApplicationController)getApplicationContext()).getId().toString(), Toast.LENGTH_SHORT).show();
//        Call<SelectedFoodretrofitarray> call = service.getselectedfoods(
//                ((ApplicationController)getApplicationContext()).getId(),
//                foodselection, year, month, day
//        );
//        call.enqueue(new Callback<SelectedFoodretrofitarray>() {
//            @Override
//            public void onResponse(Call<SelectedFoodretrofitarray> call, Response<SelectedFoodretrofitarray> response) {
//                nauji = response.body().getUsers();
//                for ( SelectedFoodretrofit c:
//                        nauji) {
//                    carbohydrates += c.getCarbohydrates();
//                    protein+=c.getProtein();
//                    fats+=c.getFat();
//                    calories+=c.getCalories();
//                }
//                carbohycounter.setText(Float.toString(carbohydrates));
//                proteincounter.setText(Float.toString(protein));
//                fatcounter.setText(Float.toString(fats));
//                caloriescounter.setText(Float.toString(calories));
//                FoodListRetrofitAdapter customAdapterFoodListPrievew= new
//                        FoodListRetrofitAdapter(nauji);
//                recyclerView.setAdapter(customAdapterFoodListPrievew);
//                initSharedButton();
//            }
//            @Override
//            public void onFailure(Call<SelectedFoodretrofitarray> call, Throwable t) {
//                Toast.makeText(FoodList.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
//                Log.d("bigerror", t.getMessage());
//
//            }
//        });
//
//    }
    public static Interceptor provideCacheInterceptor ()
    {
        return new Interceptor()
        {
            @Override
            public okhttp3.Response intercept (Chain chain) throws IOException
            {
                okhttp3.Response response = chain.proceed( chain.request() );

                // re-write response header to force use of cache
                CacheControl cacheControl = new CacheControl.Builder()
                        .maxAge( 1, TimeUnit.SECONDS )
                        .build();

                return response.newBuilder()
                        .header( CACHE_CONTROL, cacheControl.toString() )
                        .build();
            }
        };
    }

    private Cache provideCache ()
    {
        Cache cache = null;
        try
        {
            cache = new Cache( new File( getApplicationContext().getCacheDir(), "http-cache" ),
                    10 * 1024 * 1024 ); // 10 MB
        }
        catch (Exception e)
        {
            Log.e("ahahaha", "Could not create Cache!");
        }
        return cache;
    }
    public Interceptor provideOfflineCacheInterceptor () {
        return new Interceptor()
        {
            @Override
            public okhttp3.Response intercept (Chain chain) throws IOException
            {
                Request request = chain.request();

                if (!isNetworkAvailable())
                {
                    CacheControl cacheControl = new CacheControl.Builder()
                            .maxStale( 7, TimeUnit.DAYS )
                            .build();

                    request = request.newBuilder()
                            .cacheControl( cacheControl )
                            .build();
                }

                return chain.proceed( request );
            }
        };
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public void IsShared()
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
                ((ApplicationClass)getApplicationContext()).getId(),
                timestamp, foodselection
        );
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
              //  if(response.body().getMessage().equals("notfound"));
              if(response.body().getMessage().equals("Some error occurred"))
                  share.setText("UPDATE YOUR DIET");
              else
                  share.setText("SHARE YOUR DIET");
              // can't share, because you can share only one time
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
              //  Log.d("resulttt", t.getMessage());
            }

        });
    }


    ///
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_breakfasts) {
            Intent intent = new Intent(this, CommunityList.class);
            Bundle extras = intent.getExtras();
            intent.putExtra("titlename", "Community Breakfasts");
            intent.putExtra("SharedFoodListDatabase", "SharedBreakfasts");
            intent.putExtra("foodselection", "Breakfast");
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.nav_dinners) {
            Intent intent = new Intent(this, CommunityList.class);
            Bundle extras = intent.getExtras();
            intent.putExtra("titlename", "Community Dinners");
            intent.putExtra("SharedFoodListDatabase", "SharedDinners");
            intent.putExtra("foodselection", "Dinner");
            startActivity(intent);


        } else if (id == R.id.nav_lunches) {
            Intent
                    intent = new Intent(this, CommunityList.class);
            Bundle extras = intent.getExtras();
            intent.putExtra("titlename", "Community Lunches");
            intent.putExtra("SharedFoodListDatabase", "SharedLunches");
            intent.putExtra("foodselection", "Lunch");
            startActivity(intent);

        } else if (id == R.id.nav_community_daily_diets) {
            Intent intent = new Intent(this, CommunityList.class);
            Bundle extras = intent.getExtras();
            intent.putExtra("titlename", "Community Daily Diet Plan");
            intent.putExtra("SharedFoodListDatabase", "SharedDailyDiets");
            Toast.makeText(this, "Currently Not Available", Toast.LENGTH_SHORT).show();
            //startActivity(intent);

        } else if (id == R.id.nav_snacks) {
            Intent intent = new Intent(this, CommunityList.class);
            Bundle extras = intent.getExtras();
            intent.putExtra("titlename", "Snacks");
            intent.putExtra("SharedFoodListDatabase", "SharedSnacks");
            intent.putExtra("foodselection", "Snacks");
            startActivity(intent);
            //test

        } else if (id == R.id.nav_drinks_cocktails) {
            Intent intent = new Intent(this, CommunityList.class);
            Bundle extras = intent.getExtras();
            intent.putExtra("titlename", "Drinks/Coctails");
            intent.putExtra("SharedFoodListDatabase", "SharedDrinks");
            intent.putExtra("foodselection", "Drinks");
            startActivity(intent);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void ShareFoodList() //only if today
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

        Toast.makeText(this, Float.toString(protein), Toast.LENGTH_SHORT).show();
        Call<Result> call = service.addSharedList(((ApplicationClass)getApplicationContext()).getId()
                ,
                timestamp,
                SharedFoodListDatabase, foodselection,calories, protein, fats, carbohydrates
        );
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                Log.d("tavo", response.message());
                Toast.makeText(FoodList.this, "Shared Successfully!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(FoodList.this, "Some error occurred...", Toast.LENGTH_SHORT).show();
            }

        });

            }
    private void setupRecyclerView() {

      customAdapterFoodListPrievew= new
                FoodListRetrofitAdapterNew();
        activityFoodListBinding.recyclerFood.setLayoutManager(new LinearLayoutManager(this));
        activityFoodListBinding.recyclerFood.setHasFixedSize(true);
        activityFoodListBinding.recyclerFood.setAdapter(customAdapterFoodListPrievew);
      //  Log.d("tikrinu", String.valueOf(foodListViewModel.selectedFoodretrofits.size()));

    }

    @Override
    public void onRepositoriesChanged(MutableLiveData<List<SelectedFoodretrofit>> repositories) {
        receivedSelectedFoods = repositories.getValue();
        Log.d("ijunge", String.valueOf(repositories.getValue().size()));
        customAdapterFoodListPrievew.setSelectedFoodretrofits(repositories.getValue());
        customAdapterFoodListPrievew.notifyDataSetChanged();


    }
}


package udacityteam.healthapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import udacityteam.healthapp.PHP_Retrofit_Models.Result;
import udacityteam.healthapp.PHP_Retrofit_Models.SelectedFoodretrofit;
import udacityteam.healthapp.PHP_Retrofit_Models.SelectedFoodretrofitarray;
import udacityteam.healthapp.R;
import udacityteam.healthapp.adapters.FoodListRetrofitAdapter;
import udacityteam.healthapp.adapters.FoodViewHolder;
import udacityteam.healthapp.models.SelectedFood;
import okhttp3.Interceptor;

public class FoodList extends AppCompatActivity {

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
    float calories = 0;
    float protein = 0;
    float carbohydrates = 0;
    private static final String CACHE_CONTROL = "Cache-Control";
    float fat = 0;
    int cacheSize = 10 * 1024 * 1024; // 10 MiB

    ArrayList<SelectedFood> selectedFoods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);
        final SharedPreferences pref = getApplicationContext().getSharedPreferences("SharedToday", MODE_PRIVATE);
        Boolean didsharedtoday = pref.getBoolean("didshared", false);
        Log.d("did", didsharedtoday.toString());
        editor = pref.edit();
        caloriescounter = findViewById(R.id.caloriescount);
        proteincounter = findViewById(R.id.proteincount);
        carbohycounter = findViewById(R.id.carbohncount);
        fatcounter = findViewById(R.id.fatcount);
        Intent iin = getIntent();
        message = findViewById(R.id.message);
        share = findViewById(R.id.share);
        Bundle b = iin.getExtras();
        foodselection = (String) b.get("foodselection");
        requestedString = (String) b.get("requestdate");
        SharedFoodListDatabase = (String) b.get("SharedFoodListDatabase");
        getSupportActionBar().setTitle(foodselection);

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
        recyclerView = (RecyclerView) findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
      IsShared();
        RetrofitList();
   //     RetrofitCatche();

    }



    private void RetrofitList()
    {
        //region Cacheprovided
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor( provideOfflineCacheInterceptor() )
                .addNetworkInterceptor( provideCacheInterceptor() )
                .cache( provideCache() )
                .build();
        //endregion
        String year = requestedString.substring(0, 4);
        String month = requestedString.substring(5, 7);
        String day = requestedString.substring(8, 10);
       Log.d("rezas",year);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        //Defining retrofit api service
        APIService service = retrofit.create(APIService.class);

        Call<SelectedFoodretrofitarray> call = service.getselectedfoods(
                FirebaseAuth.getInstance().getCurrentUser().getUid(),
                foodselection, year, month, day
        );
        call.enqueue(new Callback<SelectedFoodretrofitarray>() {
            @Override
            public void onResponse(Call<SelectedFoodretrofitarray> call, Response<SelectedFoodretrofitarray> response) {
                ArrayList<SelectedFoodretrofit> nauji = response.body().getUsers();

                FoodListRetrofitAdapter customAdapterFoodListPrievew= new
                        FoodListRetrofitAdapter(nauji);
                recyclerView.setAdapter(customAdapterFoodListPrievew);
                initSharedButton();
            }
            @Override
            public void onFailure(Call<SelectedFoodretrofitarray> call, Throwable t) {
                Toast.makeText(FoodList.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
                Log.d("bigerror", t.getMessage());

            }
        });

    }
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
                FirebaseAuth.getInstance().getCurrentUser().getUid(),
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

    private void initSharedButton(){

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String month = requestedString.substring(5, 7);
        String day = requestedString.substring(8, 10);
        String timestampmonth = timestamp.toString().substring(5,7);
        String timestampday = timestamp.toString().substring(8,10);



        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(month.equals(timestampmonth)&&day.equals(timestampday))
               ShareFoodList();
                else
                {
                    Toast.makeText(FoodList.this, "Can't share earlier diet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    ///

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

        Call<Result> call = service.addSharedList(
                FirebaseAuth.getInstance().getCurrentUser().getUid(),
                timestamp,
                SharedFoodListDatabase, foodselection
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

    }


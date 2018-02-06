package udacityteam.healthapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import udacityteam.healthapp.PHP_Retrofit_API.APIService;
import udacityteam.healthapp.PHP_Retrofit_API.APIUrl;
import udacityteam.healthapp.Model.SelectedFoodretrofit;
import udacityteam.healthapp.Model.SelectedFoodretrofitarray;
import udacityteam.healthapp.R;
import udacityteam.healthapp.adapters.FoodListRetrofitAdapter;
import udacityteam.healthapp.adapters.FoodViewHolder;
import udacityteam.healthapp.models.SelectedFood;
import udacityteam.healthapp.models.SharedFoodProducts;

public class FoodListPrieview extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference foodList;
    DatabaseReference usersdatabase;
    String foodselection, key;

   ArrayList<SelectedFood> sharedprofucts = new ArrayList<>();

    String catergoryId = "ff";
    FirebaseRecyclerAdapter<SelectedFood, FoodViewHolder> adapter;
    String stringdate;
    Button share;
    FirebaseAuth mAuth;
    CollectionReference userstorage;
    FirebaseFirestore storage;
    //float calories, carbohydrates, protein, fats;
    ArrayList<SelectedFood> selectedFoods;
    TextView caloriescounter, proteincounter, fatcounter, carbohycounter;
    SharedFoodProducts receivedsharedfoodproducts;
   Integer getParentSharedFoodsId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);
        mAuth = FirebaseAuth.getInstance();

        caloriescounter = findViewById(R.id.caloriescount);
        proteincounter = findViewById(R.id.proteincount);
        carbohycounter = findViewById(R.id.carbohncount);
        fatcounter = findViewById(R.id.fatcount);

        Intent iin = getIntent();

        Bundle b = iin.getExtras();
     foodselection = (String) b.get("foodselection");
     Log.d("foodselection", foodselection);
        getParentSharedFoodsId = (Integer) b.get("getParentSharedFoodsId");
        //Log.d("aazzz", getParentSharedFoodsId.toString());
//        ///sjjs
//    //    receivedsharedfoodproducts = (SharedFoodProducts)  b.getParcelable("sharedfoofproducts");
//   //  sharedprofucts = receivedsharedfoodproducts.getSelectedFoods();
//     selectedFoods = b.getParcelableArrayList("user_list");
//     //Log.d("parodyk", String.valueOf(selectedFoods.size()));
//        key = (String) b.get("key");
//        calories = (float) b.get("calories");
//        carbohydrates = (float) b.get("carbohydrates");
//        protein = (float) b.get("protein");
//        fats = (float) b.get("fats");

            Date date = new Date();
            Date newDate = new Date(date.getTime());
            SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
            stringdate = dt.format(newDate);

//        caloriescounter.setText(String.valueOf(calories));
//        proteincounter.setText(String.valueOf(protein));
//        carbohycounter.setText(String.valueOf(carbohydrates));
//        fatcounter.setText(String.valueOf(fats));
        database = FirebaseDatabase.getInstance();
     //getSupportActionBar().setTitle(foodselection);
///        foodList = database.getReference("MainFeed").child(foodselection).child("SharedDiets").child(key).child("SelectedFoods");
//        Log.d("kej", key);
      //  foodList.orderByChild("date").equalTo(stringdate);

        recyclerView = findViewById(R.id.recycler_food);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        GetFoodList();

    }
    private void GetFoodList()
    {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        APIService service = retrofit.create(APIService.class);

        Call<SelectedFoodretrofitarray> call = service.getselectedfoodsPrieview(

                getParentSharedFoodsId, foodselection
        );
        call.enqueue(new Callback<SelectedFoodretrofitarray>() {
            @Override
            public void onResponse(Call<SelectedFoodretrofitarray> call, Response<SelectedFoodretrofitarray> response) {
                ArrayList<SelectedFoodretrofit> nauji = response.body().getUsers();
                //       Log.d("ahh", String.valueOf(nauji.get(1).getFoodid()));
                //sukurti nauja masyva ir pernesti viskaa

                FoodListRetrofitAdapter customAdapterFoodListPrievew= new
                        FoodListRetrofitAdapter(nauji);
                recyclerView.setAdapter(customAdapterFoodListPrievew);
            }

            @Override
            public void onFailure(Call<SelectedFoodretrofitarray> call, Throwable t) {

            }
        });
    }



}

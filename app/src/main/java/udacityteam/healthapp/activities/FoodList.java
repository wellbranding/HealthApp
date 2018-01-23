package udacityteam.healthapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import udacityteam.healthapp.PHP_Retrofit.APIService;
import udacityteam.healthapp.PHP_Retrofit.APIUrl;
import udacityteam.healthapp.PHP_Retrofit.Result;
import udacityteam.healthapp.PHP_Retrofit.SelectedFoodretrofit;
import udacityteam.healthapp.PHP_Retrofit.SelectedFoodretrofitarray;
import udacityteam.healthapp.R;
import udacityteam.healthapp.adapters.CustomAdapterFoodListPrievew;
import udacityteam.healthapp.adapters.CustomAdapterFoodListPrievewcloud;
import udacityteam.healthapp.adapters.CustomAdapterFoodListPrievewretro;
import udacityteam.healthapp.models.SelectedFood;
import udacityteam.healthapp.models.SelectedFoodmodel;
import udacityteam.healthapp.models.SharedFoodProducts;
import udacityteam.healthapp.models.User;

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
    float fat = 0;

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
      //  Log.d("gerassss",  SharedFoodListDatabase);

        if (requestedString != null)
            stringdate = requestedString;
        else {
            Date date = new Date();
            Date newDate = new Date(date.getTime());
            SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
            stringdate = dt.format(newDate);
        }
        Log.d("reqss", stringdate);
        database = FirebaseDatabase.getInstance();
         Ishared();
//        database.getReference("User").child(FirebaseAuth.getInstance().
//                getCurrentUser().getUid()).child(foodselection+"isshared").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Boolean value = (Boolean) dataSnapshot.getValue();
//                if(value==true) {
//                    newstring = "Modify your shared diet";
//                    Log.d("pagaliau", newstring);
//                    loadListFood();
//
//                }
//
//                else {
//                    newstring = "shared.";
//                    loadListFood();
//                }
//
//                // do your stuff here with value
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//
//        });
     //   Log.d("good", newstring);



        selectedFoods = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        storage = FirebaseFirestore.getInstance();
        userstorage= storage.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection(foodselection).document(stringdate).collection("TodaysFoods");
        userstorage
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d("geras", "good");
                            for (DocumentSnapshot document : task.getResult()) {

                                SelectedFood food = document.toObject(SelectedFood.class);
                               Float onecalories = food.getCalories();
                                Float oneprotein = food.getProtein();
                                Float onecarbohydrates = food.getCarbohydrates();
                                Float onefat = food.getFat();
                                protein = protein + oneprotein;
                                 calories = calories + onecalories;
                                 carbohydrates = carbohydrates + onecarbohydrates;
                                 fat = fat + onefat;

                             // SelectedFood food = new SelectedFood(UserId, UserId, UserId, UserId);

                                selectedFoods.add(food);
                            }
                            caloriescounter.setText(String.valueOf(calories));
                          //     Log.d("geras",  String.valueOf(protein));
                          proteincounter.setText(String.valueOf(protein));
                       carbohycounter.setText(String.valueOf(carbohydrates));
                         fatcounter.setText(String.valueOf(fat));
                            CustomAdapterFoodListPrievew customAdapterFoodListPrievew = new CustomAdapterFoodListPrievew(selectedFoods);
                          //  recyclerView.setAdapter(customAdapterFoodListPrievew);
                            loadListFood();
                        } else {
                            Log.d("geras", "Error getting documents: ", task.getException());
                        }
                    }

                });

        Log.d("geras",  String.valueOf(selectedFoods.size()));

        getSupportActionBar().setTitle(foodselection);
        foodList = database.getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(foodselection);
        foodList.orderByChild("date").equalTo(stringdate);
        RetrofitList();



    }

    private void RetrofitList()
    {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        //Defining retrofit api service
        APIService service = retrofit.create(APIService.class);

        Call<SelectedFoodretrofitarray> call = service.getselectedfoods(
                FirebaseAuth.getInstance().getCurrentUser().getUid(),
                foodselection
        );
        call.enqueue(new Callback<SelectedFoodretrofitarray>() {
            @Override
            public void onResponse(Call<SelectedFoodretrofitarray> call, Response<SelectedFoodretrofitarray> response) {
                ArrayList<SelectedFoodretrofit> nauji = response.body().getUsers();
        //       Log.d("ahh", String.valueOf(nauji.get(1).getFoodid()));
               //sukurti nauja masyva ir pernesti viskaa

                CustomAdapterFoodListPrievewretro customAdapterFoodListPrievew= new
                        CustomAdapterFoodListPrievewretro(nauji);
                recyclerView.setAdapter(customAdapterFoodListPrievew);
            }

            @Override
            public void onFailure(Call<SelectedFoodretrofitarray> call, Throwable t) {

            }


        });

    }
    public void Ishared()
    {
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
                stringdate, foodselection
        );
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
              //  if(response.body().getMessage().equals("notfound"));
              if(response.body().getMessage().equals("Some error occurred"))
                  share.setText("Update");


            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
              //  Log.d("resulttt", t.getMessage());
            }

        });
    }

    private void loadListFood() {
//        if (stringdate != null) {
//            adapter = new FirebaseRecyclerAdapter<SelectedFood, FoodViewHolder>(SelectedFood.class,
//                    R.layout.food_item,
//                    FoodViewHolder.class,
//                    foodList.orderByChild("date").equalTo(stringdate)) {
//
//                @Override
//                protected void populateViewHolder(FoodViewHolder viewHolder, final SelectedFood model, int position) {
//                    viewHolder.food_name.setText(model.getFoodName());
//                    // final SelectedFood local = model;
//                    viewHolder.setItemClickListener(new ItemClickListener() {
//                        @Override
//                        public void onClick(View view, int position, boolean isLongClick) {
//                            Intent intent = new Intent(FoodList.this, FoodNutritiensDisplayPrieview.class);
//                            StringBuilder amm = new StringBuilder();
//                            amm.append("https://api.nal.usda.gov/ndb/V2/reports?ndbno=");
//                            amm.append(model.getFoodid());
//                            amm.append("&type=f&format=json&api_key=HXLecTDsMqy1Y6jNoYPw2n3DQ30FeGXxD2XBZqJh");
//                            //new JSONTask().execute(amm.toString());
//                            intent.putExtra("id", model.getFoodid());
//                            intent.putExtra("foodname", model.getFoodName());
//                            intent.putExtra("foodselection", foodselection);
//
//                            startActivity(intent);
//                            Log.d("ama", "Element " + position + " clicked.");
//
//                            //  final String selected = mObjects.get(position).getId();
//
//                            // Toast.makeText(getApplicationContext(), selected, Toast.LENGTH_SHORT).show();
//                            Toast.makeText(FoodList.this, "" + model.getFoodid(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//            };
//        }
//        //set Adapter
//       //recyclerView.setAdapter(adapter)
       // share.setText(newstring);
        //not good implementation
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Date date = new Date();
                Date newDate = new Date(date.getTime());
                SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
                String currentstingdate = dt.format(newDate);
               // if(stringdate.equals(currentstingdate))
               sharefoodlist();
//                else
//                {
//                    Toast.makeText(FoodList.this, "Can;t share earlier day", Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }

    private void sharefoodlist() //only if today
    {

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
                stringdate,
                SharedFoodListDatabase, foodselection
        );
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                Log.d("tavo", response.message());

            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Log.d("tavo", t.getMessage());
            }

        });
            final DatabaseReference sharedfoodlist = database.getReference("MainFeed").child(foodselection).child("SharedDiets");
            final CollectionReference sharedfoodliststore = storage.collection("MainFeed").document(foodselection).collection("SharedDiets");
               final ArrayList<SelectedFood> foundfoods = new ArrayList<>();
//
        SharedFoodProducts sharedFoodProducts = new SharedFoodProducts(FirebaseAuth.getInstance().getCurrentUser().getUid(), stringdate,
                selectedFoods, calories, carbohydrates, protein, fat
        );
        //  sharedfoodlist.child(stringdate+FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(sharedFoodProducts);
        sharedfoodliststore.document(stringdate+FirebaseAuth.getInstance().getCurrentUser().getUid()).set(sharedFoodProducts).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(FoodList.this, "Success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(FoodList.this, "noo", Toast.LENGTH_SHORT).show();
            }
        });
            }

    }


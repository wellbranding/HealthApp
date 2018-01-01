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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import udacityteam.healthapp.R;
import udacityteam.healthapp.adapters.CustomAdapterFoodListPrievew;
import udacityteam.healthapp.adapters.CustomAdapterFoodListPrievewcloud;
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

        Bundle b = iin.getExtras();
        foodselection = (String) b.get("foodselection");
        requestedString = (String) b.get("requestdate");

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
                            recyclerView.setAdapter(customAdapterFoodListPrievew);
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
//       //recyclerView.setAdapter(adapter);
        share = findViewById(R.id.share);
        share.setText(newstring);
        //not good implementation
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Date date = new Date();
                Date newDate = new Date(date.getTime());
                SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
                String currentstingdate = dt.format(newDate);
                if(stringdate.equals(currentstingdate))
                sharefoodlist();
                else
                {
                    Toast.makeText(FoodList.this, "Can;t share earlier day", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sharefoodlist() //only if today
    {
            final DatabaseReference sharedfoodlist = database.getReference("MainFeed").child(foodselection).child("SharedDiets");
            final CollectionReference sharedfoodliststore = storage.collection("MainFeed").document(foodselection).collection("SharedDiets");
               final ArrayList<SelectedFood> foundfoods = new ArrayList<>();
//                foodList.orderByChild("date").equalTo(stringdate).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        List<SelectedFood> userList = new ArrayList<>();
//                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                            foundfoods.add(dataSnapshot1.getValue(SelectedFood.class));
//                            Log.d("shhss", "ahahha");
//                          //  usersdatabase.child(String.valueOf(System.currentTimeMillis())).setValue(dataSnapshot1.getValue(SelectedFood.class));
//                        }
//                        Date date = new Date();
//                        Date newDate = new Date(date.getTime());
//                        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
//                        String stringdate = dt.format(newDate);
//
//
//                        editor.putBoolean("didshared", true);
//                        editor.apply();
//                        Intent intent = new Intent(FoodList.this, MainActivity.class);
//                        startActivity(intent);
//                        database.getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(foodselection+"isshared").setValue(true);
//
//                        Toast.makeText(FoodList.this, "Success!", Toast.LENGTH_SHORT).show();
//                        finish();
//                       // share.setText("agooo");
//
//
//                    }
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                    }
//                });
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


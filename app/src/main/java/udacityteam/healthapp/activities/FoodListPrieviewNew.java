package udacityteam.healthapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import udacityteam.healthapp.R;
import udacityteam.healthapp.adapters.CustomAdapter;
import udacityteam.healthapp.adapters.CustomAdapterFoodListPrievew;
import udacityteam.healthapp.models.SelectedFood;
import udacityteam.healthapp.models.SharedFoodProducts;

public class FoodListPrieviewNew extends AppCompatActivity {

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
    float calories, carbohydrates, protein, fats;
    ArrayList<SelectedFood> selectedFoods;
    TextView caloriescounter, proteincounter, fatcounter, carbohycounter;
    SharedFoodProducts receivedsharedfoodproducts;


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
    //    receivedsharedfoodproducts = (SharedFoodProducts)  b.getParcelable("sharedfoofproducts");
   //  sharedprofucts = receivedsharedfoodproducts.getSelectedFoods();
     selectedFoods = b.getParcelableArrayList("user_list");
     //Log.d("parodyk", String.valueOf(selectedFoods.size()));
        key = (String) b.get("key");
        calories = (float) b.get("calories");
        carbohydrates = (float) b.get("carbohydrates");
        protein = (float) b.get("protein");
        fats = (float) b.get("fats");

            Date date = new Date();
            Date newDate = new Date(date.getTime());
            SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
            stringdate = dt.format(newDate);

        caloriescounter.setText(String.valueOf(calories));
        proteincounter.setText(String.valueOf(protein));
        carbohycounter.setText(String.valueOf(carbohydrates));
        fatcounter.setText(String.valueOf(fats));
        database = FirebaseDatabase.getInstance();
     //getSupportActionBar().setTitle(foodselection);
///        foodList = database.getReference("MainFeed").child(foodselection).child("SharedDiets").child(key).child("SelectedFoods");
//        Log.d("kej", key);
      //  foodList.orderByChild("date").equalTo(stringdate);

        recyclerView = findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        loadListFood();

    }

    private void loadListFood() {
        CustomAdapterFoodListPrievew customAdapterFoodListPrievew = new CustomAdapterFoodListPrievew(selectedFoods);
        recyclerView.setAdapter(customAdapterFoodListPrievew);
//        Log.d("kiekis",String.valueOf(sharedprofucts.size() ));
//        selectedFoods = new ArrayList<>();
//        storage = FirebaseFirestore.getInstance();
//        userstorage= storage.collection("Users").document(key).collection(foodselection).document(stringdate).collection("TodaysFoods");
//        userstorage
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            Log.d("geras", "good");
//                            for (DocumentSnapshot document : task.getResult()) {
//                                SelectedFood food = document.toObject(SelectedFood.class);
//                                Log.d("geras",  food.getDate());
//                                selectedFoods.add(food);
//                            }
//                            CustomAdapterFoodListPrievew customAdapterFoodListPrievew = new CustomAdapterFoodListPrievew(sharedprofucts);
//                            recyclerView.setAdapter(customAdapterFoodListPrievew);
//                        } else {
//                            Log.d("geras", "Error getting documents: ", task.getException());
//                        }
//                    }
//
//                });
//        Log.d("geras",  String.valueOf(selectedFoods.size()));
////        foodList.addValueEventListener(new ValueEventListener() {
////            @Override
////            public void onDataChange(DataSnapshot dataSnapshot) {
////                // Getting current user Id
////                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
////
////
////                // Filter User
////                List<String> userList = new ArrayList<>();
//////                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//////                            Log.d("shhss", "ahahha");
//////                         //   listodydis.setText();
//////                            if (!dataSnapshot1.getValue(SelectedFood.class).getUserId().equals(uid)) {
//////                                userList.add(dataSnapshot1.getValue(SelectedFood.class));
//////                            }
//////                        }
////                ArrayList<SelectedFood> selectedFoods = new ArrayList<>();
////                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
////
////                    SelectedFood foundFood = dataSnapshot1.getValue(SelectedFood.class);
////                     selectedFoods.add(foundFood);
////
////                }
////
////                CustomAdapterFoodListPrievew mAdapter = new CustomAdapterFoodListPrievew(selectedFoods);
////                recyclerView.setAdapter(mAdapter);
////
////            }
////
////            @Override
////            public void onCancelled(DatabaseError databaseError) {
////                throw databaseError.toException();
////                //listodydis.setText("errrooas");
////            }
////        });

        sharefoodlist();
    }

    private void sharefoodlist() //only if today
    {
        share = findViewById(R.id.share);
        share.setText("Cant share");
//
    }
}

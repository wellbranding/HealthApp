package udacityteam.healthapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import udacityteam.healthapp.R;
import udacityteam.healthapp.models.SelectedFood;
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
    String newstring=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("SharedToday", MODE_PRIVATE);
        Boolean didsharedtoday = pref.getBoolean("didshared", false);
        Log.d("did", didsharedtoday.toString());
        editor = pref.edit();




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
        database = FirebaseDatabase.getInstance();
        database.getReference("User").child(FirebaseAuth.getInstance().
                getCurrentUser().getUid()).child(foodselection+"isshared").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean value = (Boolean) dataSnapshot.getValue();
                if(value==true) {
                    newstring = "Modify your shared diet";
                    Log.d("pagaliau", newstring);
                    loadListFood();

                }

                else {
                    newstring = "shared.";
                    loadListFood();
                }

                // do your stuff here with value
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
     //   Log.d("good", newstring);



        getSupportActionBar().setTitle(foodselection);
        foodList = database.getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(foodselection);
        foodList.orderByChild("date").equalTo(stringdate);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    private void loadListFood() {
        if (stringdate != null) {
            adapter = new FirebaseRecyclerAdapter<SelectedFood, FoodViewHolder>(SelectedFood.class,
                    R.layout.food_item,
                    FoodViewHolder.class,
                    foodList.orderByChild("date").equalTo(stringdate)) {

                @Override
                protected void populateViewHolder(FoodViewHolder viewHolder, final SelectedFood model, int position) {
                    viewHolder.food_name.setText(model.getFoodName());
                    // final SelectedFood local = model;
                    viewHolder.setItemClickListener(new ItemClickListener() {
                        @Override
                        public void onClick(View view, int position, boolean isLongClick) {
                            Intent intent = new Intent(FoodList.this, FoodNutritiensDisplayPrieview.class);
                            StringBuilder amm = new StringBuilder();
                            amm.append("https://api.nal.usda.gov/ndb/V2/reports?ndbno=");
                            amm.append(model.getFoodid());
                            amm.append("&type=f&format=json&api_key=HXLecTDsMqy1Y6jNoYPw2n3DQ30FeGXxD2XBZqJh");
                            //new JSONTask().execute(amm.toString());
                            intent.putExtra("id", model.getFoodid());
                            intent.putExtra("foodname", model.getFoodName());
                            intent.putExtra("foodselection", foodselection);

                            startActivity(intent);
                            Log.d("ama", "Element " + position + " clicked.");

                            //  final String selected = mObjects.get(position).getId();

                            // Toast.makeText(getApplicationContext(), selected, Toast.LENGTH_SHORT).show();
                            Toast.makeText(FoodList.this, "" + model.getFoodid(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            };
        }
        //set Adapter
        recyclerView.setAdapter(adapter);
      //  if(database.getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(foodselection+"isshared").toString().equals("true"))


        share = findViewById(R.id.share);
        share.setText(newstring);
        //not good implementation
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = new Date();
                Date newDate = new Date(date.getTime());
                SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
                String todaydate= dt.format(newDate);
                if(requestedString.equals(todaydate))
                sharefoodlist();
                else
                {
                    Toast.makeText(FoodList.this, "You can only share todays", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void sharefoodlist() //only if today
    {

                //usersdatabase = database.getReference("MainFeed").child(foodselection).child("SharedDiets").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            final DatabaseReference sharedfoodlist = database.getReference("MainFeed").child(foodselection).child("SharedDiets");
               final ArrayList<SelectedFood> foundfoods = new ArrayList<>();
                foodList.orderByChild("date").equalTo(stringdate).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<SelectedFood> userList = new ArrayList<>();
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            foundfoods.add(dataSnapshot1.getValue(SelectedFood.class));
                            Log.d("shhss", "ahahha");
                          //  usersdatabase.child(String.valueOf(System.currentTimeMillis())).setValue(dataSnapshot1.getValue(SelectedFood.class));
                        }
                        Date date = new Date();
                        Date newDate = new Date(date.getTime());
                        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
                        String stringdate = dt.format(newDate);

                        SharedFoodProducts sharedFoodProducts = new SharedFoodProducts(FirebaseAuth.getInstance().getCurrentUser().getUid(), stringdate, foundfoods);
                        sharedfoodlist.child(stringdate+FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(sharedFoodProducts);
                        editor.putBoolean("didshared", true);
                        editor.apply();
                        Intent intent = new Intent(FoodList.this, MainActivity.class);
                        startActivity(intent);
                        database.getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(foodselection+"isshared").setValue(true);

                        Toast.makeText(FoodList.this, "Success!", Toast.LENGTH_SHORT).show();
                        finish();
                       // share.setText("agooo");


                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }

    }


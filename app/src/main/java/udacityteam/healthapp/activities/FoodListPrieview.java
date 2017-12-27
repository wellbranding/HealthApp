package udacityteam.healthapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class FoodListPrieview extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference foodList;
    DatabaseReference usersdatabase;
    String foodselection, key;

    String catergoryId = "ff";
    FirebaseRecyclerAdapter<SelectedFood, FoodViewHolder> adapter;
    String stringdate;
    Button share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        Intent iin = getIntent();

        Bundle b = iin.getExtras();
        foodselection = (String) b.get("foodselection");
        key = (String) b.get("key");
            Date date = new Date();
            Date newDate = new Date(date.getTime());
            SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
            stringdate = dt.format(newDate);



        database = FirebaseDatabase.getInstance();
        getSupportActionBar().setTitle(foodselection);
        foodList = database.getReference("MainFeed").child(foodselection).child("SharedDiets").child(key);
        foodList.orderByChild("date").equalTo(stringdate);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        loadListFood(catergoryId);


    }

    private void loadListFood(String catergoryId) {
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
                            Intent intent = new Intent(FoodListPrieview.this, FoodNutritiensDisplayPrieview.class);
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
                            Toast.makeText(FoodListPrieview.this, "" + model.getFoodid(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            };
        }
        //set Adapter
        recyclerView.setAdapter(adapter);
        sharefoodlist();
    }

    private void sharefoodlist() //only if today
    {
        share = findViewById(R.id.share);
        share.setText("Cant share");
//        share.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                usersdatabase = database.getReference("MainFeed").child(foodselection).child("SharedDiets").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//
//                foodList.orderByChild("date").equalTo(stringdate).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        List<SelectedFood> userList = new ArrayList<>();
//                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                            Log.d("shhss", "ahahha");
//                            usersdatabase.child(String.valueOf(System.currentTimeMillis())).setValue(dataSnapshot1.getValue(SelectedFood.class));
//                        }
//                    }
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                    }
//                });
//            }
//        });
    }
}

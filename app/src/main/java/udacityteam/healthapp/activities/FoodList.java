package udacityteam.healthapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import udacityteam.healthapp.R;

public class FoodList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference foodList;

    String catergoryId="ff";
    FirebaseRecyclerAdapter<Request, FoodViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        //Firebase
        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Snacks");

        recyclerView = (RecyclerView)findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Get Intent Here
      //  if (getIntent() != null) {
        //    catergoryId = getIntent().getStringExtra("CategoryId");
     //   }// if (!catergoryId.isEmpty() && catergoryId != null) {
            loadListFood(catergoryId);
        //}

    }

    private void loadListFood(String catergoryId) {
        adapter = new FirebaseRecyclerAdapter<Request, FoodViewHolder>(Request.class,
                R.layout.food_item,
                FoodViewHolder.class,
                foodList.orderByChild("name")) {
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, final Request model, int position) {
                viewHolder.food_name.setText(model.getName());
                final Request local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(FoodList.this, ""+model.getName(), Toast.LENGTH_SHORT).show();
                        {

                        }
                    }
                });
            }
        };
        //set Adapter
        recyclerView.setAdapter(adapter);
    }
}

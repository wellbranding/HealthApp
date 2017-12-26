package udacityteam.healthapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import udacityteam.healthapp.R;
import udacityteam.healthapp.activities.FoodList;
import udacityteam.healthapp.activities.FoodViewHolder;
import udacityteam.healthapp.activities.ItemClickListener;
import udacityteam.healthapp.activities.SelectedFood;
import udacityteam.healthapp.adapters.CustomAdapter;

/**
 * Created by vvost on 11/16/2017.
 */

public class Tab1Contacts extends Fragment {
    private static final String TAG = "RecyclerViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
    private static final int DATASET_COUNT = 60;
   private RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<SelectedFood, FoodViewHolder> adapter;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected Tab1Contacts.LayoutManagerType mCurrentLayoutManagerType;

    protected RadioButton mLinearLayoutRadioButton;
    protected RadioButton mGridLayoutRadioButton;

    protected RecyclerView mRecyclerView;
    protected CustomAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    TextView listodydis;
    protected String[] mDataset;
    public static String value;

    DatabaseReference foodList;
    FirebaseDatabase database;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        Intent iin = getActivity().getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {
            value = (String) b.get("databasevalue");
            //  Textv.setText(j);
        }


        // Initialize dataset, this data would usually come from a local content provider or
        // remote server.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycler_view_frag1, container, false);
        rootView.setTag(TAG);
        foodList = database.getReference("MainFeed").child(value).child("SharedDiets");

        mRecyclerView = rootView.findViewById(R.id.recyclerView);
        listodydis = (TextView) rootView.findViewById(R.id.listodydis);

                // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
      //  listodydis.setText(foodList.orderByChild("userId").toString());

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        foodList.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Getting current user Id
                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


                        // Filter User
                        List<String> userList = new ArrayList<>();
//                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                            Log.d("shhss", "ahahha");
//                         //   listodydis.setText();
//                            if (!dataSnapshot1.getValue(SelectedFood.class).getUserId().equals(uid)) {
//                                userList.add(dataSnapshot1.getValue(SelectedFood.class));
//                            }
//                        }
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            Log.d("shhss", dataSnapshot1.getKey().toString());
                            //   listodydis.setText();
                            if (!dataSnapshot1.getKey().equals(uid)) {
                                    userList.add(dataSnapshot1.getKey());
                            }
                        }


                        mAdapter = new CustomAdapter(userList);
                        mRecyclerView.setAdapter(mAdapter);


                        // Setting d

                    }

                    @Override
                        public void onCancelled(DatabaseError databaseError) {
                        throw databaseError.toException();
                        //listodydis.setText("errrooas");
                    }
                });
        return rootView;
    }
    private void collectPhoneNumbers(Map<String,Object> users) {

        ArrayList<Long> phoneNumbers = new ArrayList<>();

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()){

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            phoneNumbers.add((Long) singleUser.get("userId"));
        }

        System.out.println(phoneNumbers.toString());
    }

    private void loadListFood(List<Object> values) {
        adapter = new FirebaseRecyclerAdapter<SelectedFood, FoodViewHolder>(SelectedFood.class,
                R.layout.food_item,
                FoodViewHolder.class,
                foodList) {

            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, final SelectedFood model, int position) {
                viewHolder.food_name.setText(model.getFoodName());
                // final SelectedFood local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(getActivity(), ""+model.getFoodid(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        //set Adapter
        mRecyclerView.setAdapter(adapter);
    }

}

package udacityteam.healthapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import udacityteam.healthapp.PHP_Retrofit.APIService;
import udacityteam.healthapp.PHP_Retrofit.APIUrl;
import udacityteam.healthapp.PHP_Retrofit.SelectedFoodretrofit;
import udacityteam.healthapp.PHP_Retrofit.SelectedFoodretrofitarray;
import udacityteam.healthapp.PHP_Retrofit.SharedFoodProductsRetrofit;
import udacityteam.healthapp.R;
import udacityteam.healthapp.activities.FilterActivity;
import udacityteam.healthapp.activities.FoodList;
import udacityteam.healthapp.activities.FoodViewHolder;
import udacityteam.healthapp.activities.ItemClickListener;
import udacityteam.healthapp.adapters.CustomAdapterFoodListPrievew;
import udacityteam.healthapp.adapters.CustomAdapterFoodListPrievewretro;
import udacityteam.healthapp.adapters.CustomAdapterSharedFoodstore;
import udacityteam.healthapp.adapters.CustomAdapterSharedFoodstoreRetrofit;
import udacityteam.healthapp.models.SelectedFood;
import udacityteam.healthapp.adapters.CustomAdapter;
import udacityteam.healthapp.models.SharedFoodProducts;

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
    FirebaseFirestore storage;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected Tab1Contacts.LayoutManagerType mCurrentLayoutManagerType;

    protected RadioButton mLinearLayoutRadioButton;
    protected RadioButton mGridLayoutRadioButton;

    protected RecyclerView mRecyclerView;
    protected CustomAdapterSharedFoodstoreRetrofit mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    TextView listodydis;
    protected String[] mDataset;
    public static String value = "Breakfast";
    ProgressBar progressBar;

    private FirebaseAuth mAuth;
    DatabaseReference foodList;
    FirebaseDatabase database;
    Button filterData, test;
    String side, SharedFoodListDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance();
        Intent iin = getActivity().getIntent();
        final Bundle args = getArguments();
         side = args.getString("value");
        SharedFoodListDatabase =  args.getString("SharedFoodListDatabase");
        Bundle b = iin.getExtras();
        if (b != null) {
          //  SharedFoodListDatabase = (String) b.get("SharedFoodListDatabase");
            value = (String) b.get("databasevalue");

            //  Textv.setText(j);
        }
        Log.d("dataaa", String.valueOf(SharedFoodListDatabase));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycler_view_frag1, container, false);
        rootView.setTag(TAG);
       // foodList = database.getReference("MainFeed").child(value).child("SharedDiets");

        mRecyclerView = rootView.findViewById(R.id.recyclerView);
        listodydis = (TextView) rootView.findViewById(R.id.listodydis);
        progressBar = rootView.findViewById(R.id.progressbar);
        filterData = rootView.findViewById(R.id.filterdata);
        test = rootView.findViewById(R.id.test);
        if(side!=null)
        {
            Log.d("pabvyko", "paby");
        }
        filterData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FilterActivity.class);
                startActivity(intent);
            }
        });
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        RetrofitList();


if(side!=null)
{
    final ArrayList<SharedFoodProducts> userlist = new ArrayList<>();
    ArrayList<SelectedFood> fooda = new ArrayList<>();
    CollectionReference sharedfoodliststoree = storage.collection("MainFeed").document("Breakfast").
            collection("SharedDiets");
    SharedFoodProducts sharedFoodProducts = new SharedFoodProducts(
            "aaaaaaaaaaa", "201332",
            fooda, 10, 1222, 3000, 34500);
    //  sharedfoodlist.child(stringdate+FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(sharedFoodProducts);
    sharedfoodliststoree.document("ajahhaaaz").set(sharedFoodProducts);
    CollectionReference sharedfoodliststore = storage.collection("MainFeed").document("Breakfast").collection("SharedDiets");
    sharedfoodliststore
            .orderBy("calories",  Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            Log.d("protein",  Float.valueOf(document.getData().get("protein").toString()).toString());
                            if (document.getData().get("userId").equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                                    {
                                //good
                            }
                            else if( Float.valueOf(document.getData().get("protein").toString())>1000 &&
                                    Float.valueOf(document.getData().get("fats").toString())<=500
                                    && Float.valueOf(document.getData().get("calories").toString())>5000)
                            {

                            }
                                else {
                                SharedFoodProducts food = document.toObject(SharedFoodProducts.class);
                                userlist.add(food);

                            }
                        }

                        progressBar.setVisibility(View.GONE);
                    } else {
                        Log.d("geras", "Error getting documents: ", task.getException());
                    }
                }

            });

}
        return rootView;
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

        Call<SharedFoodProductsRetrofit> call = service.getAllSharedDiets(
                FirebaseAuth.getInstance().getCurrentUser().getUid(),
                SharedFoodListDatabase
        );
        call.enqueue(new Callback<SharedFoodProductsRetrofit>() {
            @Override
            public void onResponse(Call<SharedFoodProductsRetrofit> call, Response<SharedFoodProductsRetrofit> response) {
                mAdapter = new CustomAdapterSharedFoodstoreRetrofit(response.body().
                        getSelectedFoodretrofits(), SharedFoodListDatabase);
               mRecyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onFailure(Call<SharedFoodProductsRetrofit> call, Throwable t) {

            }


        });

    }


}

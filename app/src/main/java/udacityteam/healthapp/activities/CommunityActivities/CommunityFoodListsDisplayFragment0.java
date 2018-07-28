package udacityteam.healthapp.activities.CommunityActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import udacityteam.healthapp.PHP_Retrofit_API.APIService;
import udacityteam.healthapp.PHP_Retrofit_API.APIUrl;
import udacityteam.healthapp.Model.SharedFoodProductsRetrofit;
import udacityteam.healthapp.R;
import udacityteam.healthapp.activities.ApplicationClass;
import udacityteam.healthapp.adapters.FoodViewHolder;
import udacityteam.healthapp.adapters.SharedFoodListsAdapter;
import udacityteam.healthapp.models.SelectedFood;
import udacityteam.healthapp.models.SharedFoodProducts;

/**
 * Created by vvost on 11/16/2017.
 */

public class CommunityFoodListsDisplayFragment0 extends Fragment {
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

    protected CommunityFoodListsDisplayFragment0.LayoutManagerType mCurrentLayoutManagerType;

    protected RadioButton mLinearLayoutRadioButton;
    protected RadioButton mGridLayoutRadioButton;

    protected RecyclerView mRecyclerView;
    protected SharedFoodListsAdapter mAdapter;
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
         side = args.getString("foodselection");
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
        View rootView = inflater.inflate(R.layout.community_list_fragment, container, false);
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
                intent.putExtra("SharedFoodListDatabase", SharedFoodListDatabase);
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
                ((ApplicationClass)getActivity().getApplicationContext()).getId(),
                SharedFoodListDatabase
        );
        call.enqueue(new Callback<SharedFoodProductsRetrofit>() {
            @Override
            public void onResponse(Call<SharedFoodProductsRetrofit> call, Response<SharedFoodProductsRetrofit> response) {
                mAdapter = new SharedFoodListsAdapter(response.body().
                        getSelectedFoodretrofits(), side);
                Log.d("important", response.body().getSelectedFoodretrofits().get(0).getUserProfile().getDisplayname());
               mRecyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onFailure(Call<SharedFoodProductsRetrofit> call, Throwable t) {

            }


        });

    }


}

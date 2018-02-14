package udacityteam.healthapp.activities.CommunityActivities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import udacityteam.healthapp.Model.OneSharedFoodProductsListRetrofit;
import udacityteam.healthapp.Model.SelectedFoodretrofit;
import udacityteam.healthapp.Model.SharedFoodProductsRetrofit;
import udacityteam.healthapp.PHP_Retrofit_API.APIService;
import udacityteam.healthapp.PHP_Retrofit_API.APIUrl;
import udacityteam.healthapp.R;
import udacityteam.healthapp.activities.ApplicationClass;
import udacityteam.healthapp.activities.FoodListViewModel;
import udacityteam.healthapp.activities.SharedFoodListItemViewModel;
import udacityteam.healthapp.adapters.FoodListRetrofitAdapterNew;
import udacityteam.healthapp.adapters.FoodViewHolder;
import udacityteam.healthapp.adapters.SharedFoodListsAdapter;
import udacityteam.healthapp.adapters.SharedFoodListsAdapterNew;
import udacityteam.healthapp.databinding.CommunityListFragmentBinding;
import udacityteam.healthapp.models.SelectedFood;
import udacityteam.healthapp.models.SharedFoodProducts;

/**
 * Created by vvost on 11/16/2017.
 */

public class CommunityFoodListsDisplayFragment0MVVM extends Fragment implements CommunityFoodListDisplayFragment0MVVMViewModel.DataListener {
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

    protected CommunityFoodListsDisplayFragment0MVVM.LayoutManagerType mCurrentLayoutManagerType;

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
    private CommunityListFragmentBinding communityListFragmentBinding;
    private CommunityFoodListDisplayFragment0MVVMViewModel viewModel;

    public static CommunityFoodListsDisplayFragment0MVVM newInstance(Bundle values) {
        CommunityFoodListsDisplayFragment0MVVM storiesFragment = new CommunityFoodListsDisplayFragment0MVVM();
        storiesFragment.setArguments(values);
        return storiesFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new CommunityFoodListDisplayFragment0MVVMViewModel(getContext(), this );
        Bundle bundle = getArguments();
        if (bundle != null) {
            side = bundle.getString("foodselection", null);
            SharedFoodListDatabase =  bundle.getString("SharedFoodListDatabase");
        }

        mAuth = FirebaseAuth.getInstance();

    }
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        communityListFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.community_list_fragment, container, false);
        InitializeRecyclerView();

     //  communityListFragmentBinding.setViewModel(viewModel);
        viewModel.LoadFoodList(SharedFoodListDatabase);

        return communityListFragmentBinding.getRoot();
    }


//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.community_list_fragment, container, false);
//        rootView.setTag(TAG);
//       // foodList = database.getReference("MainFeed").child(value).child("SharedDiets");
//
//        mRecyclerView = rootView.findViewById(R.id.recyclerView);
//        listodydis = (TextView) rootView.findViewById(R.id.listodydis);
//        progressBar = rootView.findViewById(R.id.progressbar);
//        filterData = rootView.findViewById(R.id.filterdata);
//        test = rootView.findViewById(R.id.test);
//        if(side!=null)
//        {
//            Log.d("pabvyko", "paby");
//        }
//        filterData.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), FilterActivity.class);
//                intent.putExtra("SharedFoodListDatabase", SharedFoodListDatabase);
//                startActivity(intent);
//            }
//        });
//
//        RetrofitList();
//
//
//        return rootView;
//    }
@Override
public void onRepositoriesChanged(List<OneSharedFoodProductsListRetrofit> repositories) {
    Log.d("ijunge", String.valueOf(repositories.size()));
    SharedFoodListsAdapterNew customAdapterFoodListPrievew= new
            SharedFoodListsAdapterNew(repositories, side);

    customAdapterFoodListPrievew.setSelectedFoods(repositories);
    customAdapterFoodListPrievew.notifyDataSetChanged();
    communityListFragmentBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    communityListFragmentBinding.recyclerView.setHasFixedSize(true);
    communityListFragmentBinding.recyclerView.setAdapter(customAdapterFoodListPrievew);
}


    private void InitializeRecyclerView()
    {

        mLayoutManager = new LinearLayoutManager(getActivity());
        communityListFragmentBinding.recyclerView.setLayoutManager(mLayoutManager);

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
               mRecyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onFailure(Call<SharedFoodProductsRetrofit> call, Throwable t) {

            }


        });

    }


}

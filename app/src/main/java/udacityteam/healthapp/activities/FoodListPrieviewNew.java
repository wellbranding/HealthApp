package udacityteam.healthapp.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import udacityteam.healthapp.Model.OneSharedFoodProductsListRetrofit;
import udacityteam.healthapp.Model.SelectedFoodretrofit;
import udacityteam.healthapp.R;
import udacityteam.healthapp.adapters.FoodListRetrofitAdapterNew;
import udacityteam.healthapp.databases.DatabaseHelper;
import udacityteam.healthapp.databinding.ActivityFoodListPreviewBinding;
import udacityteam.healthapp.databinding.FoodActivityBinding;

/**
 * Created by vvost on 11/26/2017.
 */

public class FoodListPrieviewNew extends AppCompatActivity implements FoodListPrieviewNewViewModel.DataListener {
    private static final String SELECTED_FOOD_PRIEVIEW = "EXTRA_REPOSITORY";
    private static final String FOOD_SELECTION = "FOOD_SELECTION";
    String id = null;
    OneSharedFoodProductsListRetrofit oneSharedFoodProductsListRetrofit;
    String foodselection;

    private ActivityFoodListPreviewBinding binding;
    private FoodListPrieviewNewViewModel foodListPrieviewNewViewModel;
//    private FoodNutritiensDisplayViewModel foodNutritiensDisplayViewModel;
    public static Intent newIntent(Context context, OneSharedFoodProductsListRetrofit selectedFoodretrofit
    , String foodselection) {
            Intent intent = new Intent(context, FoodListPrieviewNew.class);
        intent.putExtra(SELECTED_FOOD_PRIEVIEW, selectedFoodretrofit);
        intent.putExtra(FOOD_SELECTION, foodselection);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_food_list_preview);
        foodListPrieviewNewViewModel = new FoodListPrieviewNewViewModel(this, this);
        binding.setViewModel(foodListPrieviewNewViewModel);
      oneSharedFoodProductsListRetrofit = getIntent().getParcelableExtra(SELECTED_FOOD_PRIEVIEW);
      foodselection = getIntent().getExtras().getString(FOOD_SELECTION);
      Log.d("fooo", foodselection);
      foodListPrieviewNewViewModel.LoadFoodList(oneSharedFoodProductsListRetrofit.getParentSharedFoodsId(), foodselection);

//        if(b!=null)
//        {
//           id =(String) b.get("id");
//           foodname = (String) b.get("foodname");
//            foodselection = (String) b.get("foodselection");
////            addtoSqlite.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View view) {
////                    AddFoodtoDatabase();
////                }
////            });
//            getSupportActionBar().setTitle(foodselection);
//            productname.setText(foodname);
//            StringBuilder amm = new StringBuilder();
//            amm.append("https://api.nal.usda.gov/ndb/V2/reports?ndbno=");
//            amm.append(id);
//            amm.append("&type=f&format=json&api_key=HXLecTDsMqy1Y6jNoYPw2n3DQ30FeGXxD2XBZqJh");
//           JSONTask jsonTask =  new JSONTask();
//           jsonTask.execute(amm.toString());
//          //  Textv.setText(j);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
     //   foodNutritiensDisplayViewModel.destroy();
    }

    @Override
    public void onRepositoriesChanged(List<SelectedFoodretrofit> repositories) {

        FoodListRetrofitAdapterNew customAdapterFoodListPrievew= new
                FoodListRetrofitAdapterNew(repositories);


        customAdapterFoodListPrievew.setSelectedFoodretrofits(repositories);
        customAdapterFoodListPrievew.notifyDataSetChanged();
       binding.recyclerFood.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerFood.setHasFixedSize(true);
        binding.recyclerFood.setAdapter(customAdapterFoodListPrievew);
    }
}


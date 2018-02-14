package udacityteam.healthapp.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import udacityteam.healthapp.Model.SelectedFoodretrofit;
import udacityteam.healthapp.R;
import udacityteam.healthapp.databases.DatabaseHelper;
import udacityteam.healthapp.databinding.FoodActivityBinding;

/**
 * Created by vvost on 11/26/2017.
 */

public class FoodNutritiensDisplayPrieview extends AppCompatActivity {
    private static final String SELECTED_FOOD_PRIEVIEW = "EXTRA_REPOSITORY";
    String id = null;

    private FoodActivityBinding binding;
    private FoodNutritiensDisplayViewModel foodNutritiensDisplayViewModel;
    public static Intent newIntent(Context context, SelectedFoodretrofit selectedFoodretrofit) {
        Intent intent = new Intent(context, FoodNutritiensDisplayPrieview.class);
        intent.putExtra(SELECTED_FOOD_PRIEVIEW, selectedFoodretrofit);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.food_activity);

        SelectedFoodretrofit repository = getIntent().getParcelableExtra(SELECTED_FOOD_PRIEVIEW);
        foodNutritiensDisplayViewModel = new FoodNutritiensDisplayViewModel(this, repository);
        binding.setViewModel(foodNutritiensDisplayViewModel);


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
        foodNutritiensDisplayViewModel.destroy();
    }

    }


package udacityteam.healthapp.activities.CommunityActivities;

import android.app.DialogFragment;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import io.apptik.widget.MultiSlider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import udacityteam.healthapp.PHP_Retrofit_API.APIService;
import udacityteam.healthapp.PHP_Retrofit_API.APIUrl;
import udacityteam.healthapp.Model.OneSharedFoodProductsListRetrofit;
import udacityteam.healthapp.Model.SharedFoodProductsRetrofit;
import udacityteam.healthapp.R;
import udacityteam.healthapp.activities.ApplicationClass;

public class FilterActivity extends DialogFragment {
  //  private DialogInterface.OnDismissListener onDismissListener;

//    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
//        this.onDismissListener = onDismissListener;
//    }

    TextView carbohydratesbegin, carbohydratesend, proteinbegin, proteinend, caloriesbegin, caloriesend,
            fatsbegin, fatsend;
    MultiSlider carbohydrates, protein, calories, fats;
    String SharedFoodListDatabase;
    Button confirm;
    CommunityFoodListDisplayFragment0MVVMViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_filter, container, false);
        carbohydratesbegin =view.findViewById(R.id.carbobegin);
        carbohydratesend = view.findViewById(R.id.carboend);
        proteinbegin = view.findViewById(R.id.proteinbegin);
        proteinend = view.findViewById(R.id.proteinend);
        caloriesbegin = view.findViewById(R.id.caloriesbegin);
        caloriesend = view.findViewById(R.id.caloriesnend);
        fatsbegin = view.findViewById(R.id.fatbegin);
        fatsend = view.findViewById(R.id.fatnend);
        confirm = view.findViewById(R.id.confirm);

        viewModel = ViewModelProviders.of((FragmentActivity) getActivity()).get(CommunityFoodListDisplayFragment0MVVMViewModel.class);
        viewModel.Hello();

        Intent iin = getActivity().getIntent();

        Bundle b = iin.getExtras();

        SharedFoodListDatabase = (String) b.get("SharedFoodListDatabase");


        carbohydrates = view.findViewById(R.id.carbohydrates);
        protein = view.findViewById(R.id.protein);
        calories = view.findViewById(R.id.calories);
        fats = view.findViewById(R.id.fat);
    carbohydrates.setMax(10000);
        protein.setMax(10000);
        fats.setMax(10000);
        calories.setMax(10000);
        calories.jumpDrawablesToCurrentState();
        carbohydrates.getThumb(0).setValue(0);
        carbohydrates.getThumb(1).setValue(10000);
        protein.getThumb(0).setValue(0);
        protein.getThumb(1).setValue(10000);
        fats.getThumb(0).setValue(0);
        fats.getThumb(1).setValue(10000);
        calories.getThumb(0).setValue(0);
        calories.getThumb(1).setValue(10000);
        carbohydratesbegin.setText(String.valueOf(carbohydrates.getThumb(0).getValue()));
        carbohydratesend.setText(String.valueOf(carbohydrates.getThumb(1).getValue()));
        proteinbegin.setText(String.valueOf(protein.getThumb(0).getValue()));
        proteinend.setText(String.valueOf(protein.getThumb(1).getValue()));
        caloriesbegin.setText(String.valueOf(calories.getThumb(0).getValue()));
        caloriesend.setText(String.valueOf(calories.getThumb(1).getValue()));
        fatsbegin.setText(String.valueOf(fats.getThumb(0).getValue()));
        fatsend.setText(String.valueOf(fats.getThumb(1).getValue()));
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              GetFilteredSharedDiets();
              dismiss();
            }
        });

        carbohydrates.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider,
                                       MultiSlider.Thumb thumb,
                                       int thumbIndex,
                                       int value) {
                carbohydratesbegin.setText(String.valueOf(carbohydrates.getThumb(0).getValue()));
                carbohydratesend.setText(String.valueOf(carbohydrates.getThumb(1).getValue()));
            }
        });
        protein.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider,
                                       MultiSlider.Thumb thumb,
                                       int thumbIndex,
                                       int value) {
                proteinbegin.setText(String.valueOf(protein.getThumb(0).getValue()));
                proteinend.setText(String.valueOf(protein.getThumb(1).getValue()));
            }
        });
        fats.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider,
                                       MultiSlider.Thumb thumb,
                                       int thumbIndex,
                                       int value) {
                fatsbegin.setText(String.valueOf(fats.getThumb(0).getValue()));
                fatsend.setText(String.valueOf(fats.getThumb(1).getValue()));
            }
        });
        calories.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider,
                                       MultiSlider.Thumb thumb,
                                       int thumbIndex,
                                       int value) {
                caloriesbegin.setText(String.valueOf(calories.getThumb(0).getValue()));
                caloriesend.setText(String.valueOf(calories.getThumb(1).getValue()));
            }
        });
        return  view;
    }

    private void GetFilteredSharedDiets() //only if today
    {

        viewModel.GetFilteredSharedDiets(protein, calories, carbohydrates, fats, SharedFoodListDatabase );


//        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//        Gson gson = new GsonBuilder()
//                .setLenient()
//                .create();
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(APIUrl.BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();
//
//        //Defining retrofit api service
//        APIService service = retrofit.create(APIService.class);
//
//        Call<SharedFoodProductsRetrofit> call = service.getAllFilteredSharedDiets(((ApplicationClass) getActivity().getApplicationContext()).getId()
//                , SharedFoodListDatabase, protein.getThumb(0).getValue(), protein.getThumb(1).getValue(),
//                calories.getThumb(0).getValue(), calories.getThumb(1).getValue(),
//                carbohydrates.getThumb(0).getValue(), carbohydrates.getThumb(1).getValue(),
//                fats.getThumb(0).getValue(), fats.getThumb(1).getValue()
//        );
//        call.enqueue(new Callback<SharedFoodProductsRetrofit>() {
//            @Override
//            public void onResponse(Call<SharedFoodProductsRetrofit> call, Response<SharedFoodProductsRetrofit> response) {
//                List<OneSharedFoodProductsListRetrofit> selectedFoodretrofits = response.body().
//                        getSelectedFoodretrofits();
//
//                if (selectedFoodretrofits.size() != 0) {
//                    Toast.makeText(getActivity(), String.valueOf(selectedFoodretrofits.get(0).getCalories()), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<SharedFoodProductsRetrofit> call, Throwable t) {
//
//            }
//
//        });

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
}





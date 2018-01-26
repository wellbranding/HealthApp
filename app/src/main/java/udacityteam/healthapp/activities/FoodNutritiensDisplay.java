package udacityteam.healthapp.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import udacityteam.healthapp.PHP_Retrofit_API.APIService;
import udacityteam.healthapp.PHP_Retrofit_API.APIUrl;
import udacityteam.healthapp.PHP_Retrofit_Models.Result;
import udacityteam.healthapp.R;
import udacityteam.healthapp.databases.DatabaseHelper;
import udacityteam.healthapp.models.SelectedFood;
import udacityteam.healthapp.models.SelectedFoodmodel;

/**
 * Created by vvost on 11/26/2017.
 */

public class FoodNutritiensDisplay extends AppCompatActivity {
    TextView Textv;
    Button addtoSqlite;
    DatabaseHelper myDbHelper;
    Cursor c = null;
    String id = null;
    String foodname = null;
    String UserId = null;
    FirebaseDatabase database;
    DatabaseReference user;
    DatabaseReference allusers;
    DatabaseReference requests;
    String foodselection = null;
    TextView productname, nutritionaldisplay;
     DocumentReference userstorage;
    ProgressBar progressBar;
    FirebaseFirestore storage;
    String SharedFoodListDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foodactivity);
        progressBar = findViewById(R.id.progressbar);
        productname = findViewById(R.id.ProductName);
        nutritionaldisplay = findViewById(R.id.nutritionaldysplay);
        UserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Textv = (TextView)findViewById(R.id.tv2);

        Intent iin= getIntent();

        Bundle b = iin.getExtras();

        addtoSqlite = findViewById(R.id.button2);

        addtoSqlite.setActivated(false);

        if(b!=null)
        {
           id =(String) b.get("id");
           foodname = (String) b.get("foodname");
            foodselection = (String) b.get("foodselection");
            SharedFoodListDatabase = (String) b.get("SharedFoodListDatabase");


            getSupportActionBar().setTitle(foodselection);
            productname.setText(foodname);
            StringBuilder amm = new StringBuilder();
            amm.append("https://api.nal.usda.gov/ndb/V2/reports?ndbno=");
            amm.append(id);
            amm.append("&type=f&format=json&api_key=HXLecTDsMqy1Y6jNoYPw2n3DQ30FeGXxD2XBZqJh");
           JSONTask jsonTask =  new JSONTask();
           jsonTask.execute(amm.toString());
          //  Textv.setText(j);
        }
        Date date = new Date();
        Date newDate = new Date(date.getTime());
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        String stringdate = dt.format(newDate);
        storage = FirebaseFirestore.getInstance();
        userstorage= storage.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection(foodselection).document(stringdate);
        database = FirebaseDatabase.getInstance();
        allusers = database.getReference("MainFeed").child(foodselection);
        user = database.getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(foodselection);




    }
    private void AddFoodtoDatabase(List<Float> nutritiens) {
        Date date = new Date();
        Date newDate = new Date(date.getTime());
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Log.d("timestamp", timestamp.toString());

        String stringdate = dt.format(newDate);
        SelectedFood thisuser = new SelectedFood(
                id,
                foodname,
                stringdate
        );
        SelectedFood alluser = new SelectedFood(
                id,
                foodname,
                UserId, timestamp, nutritiens.get(0)
                ,nutritiens.get(1),nutritiens.get(2),nutritiens.get(3)
        );
        SelectedFoodmodel alluser1 = new SelectedFoodmodel(
                id,
                foodname,
                UserId, stringdate
        );

        Map<String, Object> userr = new HashMap<>();
        userr.put("first", nutritiens.get(0));
        DocumentReference nutritiensvalue = storage.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection(foodselection).document("Calories");
        nutritiensvalue.update(userr);


        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        //Defining retrofit api service
        APIService service = retrofit.create(APIService.class);

        Call<Result> call = service.addSelectedFood(
                id,
                UserId, timestamp, nutritiens.get(0)
                ,nutritiens.get(1),nutritiens.get(2),nutritiens.get(3),
                foodselection,0
        );
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                //change Result
                Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                // progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        alluser1.setDate(stringdate);
        alluser1.setFoodid(id);
        alluser1.setUserId(UserId);
        alluser1.setFoodName(foodname);
        userstorage.collection("TodaysFoods").add(alluser);

        user.child(String.valueOf(System.currentTimeMillis()))
                .setValue(thisuser);
     //   allusers.child(String.valueOf(System.currentTimeMillis())).setValue(alluser);
                Intent intent = new Intent(FoodNutritiensDisplay.this, FoodList.class);
                intent.putExtra("requestdate", stringdate);
                intent.putExtra("foodselection", foodselection);
                intent.putExtra("SharedFoodListDatabase", SharedFoodListDatabase);
                startActivity(intent);
                finish();

    }

    public class JSONTask extends AsyncTask<String, String, List<Float>>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            productname.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            nutritionaldisplay.setVisibility(View.INVISIBLE);
            Textv.setVisibility(View.INVISIBLE);
        }

        @Override
        protected List<Float> doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

           // return null;
            Log.e("ayaa", "aaaa");
            try
            {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line = "";
                while((line=reader.readLine())!=null)
                {
                    buffer.append(line);
                }
                String finalJson = buffer.toString();
           JSONObject parentObject = new JSONObject(finalJson);
           JSONArray parentArray = parentObject.getJSONArray("foods");
            StringBuffer finalBufferData = new StringBuffer();

            JSONObject finalobject = parentArray.getJSONObject(0);
            JSONObject aaa = finalobject.getJSONObject("food");
            JSONArray aaa1 = aaa.getJSONArray("nutrients");
            JSONObject enerhy = aaa1.getJSONObject(1);
                JSONObject protein = aaa1.getJSONObject(3);
                JSONObject fat = aaa1.getJSONObject(4);
                JSONObject carbohydrates = aaa1.getJSONObject(6);
              float enerhykcal = Float.parseFloat(String.format(Locale.getDefault(), "%.2f", Float.parseFloat(enerhy.getString("value"))));
                float proteinvalue = Float.parseFloat(String.format(Locale.getDefault(), "%.2f",Float.parseFloat(protein.getString("value"))));
                float fatvalue = Float.parseFloat(String.format(Locale.getDefault(), "%.2f",Float.parseFloat(fat.getString("value"))));
                float carbohydratesvalue = Float.parseFloat(String.format(Locale.getDefault(),"%.2f",Float.parseFloat(carbohydrates.getString("value"))));

           List<Float> modelList = new ArrayList<>();
           modelList.add(enerhykcal);
           modelList.add(proteinvalue);
           modelList.add(fatvalue);
           modelList.add(carbohydratesvalue);
                return modelList;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(connection!=null)
                {
                    connection.disconnect();
                }
                try {
                    if(reader!=null)
                    {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
          return null;
        }

        @Override
        protected void onPostExecute(final List<Float> nutritiens) {
            super.onPostExecute(nutritiens);

            if (nutritiens != null) {
                addtoSqlite.setActivated(true);
                addtoSqlite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AddFoodtoDatabase(nutritiens);
                    }
                });
                productname.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                nutritionaldisplay.setVisibility(View.VISIBLE);
                Textv.setVisibility(View.VISIBLE);
                Textv.setText("CALORIES PER 100G " + nutritiens.get(3) + " KCAL");
            }
            else
            {
                Textv.setText("Oups Error");
            }


//            addtoSqlite = findViewById(R.id.button2);
//            addtoSqlite.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    try {
//                        myDbHelper.openDataBase();
//                    } catch (SQLException sqle) {
//                        throw sqle;
//                    }
//
//                    ContentValues a1 = new ContentValues();
//                    a1.put("Name", id);
//                    a1.put("Name1", foodname);
//
//                    SQLiteDatabase aa = myDbHelper.myDataBase;
//                    aa.insert("Ha", null, a1);
//                    c = myDbHelper.query("Ha", null, null, null, null, null, null);
//                    if (c.moveToFirst()) {
//                        do {
//                            Toast.makeText(FoodNutritiensDisplay.this,
//                                    "_id: " + c.getString(0) + "\n" +
//                                            "E_NAME: " + c.getString(1) + "\n" +
//                                            "E_AGE: " + c.getString(2) + "\n",
//                                    Toast.LENGTH_LONG).show();
//                        } while (c.moveToNext());
//                    }
//                    myDbHelper.close();
//                }
//            });
        }

        }
    }




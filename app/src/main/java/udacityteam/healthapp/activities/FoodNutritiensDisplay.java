package udacityteam.healthapp.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
import java.util.ArrayList;
import java.util.List;

import udacityteam.healthapp.R;
import udacityteam.healthapp.models.Model;
import udacityteam.healthapp.databases.DatabaseHelper;

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
    FirebaseDatabase database;
    DatabaseReference user;
    DatabaseReference requests;
    String foodselection = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foodactivity);
        Textv = (TextView)findViewById(R.id.tv2);

        Intent iin= getIntent();

        Bundle b = iin.getExtras();

        addtoSqlite = findViewById(R.id.button2);



        if(b!=null)
        {
           id =(String) b.get("id");
           foodname = (String) b.get("foodname");
            foodselection = (String) b.get("foodselection");
            addtoSqlite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AddFoodtoDatabase();
                }
            });
            StringBuilder amm = new StringBuilder();
            amm.append("https://api.nal.usda.gov/ndb/V2/reports?ndbno=");
            amm.append(id);
            amm.append("&type=f&format=json&api_key=HXLecTDsMqy1Y6jNoYPw2n3DQ30FeGXxD2XBZqJh");
           JSONTask jsonTask =  new JSONTask();
           jsonTask.execute(amm.toString());
          //  Textv.setText(j);
        }
        database = FirebaseDatabase.getInstance();
        user = database.getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(foodselection);



    }
    private void AddFoodtoDatabase() {
        SelectedFood request = new SelectedFood(
                id,
                foodname
        );
        user.child(String.valueOf(System.currentTimeMillis()))
                .setValue(request);
                Intent intent = new Intent(FoodNutritiensDisplay.this, FoodList.class);
                intent.putExtra("foodselection", foodselection);
                startActivity(intent);
                finish();

    }

    public class JSONTask extends AsyncTask<String, String, String>
    {

        @Override
        protected String doInBackground(String... strings) {
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
            JSONObject enerhy = aaa1.getJSONObject(0);
              String enerhykcal = enerhy.getString("value");

           List<Model> modelList = new ArrayList<>();
            String aki = enerhykcal.toString();
                return aki;


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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s != null) {
            }
            Textv.setText(foodselection);
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




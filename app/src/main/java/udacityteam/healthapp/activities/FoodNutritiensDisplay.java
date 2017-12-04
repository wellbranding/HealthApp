package udacityteam.healthapp.activities;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import udacityteam.healthapp.databases.CopyDbActivity;
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
    DatabaseReference requests;
    String foodselection = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foodactivity);
        Textv = (TextView)findViewById(R.id.tv2);
        myDbHelper = new DatabaseHelper(FoodNutritiensDisplay.this);
        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        try {
            myDbHelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }
        Intent iin= getIntent();

        Bundle b = iin.getExtras();


        if(b!=null)
        {
           id =(String) b.get("id");
           foodname = (String) b.get("foodname");
            foodselection = (String) b.get("foodselection");
            StringBuilder amm = new StringBuilder();
            amm.append("https://api.nal.usda.gov/ndb/V2/reports?ndbno=");
            amm.append(id);
            amm.append("&type=f&format=json&api_key=HXLecTDsMqy1Y6jNoYPw2n3DQ30FeGXxD2XBZqJh");
            new JSONTask().execute(amm.toString());

          //  Textv.setText(j);
        }

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
            database = FirebaseDatabase.getInstance();
            requests = database.getReference(foodselection);
            addtoSqlite = findViewById(R.id.button2);
            addtoSqlite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showAlertDialog();
                }
            });
        }

        private void showAlertDialog() {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(FoodNutritiensDisplay.this);
            alertDialog.setTitle("One more step..");
            alertDialog.setMessage("Enter Your Address : ");

            final EditText edtAddress = new EditText(FoodNutritiensDisplay.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            );
            edtAddress.setLayoutParams(lp);
            alertDialog.setView(edtAddress);    //Add edit Text to alert dialog
         //   alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //Create a new Request
                    Request request = new Request(
                            id,
                            foodname,
                            edtAddress.getText().toString(),
                            "sssss"
                    );
                    //Submit to Firebase
                    //WE will be using System.CureentMilli to key
                    requests.child(String.valueOf(System.currentTimeMillis()))
                            .setValue(request);
                    //Delete Cart
                  //  new Database(getBaseContext()).cleanCart();
                   // Toast.makeText(Cart.this, "Thank You, Order Placed", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(FoodNutritiensDisplay.this, FoodList.class);
                    startActivity(intent);
                    finish();
                }
            });

            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            alertDialog.show();
        }




        }
    }




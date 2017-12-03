package udacityteam.healthapp.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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


public class FoodSearchActivity extends AppCompatActivity {
    private TextView tvData;
    StringBuffer buffer;
    DatabaseReference databaseReference1;
    ArrayAdapter<String> adapter;
    int amm=0;
    DatabaseReference referencee;
    ArrayList<String> arrayCountry;
    ArrayList<Model> models;
    ListView lv;
    String aaki;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button b;

        setContentView(R.layout.activity_foodsearchactivity);
        buffer = new StringBuffer();
        FirebaseApp.initializeApp(this);
        tvData = (TextView) findViewById(R.id.textView);
        final TextView aaa = (TextView) findViewById(R.id.textView2);
        lv = (ListView)findViewById(R.id.listViewCountry);
       arrayCountry = new ArrayList<>();
       models = new ArrayList<>();
        // arrayCountry.addAll(Arrays.asList(getResources().getStringArray(R.array.array_country)));

        adapter = new ArrayAdapter<>(
                FoodSearchActivity.this,
                android.R.layout.simple_list_item_1,
                arrayCountry);
        lv.setAdapter(adapter);
        databaseReference1 = FirebaseDatabase.getInstance().getReference();
        referencee = databaseReference1;
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(FoodSearchActivity.this, models.get(i).getId(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(FoodSearchActivity.this, FoodNutritiensDisplay.class);
                StringBuilder amm = new StringBuilder();
                amm.append("https://api.nal.usda.gov/ndb/V2/reports?ndbno=");
                amm.append(models.get(i).getId());
                amm.append("&type=f&format=json&api_key=HXLecTDsMqy1Y6jNoYPw2n3DQ30FeGXxD2XBZqJh");
                //new JSONTask().execute(amm.toString());
                intent.putExtra("id",models.get(i).getId());
                intent.putExtra("foodname", models.get(i).getName());

                startActivity(intent);
            }
        });
//
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                referencee.orderByChild("name").startAt(query.toUpperCase()).limitToFirst(5).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        Log.e("Tagg", "ajahaaa");
//                        for(DataSnapshot data : dataSnapshot.getChildren()){
//                            Model model = data.getValue(Model.class);
//                            Log.e("TAG", "onDataChange: "  + model.getName());
//                            String name = model.getName();
//                            arrayCountry.add(name);
//                            tvData.setText(name);
//                        }
//                        ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, arrayCountry);
//                        lv.setAdapter(adapter);
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
               // adapter.getFilter().filter(newText);
                referencee.orderByChild("name").startAt(newText.toUpperCase()).limitToFirst(20).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        arrayCountry.clear();
                        models.clear();
                        Log.e("Tagg", "ajahaaa");
                        for(DataSnapshot data : dataSnapshot.getChildren()){
                            Model model = data.getValue(Model.class);
                            Log.e("TAG", "onDataChange: "  + model.getName());
                            String name = model.getName();
                            arrayCountry.add(model.getId());
                            models.add(model);
                            tvData.setText(name);
                        }
                        ArrayAdapter adapter = new ArrayAdapter(FoodSearchActivity.this, android.R.layout.simple_list_item_1, models);
                        lv.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                return false;


              //  return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
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

            if(s!=null) {
                    //   for (int i = 0; i < s.size(); i++) {
                    //  buffer.append(s.get(i).getId() + "\n" + s.get(i).getName() + "\n" + s.get(i).getOffset() + "\n");
                    //  tvData.setText(s.get(i).getId() + s.get(i).getName() + s.get(i).getOffset() + "\n");
                    // }
                    //   tvData.setText(s.size());w
                    // tvData.setText(s.get(1).getId());
                    }
                    //  Textv.setText(s);

                    aaki =s;

                    }
                    }

                    }


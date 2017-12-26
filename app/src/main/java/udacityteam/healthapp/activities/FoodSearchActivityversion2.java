package udacityteam.healthapp.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import udacityteam.healthapp.adapters.ListAdapter;
import udacityteam.healthapp.models.Model;


public class FoodSearchActivityversion2 extends AppCompatActivity {
    StringBuffer buffer;
    DatabaseReference databaseReference1;
    ListAdapter adapter;
    int amm = 0;
    DatabaseReference referencee;
    ArrayList<String> arrayCountry;
    ArrayList<Model> models;
    ArrayList<Model> models1;
    RecyclerView lv;
    RelativeLayout noresultsdisplay;
    public static String foodselection = null;
    RecyclerView main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_foodsearchactivity2);
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        foodselection = (String) b.get("foodselection");
        main = findViewById(R.id.main);
        noresultsdisplay = findViewById(R.id.noresultsdisplay);

        buffer = new StringBuffer();
        FirebaseApp.initializeApp(this);
        final TextView aaa = (TextView) findViewById(R.id.textView2);
        lv = (RecyclerView) findViewById(R.id.listViewCountry);
        lv.setVisibility(View.INVISIBLE);
        arrayCountry = new ArrayList<>();
        models = new ArrayList<>();
        models1 = new ArrayList<>();
        // arrayCountry.addAll(Arrays.asList(getResources().getStringArray(R.array.array_country)));

        databaseReference1 = FirebaseDatabase.getInstance().getReference("FoodsDatabase");
        referencee = databaseReference1;
//
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        final SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                StringBuilder amm = new StringBuilder();
                //  amm.append("https://api.nal.usda.gov/ndb/V2/reports?ndbno=");
                amm.append("https://api.nal.usda.gov/ndb/search/?format=json&q=");
                amm.append(query);
                amm.append("&sort=n&max=50&offset=0&ds=Standard+Reference&api_key=HXLecTDsMqy1Y6jNoYPw2n3DQ30FeGXxD2XBZqJh");
                //   amm.append("&type=f&format=json&api_key=HXLecTDsMqy1Y6jNoYPw2n3DQ30FeGXxD2XBZqJh");
                main.setVisibility(View.VISIBLE);
                lv.setVisibility(View.INVISIBLE);
                searchView.setQuery("", false);
                lv.setVisibility(View.INVISIBLE);
                new JSONTask1().execute(amm.toString());
//
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                lv.setVisibility(View.VISIBLE);
                // adapter.getFilter().filter(newText);
                // tvData.setText("ahahaaa");
                StringBuilder amm = new StringBuilder();
                //  amm.append("https://api.nal.usda.gov/ndb/V2/reports?ndbno=");
                amm.append("https://api.nal.usda.gov/ndb/search/?format=json&q=");
                amm.append(newText);
                amm.append("&sort=n&max=25&offset=0&ds=Standard+Reference&api_key=HXLecTDsMqy1Y6jNoYPw2n3DQ30FeGXxD2XBZqJh");
                //   amm.append("&type=f&format=json&api_key=HXLecTDsMqy1Y6jNoYPw2n3DQ30FeGXxD2XBZqJh");
                new JSONTask().execute(amm.toString());
                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }


    public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            // return null;
            Log.e("ayaa", "aaaa");
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                String finalJson = buffer.toString();
                JSONObject parentObject = new JSONObject(finalJson);
                JSONObject parentArray = parentObject.getJSONObject("list");
                JSONArray array = parentArray.getJSONArray("item");
                JSONObject finalobjectt = array.getJSONObject(0);
                String name = finalobjectt.getString("name");
                models.clear();
                for (int i = 0; i < array.length(); i++) {

                    JSONObject finalobject = array.getJSONObject(i);
                    Model model = new Model(finalobject.getString("name"), finalobject.getString("offset"),
                            finalobject.getString("ndbno"));
                    models.add(model);
                    Log.e("aaa", String.valueOf(models.size()));
                }

                return finalobjectt.getString("ndbno");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
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
                try {
                    LinearLayoutManager liner = new LinearLayoutManager(FoodSearchActivityversion2.this);
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(lv.getContext(),
                            liner.getOrientation());
                    lv.addItemDecoration(dividerItemDecoration);
                    lv.setLayoutManager(liner);
                    adapter = new ListAdapter(models);
                    lv.setAdapter(adapter);
                    // aki.setText(String.valueOf(models.size()));
                } catch (Exception e) {
                    // aki.setText(s);
                }


            }


        }
    }

        public class JSONTask1 extends AsyncTask<String, String, String> {

            @Override
            protected String doInBackground(String... strings) {
                HttpURLConnection connection = null;
                BufferedReader reader = null;

                // return null;
                Log.e("ayaa", "aaaa");
                try {
                    URL url = new URL(strings[0]);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    InputStream stream = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();

                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }
                    String finalJson = buffer.toString();
                    JSONObject parentObject = new JSONObject(finalJson);
                    JSONObject parentArray = parentObject.getJSONObject("list");
                    JSONArray array = parentArray.getJSONArray("item");
                    JSONObject finalobjectt = array.getJSONObject(0);
                    String name = finalobjectt.getString("name");
                    models1.clear();
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject finalobject = array.getJSONObject(i);
                        Model model = new Model(finalobject.getString("name"), finalobject.getString("offset"),
                                finalobject.getString("ndbno"));
                        models1.add(model);
                        Log.e("aaa", String.valueOf(models1.size()));
                    }

                    return finalobjectt.getString("ndbno");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                    try {
                        if (reader != null) {
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
                    try {
                        LinearLayoutManager liner = new LinearLayoutManager(FoodSearchActivityversion2.this);
                        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(main.getContext(),
                                liner.getOrientation());
                        main.addItemDecoration(dividerItemDecoration);
                        main.setLayoutManager(liner);
                        adapter = new ListAdapter(models1);
                        main.setAdapter(adapter);
                        main.setVisibility(View.VISIBLE);
                        noresultsdisplay.setVisibility(View.GONE);
                        // aki.setText(String.valueOf(models.size()));
                    } catch (Exception e) {
                        // aki.setText(s);
                    }



                }
                else
                {

                }


            }
        }
    }



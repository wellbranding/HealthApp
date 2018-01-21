package udacityteam.healthapp.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
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

import udacityteam.healthapp.R;
import udacityteam.healthapp.adapters.ListAdapter;
import udacityteam.healthapp.models.Model;


public class FoodSearchActivityversion3 extends AppCompatActivity implements SearchView.OnQueryTextListener {
    StringBuffer buffer;
    DatabaseReference databaseReference1;
    ListAdapter adapter;
    int amm = 0;
    DatabaseReference referencee;
    ArrayList<String> arrayCountry;
    String SharedFoodListDatabase;
    private final ArrayList<Model> models = new ArrayList<>();
    ArrayList<Model> models1;
    RecyclerView lv;
    RelativeLayout noresultsdisplay;
    public static String foodselection = null;
    RecyclerView main;
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TERM = "term";
    private static final String DEFAULT = "default";
    private SearchManager searchManager;
    private SearchView searchView;
    TextView message;
    private MenuItem searchMenuItem;
    private SuggestAdapter suggestionsAdapter;
    private final ArrayList<String> dummyArray = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_foodsearchactivity2);
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        foodselection = (String) b.get("foodselection");
        getSupportActionBar().setTitle(foodselection);
        main = findViewById(R.id.main);
        noresultsdisplay = findViewById(R.id.noresultsdisplay);

        SharedFoodListDatabase = (String) b.get("SharedFoodListDatabase");
        buffer = new StringBuffer();
        FirebaseApp.initializeApp(this);
        final TextView aaa = (TextView) findViewById(R.id.textView2);
        lv = (RecyclerView) findViewById(R.id.listViewCountry);
        lv.setVisibility(View.INVISIBLE);
        arrayCountry = new ArrayList<>();
        models1 = new ArrayList<>();
        // arrayCountry.addAll(Arrays.asList(getResources().getStringArray(R.array.array_country)));

        databaseReference1 = FirebaseDatabase.getInstance().getReference("FoodsDatabase");
        referencee = databaseReference1;
//
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        searchMenuItem = menu.findItem(R.id.menuSearch);
        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);
        return  true;

    }
    @Override
    public boolean onQueryTextChange(final String newText) {

        StringBuilder amm = new StringBuilder();
        //  amm.append("https://api.nal.usda.gov/ndb/V2/reports?ndbno=");
        amm.append("https://api.nal.usda.gov/ndb/search/?format=json&q=");
        amm.append(newText);
        amm.append("&sort=r&max=10&offset=10&ds=Standard+Reference&api_key=HXLecTDsMqy1Y6jNoYPw2n3DQ30FeGXxD2XBZqJh");
        //   amm.append("&type=f&format=json&api_key=HXLecTDsMqy1Y6jNoYPw2n3DQ30FeGXxD2XBZqJh");
        new JSONTask().execute(amm.toString());


        return true;
    }

    @Override
    public boolean onQueryTextSubmit(final String query) {
        StringBuilder amm = new StringBuilder();
        //  amm.append("https://api.nal.usda.gov/ndb/V2/reports?ndbno=");

        amm.append("https://api.nal.usda.gov/ndb/search/?format=json&q=");
        amm.append(query);
        amm.append("&sort=r&max=50&offset=0&ds=Standard+Reference&api_key=HXLecTDsMqy1Y6jNoYPw2n3DQ30FeGXxD2XBZqJh");
        //   amm.append("&type=f&format=json&api_key=HXLecTDsMqy1Y6jNoYPw2n3DQ30FeGXxD2XBZqJh");
        main.setVisibility(View.VISIBLE);
        lv.setVisibility(View.INVISIBLE);
        searchView.setQuery("", false);
        lv.setVisibility(View.INVISIBLE);
        new FoodSearchActivityversion3.JSONTask1().execute(amm.toString(), query);
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    private class SuggestAdapter extends CursorAdapter implements View.OnClickListener {

        private final ArrayList<Model> mObjects;
        private final LayoutInflater mInflater;
        private TextView tvSearchTerm;

        public SuggestAdapter(final Context ctx, final Cursor cursor, final ArrayList<Model> mObjects) {
            super(ctx, cursor, 0);

            this.mObjects = mObjects;
            this.mInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View newView(final Context ctx, final Cursor cursor, final ViewGroup parent) {
            final View view = mInflater.inflate(R.layout.text_row_item1, parent, false);


            tvSearchTerm = (TextView) view.findViewById(R.id.textView);

            return view;
        }

        @Override
        public void bindView(final View view, final Context ctx, final Cursor cursor) {

            tvSearchTerm = (TextView) view.findViewById(R.id.textView);

            final int position = cursor.getPosition();

            if (cursorInBounds(position)) {

                final String term = mObjects.get(position).getName();
                tvSearchTerm.setText(term);

                view.setTag(position);
                view.setOnClickListener(this);

            } else {
                // Something went wrong
            }
        }

        private boolean cursorInBounds(final int position) {
            return position < mObjects.size();
        }

        @Override
        public void onClick(final View view) {

            final int position = (Integer) view.getTag();

            if (cursorInBounds(position)) {

                Intent intent = new Intent(FoodSearchActivityversion3.this, FoodNutritiensDisplay.class);
                StringBuilder amm = new StringBuilder();
                amm.append("https://api.nal.usda.gov/ndb/V2/reports?ndbno=");
                amm.append(models.get(position).getId());
                amm.append("&type=f&format=json&api_key=HXLecTDsMqy1Y6jNoYPw2n3DQ30FeGXxD2XBZqJh");
                //new JSONTask().execute(amm.toString());
                intent.putExtra("id", models.get(position).getId());
                intent.putExtra("foodname", models.get(position).getName());
                intent.putExtra("foodselection", FoodSearchActivityversion3.foodselection);

                startActivity(intent);
                Log.d("ama", "Element " + position + " clicked.");

                final String selected = mObjects.get(position).getId();

                Toast.makeText(getApplicationContext(), selected, Toast.LENGTH_SHORT).show();

                // Do something

            } else {
                // Something went wrong
            }
        }
    }

    private MatrixCursor getCursor(final ArrayList<String> suggestions) {

        final String[] columns = new String[] { COLUMN_ID, COLUMN_TERM };
        final Object[] object = new Object[] { 0, DEFAULT };

        final MatrixCursor matrixCursor = new MatrixCursor(columns);

        for (int i = 0; i < suggestions.size(); i++) {

            object[0] = i;
            object[1] = suggestions.get(i);

            matrixCursor.addRow(object);
        }

        return matrixCursor;
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
                dummyArray.clear();

                for (int i = 0; i < array.length(); i++) {

                    JSONObject finalobject = array.getJSONObject(i);
                    Model model = new Model(finalobject.getString("name"), finalobject.getString("offset"),
                            finalobject.getString("ndbno"));
                    models.add(model);
                    dummyArray.add(model.getName());
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
                    Log.d("ahhaa", String.valueOf(models.size()));
                    final MatrixCursor matrixCursor = getCursor(dummyArray);
                    suggestionsAdapter = new SuggestAdapter(FoodSearchActivityversion3.this, matrixCursor, models);
                    searchView.setSuggestionsAdapter(suggestionsAdapter);
                    suggestionsAdapter.notifyDataSetChanged();
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
                String query = strings[1];
                Log.d("taga", query);
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
                if(models.size()==0)
                {
                    StringBuilder amm = new StringBuilder();
                    amm.append("https://api.nal.usda.gov/ndb/search/?format=json&q=");
                    amm.append(query);
                    amm.append("&sort=n&max=50&offset=0&ds=Standard+Reference&api_key=HXLecTDsMqy1Y6jNoYPw2n3DQ30FeGXxD2XBZqJh");
                    //   amm.append("&type=f&format=json&api_key=HXLecTDsMqy1Y6jNoYPw2n3DQ30FeGXxD2XBZqJh");
                    main.setVisibility(View.VISIBLE);
                    lv.setVisibility(View.INVISIBLE);
                    searchView.setQuery("", false);
                    lv.setVisibility(View.INVISIBLE);
                    new FoodSearchActivityversion3.JSONTask1().execute(amm.toString());
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
                    LinearLayoutManager liner = new LinearLayoutManager(FoodSearchActivityversion3.this);
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



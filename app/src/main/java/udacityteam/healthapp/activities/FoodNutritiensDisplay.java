package udacityteam.healthapp.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

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

/**
 * Created by vvost on 11/26/2017.
 */

public class FoodNutritiensDisplay extends AppCompatActivity {
    TextView Textv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foodactivity);
        Textv = (TextView)findViewById(R.id.tv2);
        Intent iin= getIntent();

        Bundle b = iin.getExtras();


        if(b!=null)
        {
            String j =(String) b.get("vardas");
            StringBuilder amm = new StringBuilder();
            amm.append("https://api.nal.usda.gov/ndb/V2/reports?ndbno=");
            amm.append(j);
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

            if(s!=null) {
             //   for (int i = 0; i < s.size(); i++) {
                  //  buffer.append(s.get(i).getId() + "\n" + s.get(i).getName() + "\n" + s.get(i).getOffset() + "\n");
                 //  tvData.setText(s.get(i).getId() + s.get(i).getName() + s.get(i).getOffset() + "\n");
               // }
             //   tvData.setText(s.size());w
               // tvData.setText(s.get(1).getId());
            }
            Textv.setText(s);


        }
    }


}

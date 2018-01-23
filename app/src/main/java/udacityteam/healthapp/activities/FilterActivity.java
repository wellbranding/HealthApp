package udacityteam.healthapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import udacityteam.healthapp.R;

public class FilterActivity extends AppCompatActivity {
    CheckBox calories, fats, carbohydrates, protein;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_filter);
        calories = findViewById(R.id.calories);
        fats = findViewById(R.id.fats);
        carbohydrates = findViewById(R.id.carbohydrates);
        protein = findViewById(R.id.protein);

       Button confirm = findViewById(R.id.confirm);
       confirm.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(calories.isChecked())
               {
                   Intent intent = new Intent(FilterActivity.this, CommunityList.class);
                   intent.putExtra("value", "calories");
                   startActivity(intent);
               }
           }
       });


    }

}

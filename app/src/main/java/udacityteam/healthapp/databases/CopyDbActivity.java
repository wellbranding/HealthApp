package udacityteam.healthapp.databases;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import udacityteam.healthapp.R;

public class CopyDbActivity extends AppCompatActivity {

    Cursor c = null;
    DatabaseHelper myDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        myDbHelper = new DatabaseHelper(CopyDbActivity.this);
//        try {
//            myDbHelper.createDataBase();
//        } catch (IOException ioe) {
//            throw new Error("Unable to create database");
//        }
//        try {
//            myDbHelper.openDataBase();
//        } catch (SQLException sqle) {
//            throw sqle;
//        }
//
//        ((Button) findViewById(R.id.button01)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DatabaseHelper myDbHelper = new DatabaseHelper(CopyDbActivity.this);
//                try {
//                    myDbHelper.openDataBase();
//                } catch (SQLException sqle) {
//                    throw sqle;
//                }
//                c = myDbHelper.query("Ha", null, null, null, null, null, null);
//                if (c.moveToFirst()) {
//                    do {
//                        Toast.makeText(CopyDbActivity.this,
//                                "_id: " + c.getString(0) + "\n" +
//                                        "E_NAME: " + c.getString(1) + "\n" +
//                                        "E_AGE: " + c.getString(2) + "\n" +
//                                        "E_DEPT:  " + c.getString(3),
//                                Toast.LENGTH_LONG).show();
//                    } while (c.moveToNext());
//                }
//                myDbHelper.close();
//            }
//        });
//
//        EditText good  = (EditText) findViewById(R.id.good);
//        ContentValues ai = new ContentValues();
//      //  ai.put("_id", 3);
//        ai.put("E_NAME", "ahahhaha");
//        ai.put("E_AGE", "hahaaa");
//        ai.put("E_DEPT", "aaa");
//
//        ContentValues a1 = new ContentValues();
//        a1.put("Name", "bybys");
//        a1.put("Name1", "aaaaz");
//        a1.put("Name2", "aaazz");
//
//
//          SQLiteDatabase aa = myDbHelper.myDataBase;
//          aa.insert("EMP_TABLE", null, ai);
//          aa.insert("Ha", null, a1);

    }


}

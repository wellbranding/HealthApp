package udacityteam.healthapp.activities;

/**
 * Created by vvost on 12/3/2017.
 */

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

import udacityteam.healthapp.R;
import udacityteam.healthapp.models.SelectedFood;
import udacityteam.healthapp.models.User;

public class SignupActivity extends AppCompatActivity {

    MaterialEditText edtPhone,edtName,edtPassword;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtPhone = (MaterialEditText)findViewById(R.id.edtPhone);
        edtName = (MaterialEditText)findViewById(R.id.edtName);
        edtPassword = (MaterialEditText)findViewById(R.id.edtPassword);

        btnSignUp = (Button) findViewById(R.id.btnSignUp);

        //Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog mDialog = new ProgressDialog(SignupActivity.this);
                mDialog.setMessage("Please Wait...");
                mDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //check if user phone is already registered
                        if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                            mDialog.dismiss();
                            Toast.makeText(SignupActivity.this, "Phone Number Already Registered", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            mDialog.dismiss();
                            ArrayList<SelectedFood> Breakfast = new ArrayList<>();
                           ArrayList<SelectedFood> Dinner= new ArrayList<>();
                         ArrayList<SelectedFood> Drinks = new ArrayList<>();
                            ArrayList<SelectedFood> Lunch= new ArrayList<>();
                            ArrayList<SelectedFood> Snacks= new ArrayList<>();
                            User user = new User(edtName.getText().toString(),edtPassword.getText().toString(), Breakfast, Dinner, Drinks, Lunch, Snacks);
                       //     User user = new User(edtName.getText().toString(),edtPassword.getText().toString());
                            table_user.child(edtPhone.getText().toString()).setValue(user);
                            Toast.makeText(SignupActivity.this, "Sign Up SuccessFull !", Toast.LENGTH_SHORT).show();
                            mDialog.dismiss();
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}

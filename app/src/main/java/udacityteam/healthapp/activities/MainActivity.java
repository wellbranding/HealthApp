package udacityteam.healthapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

import udacityteam.healthapp.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String ANONYMOUS = "anonymous";

    public static final int RC_SIGN_IN = 1;

    // Firebase instance variables
    private FirebaseDatabase mFirebaseDatabase;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase components
        mFirebaseAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    onSignedInInitialize(user.getDisplayName());
                } else {
                    // User is signed out
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(
                                            Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))

                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fabsettings = findViewById(R.id.fabSetting);
        Snacks = (LinearLayout) this.findViewById(R.id.Snacks);
        Drinks = (LinearLayout) this.findViewById(R.id.Drinks);
        Breakfast = (LinearLayout) this.findViewById(R.id.Breakfast);
        Dinner = (LinearLayout) this.findViewById(R.id.Dinner);
        Lunch = (LinearLayout) this.findViewById(R.id.Lunch);
      //  layoutFabEdit = (LinearLayout) this.findViewById(R.id.layoutFabEdit);
     //   layoutFabPhoto = (LinearLayout) this.findViewById(R.id.layoutFabPhoto);
        fabsettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              // Intent intent = new Intent(MainActivity.this, FoodSearchActivity.class);
             //  startActivity(intent);
                if (fabExpanded == true){
                    closeSubMenusFab();
                } else {
                    openSubMenusFab();
                }
            }
        });
      //  closeSubMenusFab();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    private void onSignedInInitialize(String username) {
        mUsername = username;
    }

    private void onSignedOutCleanup() {
        mUsername = ANONYMOUS;
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_sign_out:
                AuthUI.getInstance().signOut(this);
             default:
                 return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_breakfasts: {
                Intent intent = new Intent(this, Main2Activity.class);
                Bundle extras = intent.getExtras();
                intent.putExtra("titlename", "Community Breakfasts");
                startActivity(intent);
                // Handle the camera action
                break;
            }
            case R.id.nav_dinners: {
                Intent intent = new Intent(this, Main2Activity.class);
                Bundle extras = intent.getExtras();
                intent.putExtra("titlename", "Community Dinners");
                startActivity(intent);


                break;
            }
            case R.id.nav_lunches: {
                Intent intent = new Intent(this, Main2Activity.class);
                Bundle extras = intent.getExtras();
                intent.putExtra("titlename", "Community Lunches");
                startActivity(intent);

                break;
            }
            case R.id.nav_community_daily_diets: {
                Intent intent = new Intent(this, Main2Activity.class);
                Bundle extras = intent.getExtras();
                intent.putExtra("titlename", "Community Daily Diet Plan");
                startActivity(intent);

                break;
            }
            case R.id.nav_snacks: {
                Intent intent = new Intent(this, Main2Activity.class);
                Bundle extras = intent.getExtras();
                intent.putExtra("titlename", "Snacks");
                startActivity(intent);

                break;
            }
            case R.id.nav_drinks_cocktails: {
                Intent intent = new Intent(this, Main2Activity.class);
                Bundle extras = intent.getExtras();
                intent.putExtra("titlename", "Drinks/Cocktails");
                startActivity(intent);
                break;
            }
            default:
                Log.e(TAG, "NavigationItemSelected not handled properly");
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

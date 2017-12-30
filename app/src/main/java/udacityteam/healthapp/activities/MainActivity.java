package udacityteam.healthapp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import udacityteam.healthapp.R;
import udacityteam.healthapp.adapters.CustomAdapter;
import udacityteam.healthapp.models.User;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String ANONYMOUS = "anonymous";

    public static final int RC_SIGN_IN = 1;

    FloatingActionButton fabsettings;
    @BindView(R.id.calendarView)
    MaterialCalendarView widget;


    // Firebase instance variables
    private FirebaseDatabase mFirebaseDatabase;
    private ChildEventListener mChildEventListener;
   // public static FirebaseAuth mFirebaseAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private boolean fabExpanded = false;
    private LinearLayout Snacks;
    private LinearLayout Drinks;
    private LinearLayout Breakfast;
    private LinearLayout Dinner;
    private LinearLayout Lunch;
    private Button dinnerbtn;
    private Button lunchbtn;
    private Button breakfastbtn;
    private Button snacksbtn;
    private Button dailybtn;
    private Button drinksbtn;
    Calendar today;


    private String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase components
     //   mFirebaseAuth = FirebaseAuth.getInstance();


//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken("1025887070439-pa8ivq2h24eigj8vv8h66e43ng7fgefh.apps.googleusercontent.com")
//                .requestEmail()
//                .build();
//
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//
//        // [START initialize_auth]
//
//        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user != null) {
//                    // User is signed in
//                    onSignedInInitialize(user.getDisplayName());
//                    onSignedInInitialize(user.getEmail());
//                    Log.d("newuser", user.getDisplayName());
//                  // User newuser = new User(edtName.getText().toString(),edtPassword.getText().toString(), Breakfast, Dinner, Drinks, Lunch, Snacks);
//                } else {
//                    // User is signed out
//                    onSignedOutCleanup();
//                    startActivityForResult(
//                            AuthUI.getInstance()
//                                    .createSignInIntentBuilder()
//                                    .setIsSmartLockEnabled(false)
//                                    .setAvailableProviders(
//                                            Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
//                                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
//
//                                    .build(),
//                            RC_SIGN_IN);
//                }
//            }
//        };
        setContentView(R.layout.activity_main);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        Log.d("aryra",mAuth.getCurrentUser().getUid().toString() );
        Date date = new Date();
        Date newDate = new Date(date.getTime());
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
       final String stringdate = dt.format(newDate);
        mFirebaseDatabase.getReference("User").child(FirebaseAuth.getInstance().
                getCurrentUser().getUid()).child("LastLogin").setValue(stringdate);
        mFirebaseDatabase.getReference("User").child(FirebaseAuth.getInstance().
                getCurrentUser().getUid()).child("LastLogin").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = (String) dataSnapshot.getValue();
                if(value!=stringdate) {
                    mFirebaseDatabase.getReference("User").child(FirebaseAuth.getInstance().
                            getCurrentUser().getUid()).child("Dinner"+"isshared").setValue(false);
                    mFirebaseDatabase.getReference("User").child(FirebaseAuth.getInstance().
                            getCurrentUser().getUid()).child("Lunch"+"isshared").setValue(false);
                    mFirebaseDatabase.getReference("User").child(FirebaseAuth.getInstance().
                            getCurrentUser().getUid()).child("Breakfast"+"isshared").setValue(false);
                    mFirebaseDatabase.getReference("User").child(FirebaseAuth.getInstance().
                            getCurrentUser().getUid()).child("Snacks"+"isshared").setValue(false);
                    mFirebaseDatabase.getReference("User").child(FirebaseAuth.getInstance().
                            getCurrentUser().getUid()).child("Drinks"+"isshared").setValue(false);

                }

                // do your stuff here with value
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        getSupportActionBar().setTitle("MainActivity");

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
                if (fabExpanded == true) {
                    closeSubMenusFab();
                } else {
                    openSubMenusFab();
                }
            }
        });
        closeSubMenusFab();
        Toolbar toolbar = findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);


        //  closeSubMenusFab();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        calendarinit();
        initRecyclerview();
        initButton();



    }

    private void initButton()
    {
       dinnerbtn= this.findViewById(R.id.btndinner);

       lunchbtn = this.findViewById(R.id.btnlunch);
       breakfastbtn = this.findViewById(R.id.btnbreakfast);
       snacksbtn = this.findViewById(R.id.btnscancks);
       drinksbtn = this.findViewById(R.id.btndrinks);
       dailybtn = this.findViewById(R.id.btndaily);
//        Date datee = new Date();
//        Calendar calendarr = Calendar.getInstance();
//        calendarr.setTime(datee);
//        SimpleDateFormat dateFormat = new SimpleDateFormat(
//                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, 6);
//        calendar.set(Calendar.MINUTE,0);
        widget.getSelectedDate();
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(widget.getSelectedDate().getDate());
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println();
        Log.d("ajaaz", format.format(calendar.getTime()));
        dinnerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FoodList.class);
                intent.putExtra("foodselection", "Dinner");
                intent.putExtra("requestdate", format.format(calendar.getTime()));
                startActivity(intent);
            }
        });
        lunchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FoodList.class);
                intent.putExtra("foodselection", "Lunch");
                intent.putExtra("requestdate", format.format(calendar.getTime()));
                startActivity(intent);
            }
        });
        breakfastbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FoodList.class);
                intent.putExtra("foodselection", "Breakfast");
                intent.putExtra("requestdate", format.format(calendar.getTime()));
                startActivity(intent);
            }
        });
        snacksbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FoodList.class);
                intent.putExtra("foodselection", "Snacks");
                intent.putExtra("requestdate", format.format(calendar.getTime()));
                startActivity(intent);
            }
        });
        drinksbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FoodList.class);
                intent.putExtra("foodselection", "Drinks");
                intent.putExtra("requestdate", format.format(calendar.getTime()));
                startActivity(intent);
            }
        });
        dailybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Currently not Available", Toast.LENGTH_SHORT).show();
            }
        });


    }
    private void initRecyclerview()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference foodList = database.getReference("MainFeed").child("Breakfast").child("SharedDiets");

        final RecyclerView mRecyclerView = findViewById(R.id.recyclerView);
        final ProgressBar progressBar = findViewById(R.id.progressbar);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        //  listodydis.setText(foodList.orderByChild("userId").toString());

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        foodList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Getting current user Id

                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


                // Filter User
                List<String> userList = new ArrayList<>();
//                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                            Log.d("shhss", "ahahha");
//                         //   listodydis.setText();
//                            if (!dataSnapshot1.getValue(SelectedFood.class).getUserId().equals(uid)) {
//                                userList.add(dataSnapshot1.getValue(SelectedFood.class));
//                            }
//                        }
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Log.d("shhss", dataSnapshot1.getKey().toString());
                    progressBar.setVisibility(View.GONE);
                    //   listodydis.setText();

                    if (!dataSnapshot1.getKey().equals(uid)) {
                        userList.add(dataSnapshot1.getKey());
                    }
                }
                progressBar.setVisibility(View.GONE);


               CustomAdapter mAdapter = new CustomAdapter(userList);
                mRecyclerView.setAdapter(mAdapter);


                // Setting d

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
                //listodydis.setText("errrooas");
            }
        });
    }

    private void calendarinit()
    {
        Date date = new Date();
        Date newDate = new Date(date.getTime());
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        ButterKnife.bind(this);

        widget.state().edit()
                .setCalendarDisplayMode(CalendarMode.WEEKS)
                .commit();
        widget.setShowOtherDates(MaterialCalendarView.SHOW_OTHER_MONTHS);


        Calendar beginning = Calendar.getInstance();
      // today.setTime(date);
        beginning.set(beginning.get(Calendar.YEAR), beginning.get(Calendar.MONTH), beginning.get(Calendar.DAY_OF_MONTH)-10);
        Log.d("aaa", beginning.toString());
       today = Calendar.getInstance();
       today.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
        widget.setCurrentDate(today);

        //  widget.setSelectionMode();
        Calendar end = Calendar.getInstance();
        end.set(end.get(Calendar.YEAR),  end.get(Calendar.MONTH), end.get(Calendar.DAY_OF_MONTH)+10);

        widget.setArrowColor(this.getResources().getColor(R.color.colorPrimary));
        widget.setSelectionColor(this.getResources().getColor(R.color.colorPrimary));
        widget.setSelectedDate(today);
        widget.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                initButton();
            }
        });

        Calendar minimum = Calendar.getInstance();
       minimum.set(minimum.get(Calendar.YEAR),minimum.get(Calendar.MONTH), minimum.get(Calendar.DAY_OF_MONTH)-50);
        Calendar maximum = Calendar.getInstance();
        maximum.set(maximum.get(Calendar.YEAR),maximum.get(Calendar.MONTH), maximum.get(Calendar.DAY_OF_MONTH)+50);

        widget.state().edit()
                .setMinimumDate(minimum.getTime())
                .setMaximumDate(maximum.getTime())
                .commit();
    }

    private void closeSubMenusFab(){
        //  layoutFabSave.setVisibility(View.INVISIBLE);
        Snacks.setVisibility(View.INVISIBLE);
        Drinks.setVisibility(View.INVISIBLE);
        Breakfast.setVisibility(View.INVISIBLE);
        Dinner.setVisibility(View.INVISIBLE);
        Lunch.setVisibility(View.INVISIBLE);
        fabsettings.setImageResource(R.drawable.ic_settings_black_24dp);
        fabExpanded = false;
    }

    //Opens FAB submenus
    private void openSubMenusFab(){
        // layoutFabSave.setVisibility(View.VISIBLE);
        Snacks.setVisibility(View.VISIBLE);
        Drinks.setVisibility(View.VISIBLE);
        Breakfast.setVisibility(View.VISIBLE);
        Dinner.setVisibility(View.VISIBLE);
        Lunch.setVisibility(View.VISIBLE);
        Drinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "ahahaa", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, FoodSearchActivityversion3.class);
                intent.putExtra("foodselection", "Drinks");
                startActivity(intent);

            }
        });
        Snacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "ooooo", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, FoodSearchActivityversion3.class);
                intent.putExtra("foodselection", "Snacks");
                startActivity(intent);
            }
        });
        Breakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "tttttt", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, FoodSearchActivityversion3.class);
                intent.putExtra("foodselection", "Breakfast");
                startActivity(intent);
            }
        });
        Dinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "tttttt", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, FoodSearchActivityversion3.class);
                intent.putExtra("foodselection", "Dinner");
                startActivity(intent);
            }
        });
        Lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "tttttt", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, FoodSearchActivityversion3.class);
                intent.putExtra("foodselection", "Lunch");
                startActivity(intent);
            }
        });
        //Change settings icon to 'X' icon
        fabsettings.setImageResource(R.drawable.ic_close_black_24dp);
        fabExpanded = true;
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
      //  mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
          //  mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
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
                ReturntoRegister();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void ReturntoRegister()
    {
        AuthUI.getInstance().signOut(this)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                public void onComplete(@NonNull Task<Void> task) {
                    // user is now signed out
                      Intent intent = new Intent(MainActivity.this, Register.class);
                    intent.putExtra("offline", true);
                    startActivity(intent);
                    finish();
                }
            });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_breakfasts) {
            Intent intent = new Intent(this, CommunityList.class);
            Bundle extras = intent.getExtras();
            intent.putExtra("titlename", "Community Breakfasts");
            intent.putExtra("databasevalue", "Breakfast");
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.nav_dinners) {
            Intent intent = new Intent(this, CommunityList.class);
            Bundle extras = intent.getExtras();
            intent.putExtra("titlename", "Community Dinners");
            intent.putExtra("databasevalue", "Dinner");
            startActivity(intent);


        } else if (id == R.id.nav_lunches) {
            Intent intent = new Intent(this, CommunityList.class);
            Bundle extras = intent.getExtras();
            intent.putExtra("titlename", "Community Lunches");
            intent.putExtra("databasevalue", "Lunch");
            startActivity(intent);

        } else if (id == R.id.nav_community_daily_diets) {
            Intent intent = new Intent(this, CommunityList.class);
            Bundle extras = intent.getExtras();
            intent.putExtra("titlename", "Community Daily Diet Plan");
            Toast.makeText(this, "Currently Not Available", Toast.LENGTH_SHORT).show();
            //startActivity(intent);

        } else if (id == R.id.nav_snacks) {
            Intent intent = new Intent(this, CommunityList.class);
            Bundle extras = intent.getExtras();
            intent.putExtra("titlename", "Snacks");
            intent.putExtra("databasevalue", "Snacks");
            startActivity(intent);

        } else if (id == R.id.nav_drinks_cocktails) {
            Intent intent = new Intent(this, CommunityList.class);
            Bundle extras = intent.getExtras();
            intent.putExtra("titlename", "Drinks/Coctails");
            intent.putExtra("databasevalue", "Drinks");
            startActivity(intent);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

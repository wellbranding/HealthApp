package udacityteam.healthapp.activities;

/**
 * Created by vvost on 12/29/2017.
 */
import android.app.Fragment;
import android.app.FragmentManager;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import udacityteam.healthapp.Model.Userretrofit;
import udacityteam.healthapp.R;


/**
 * Demonstrate Firebase Authentication using a Google ID Token.
 */
public class BaseActivity extends AppCompatActivity implements
        View.OnClickListener {

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private static final String BACK_STACK_ROOT_TAG_LOGIN = "login";

    // [START declare_auth]
    private FirebaseAuth mAuth;

    // [END declare_auth]

    private GoogleSignInClient mGoogleSignInClient;

    @Inject
    ViewModelProvider.Factory ViewModelFactory;

    LoginRegisterViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1025887070439-pa8ivq2h24eigj8vv8h66e43ng7fgefh.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity_home);
        findViewById(R.id.sign_in_button).setOnClickListener(this);

        viewModel = ViewModelProviders.of(this, ViewModelFactory).
                get(LoginRegisterViewModel.class);

        Button registermail = findViewById(R.id.mailregister);
        Button loginmail = findViewById(R.id.mailogin);


        registermail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RegisterWithMailFragment fragment = new RegisterWithMailFragment();
                android.support.v4.app.FragmentManager fragmentManager = BaseActivity.this.getSupportFragmentManager();
                fragmentManager.popBackStack(BACK_STACK_ROOT_TAG_LOGIN, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragmentManager.beginTransaction().
                        replace(R.id.fragmentContainer, fragment)
                        .addToBackStack(BACK_STACK_ROOT_TAG_LOGIN)
                        .commit();
                fragmentManager.executePendingTransactions();
//                Intent intent = new Intent(BaseActivity.this, RegisterWithMailPasword.class);
//                startActivity(intent);
            }
        });
        loginmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BaseActivity.this, LoginWithMailPasword.class);
                startActivity(intent);
            }
        });

    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    // [END on_start_check_user]

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        final FirebaseUser user = mAuth.getCurrentUser();
                        // Sign in success, update UI with the signed-in user's information
                        if(user!=null) {

                            Userretrofit retrofituser = new Userretrofit(user.getDisplayName(), user.getEmail(), mAuth.getCurrentUser().getUid());
                            viewModel.getRegisterWithGoogleSignInResponse(retrofituser).observe(this, result->
                            {
                                if(result!=null)
                                if(!result.getError())
                                {
                                    Intent intent = new Intent(BaseActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else
                                {
                                    Toast.makeText(this, result.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                    } else {
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                }
            });}

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }



    private void updateUI(FirebaseUser user) {
        if (user != null && user.isEmailVerified()) {
            Intent intent = new Intent(BaseActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else
            if(user!=null && !user.isEmailVerified())
            {
               //TODO
                //Possibly add Verification button in MainActivity
            }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.sign_in_button) {
            signIn();
        }

    }
}



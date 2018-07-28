package udacityteam.healthapp.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Objects;
import java.util.zip.Inflater;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import udacityteam.healthapp.PHP_Retrofit_API.APIService;
import udacityteam.healthapp.PHP_Retrofit_API.APIUrl;
import udacityteam.healthapp.Model.Result;
import udacityteam.healthapp.Model.Userretrofit;
import udacityteam.healthapp.R;


public class RegisterWithMailFragment extends Fragment implements
        View.OnClickListener {

    private static final String TAG = "EmailPassword";

    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private EditText mEmailField;
    private EditText mPasswordField;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_register_with_mail_pasword,
                container, false);
        mStatusTextView = view.findViewById(R.id.status);
        mDetailTextView = view.findViewById(R.id.detail);
        mEmailField = view.findViewById(R.id.field_email);
        mPasswordField = view.findViewById(R.id.field_password);

        view.findViewById(R.id.email_create_account_button).setOnClickListener(this);
         view.findViewById(R.id.verify_email_button).setOnClickListener(this);
        view.findViewById(R.id.signed_in_buttons).setVisibility(View.GONE);
        return view;
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    // [END on_start_check_user]

    private void createAccount(final String email, final String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();

                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        try {
                            throw Objects.requireNonNull(task.getException());
                        } catch(FirebaseAuthWeakPasswordException e) {
                            Toast.makeText(requireActivity(), "The Password is too short",
                                    Toast.LENGTH_SHORT).show();

                        } catch(FirebaseAuthInvalidCredentialsException e) {
                            Toast.makeText(requireActivity(), "The Email  is badly formatted",
                                    Toast.LENGTH_SHORT).show();

                        } catch(FirebaseAuthUserCollisionException e) {
                            Toast.makeText(requireActivity(), "The Email is already in Use",
                                    Toast.LENGTH_SHORT).show();

                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }

                        updateUI(null);
                    }

                });
    }



    private void sendEmailVerification() {
        requireActivity().findViewById(R.id.verify_email_button).setEnabled(false);
        final FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(requireActivity(), new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            requireActivity().findViewById(R.id.verify_email_button).setEnabled(true);

                            if (task.isSuccessful()) {
                                Toast.makeText(requireActivity(),
                                        "Verification email sent to " + user.getEmail(),
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(requireActivity(), LoginWithMailPasword.class);
                                startActivity(intent);
                            }
                        }
                    });
        }
        else
        {
            Toast.makeText(requireActivity(),
                    "Could not find a registration request",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            requireActivity().findViewById(R.id.email_password_buttons).setVisibility(View.GONE);
            requireActivity().findViewById(R.id.email_password_fields).setVisibility(View.GONE);
            requireActivity().findViewById(R.id.signed_in_buttons).setVisibility(View.VISIBLE);
            requireActivity().findViewById(R.id.verify_email_button).setEnabled(!user.isEmailVerified());


        } else {
            mStatusTextView.setText("sitnout");
            mDetailTextView.setText(null);
            requireActivity().findViewById(R.id.email_password_buttons).setVisibility(View.VISIBLE);
            requireActivity().findViewById(R.id.email_password_fields).setVisibility(View.VISIBLE);
            requireActivity().findViewById(R.id.signed_in_buttons).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.email_create_account_button) {
            createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());

        } else if (i == R.id.verify_email_button) {
            sendEmailVerification();
        }
    }
}

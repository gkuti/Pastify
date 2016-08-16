package com.gamik.pastify.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.gamik.pastify.R;
import com.gamik.pastify.model.DataItem;
import com.gamik.pastify.model.User;
import com.gamik.pastify.store.Store;
import com.gamik.pastify.util.Validator;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;

import java.util.List;

public class SigninActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    LinearLayout linearLayout;
    TextInputLayout confrimPasswordInputLayout, emailInputLayout, passwordInputLayout, passwordRecoveryInputLayout;
    EditText emailEditText, passwordEditText, confirmPasswordEditText, passwordRecoveryEditText;
    Button signInButton, signUpButton;
    int mode = 0;
    FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    FirebaseAuth.AuthStateListener authStateListener;
    GoogleApiClient mGoogleApiClient;
    boolean flag = true;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        firebaseAuth = FirebaseAuth.getInstance();
        getCurrentUser();
        signInButton = (Button) findViewById(R.id.bt_sign_in);
        signUpButton = (Button) findViewById(R.id.bt_sign_up);
        emailInputLayout = (TextInputLayout) findViewById(R.id.til_email);
        confrimPasswordInputLayout = (TextInputLayout) findViewById(R.id.til_confirm_password);
        passwordInputLayout = (TextInputLayout) findViewById(R.id.til_password);
        // passwordRecoveryInputLayout = (TextInputLayout) findViewById(R.id.til_password_recovery);
        emailEditText = (EditText) findViewById(R.id.et_email);
        passwordEditText = (EditText) findViewById(R.id.et_password);
        confirmPasswordEditText = (EditText) findViewById(R.id.et_confirm_password);
        //passwordRecoveryEditText = (EditText) findViewById(R.id.et_password_recovery);
        emailEditText.addTextChangedListener(new MyTextWatcher(emailEditText));
        passwordEditText.addTextChangedListener(new MyTextWatcher(passwordEditText));
        confirmPasswordEditText.addTextChangedListener(new MyTextWatcher(confirmPasswordEditText));
        //passwordRecoveryEditText.addTextChangedListener(new MyTextWatcher(passwordRecoveryEditText));
        //linearLayout = (LinearLayout) findViewById(R.id.ll_password_recovery);
        TextView textView = (TextView) findViewById(R.id.textView3);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "arial_rounded_bold.ttf");
        textView.setTypeface(typeface);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("12196201887-1mh9jnkv633c79siaad4kesbooptk8cs.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 1) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        //Log.d(, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            //Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(SigninActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });

    }

    public void signInWithGoogle(View view) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, 1);
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    private void getCurrentUser() {
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null && flag) {
                    flag = false;
                    List<UserInfo> list = (List<UserInfo>) user.getProviderData();
                    UserInfo userInfo = list.get(1);
                    String info = userInfo.getPhotoUrl().toString();
                    User currentUser = new User(user.getUid(), info, user.getEmail());
                    Intent intent = new Intent(getBaseContext(), UserAccountActivity.class);
                    intent.putExtra("user", currentUser);
                    startActivity(intent);
                    finish();
                }
            }
        }

        ;
    }

    public void signUp(View view) {
        if (mode == 0) {
            YoYo.with(Techniques.Landing).duration(1000).playOn(findViewById(R.id.ll_form));
            confrimPasswordInputLayout.setVisibility(View.VISIBLE);
            signUpButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            signUpButton.setTextColor(getResources().getColor(R.color.white));
            signInButton.setTextColor(getResources().getColor(R.color.colorAccent));
            signInButton.setBackgroundColor(getResources().getColor(R.color.whitish));
            mode = 1;
        } else {
            int check = 0;
            if (validateEmail()) {
                check++;
            }
            if (validatePassword()) {
                check++;
            }
            if (validateConfirmPassword()) {
                check++;
            }
            if (check == 3) {
                CreateUser();
            }
        }
    }

    private void CreateUser() {
        progressDialog = ProgressDialog.show(this, "Creating Account", "Please wait ...");
        firebaseAuth.createUserWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (!task.isSuccessful()) {
                            String[] error = task.getException().toString().split("Exception: ");
                            Toast.makeText(SigninActivity.this, error[1], Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void signIn(View view) {
        if (mode != 1) {
            int check = 0;
            if (validateEmail()) {
                check++;
            }
            if (validatePassword()) {
                check++;
            }
            if (check == 2) {
                SignInUser();
            }

        } else {
            YoYo.with(Techniques.Landing).duration(1000).playOn(findViewById(R.id.ll_form));
            confrimPasswordInputLayout.setVisibility(View.GONE);
            signInButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            signInButton.setTextColor(getResources().getColor(R.color.white));
            signUpButton.setTextColor(getResources().getColor(R.color.colorAccent));
            signUpButton.setBackgroundColor(getResources().getColor(R.color.whitish));
            mode = 0;
        }
    }

    private void SignInUser() {
        progressDialog = ProgressDialog.show(this, "Creating Account", "Please wait ...");
        firebaseAuth.signInWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            progressDialog.dismiss();
                            String[] error = task.getException().toString().split("Exception: ");
                            Toast.makeText(SigninActivity.this, error[1], Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean validateEmail() {
        if (emailEditText.getText().toString().trim().isEmpty()) {
            emailInputLayout.setError("Enter email address");
            return false;
        } else if (!Validator.isValidEmail(emailEditText.getText().toString())) {
            emailInputLayout.setError("Invalid email address");
            return false;
        }
        emailInputLayout.setErrorEnabled(false);
        return true;
    }

    private boolean validatePassword() {
        if (passwordEditText.getText().toString().trim().isEmpty() || passwordEditText.getText().toString().trim().length() < 6) {
            passwordInputLayout.setError("Password is too short");
            return false;
        } else if (!Validator.isValidPassword(passwordEditText.getText().toString())) {
            passwordInputLayout.setError("Most contain letters and numbers without spaces");
            return false;
        }
        passwordInputLayout.setErrorEnabled(false);
        return true;
    }

    private boolean validateConfirmPassword() {
        if (confirmPasswordEditText.getText().toString().trim().isEmpty() || !confirmPasswordEditText.getText().toString().trim().equals(passwordEditText.getText().toString().trim())) {
            confrimPasswordInputLayout.setError("Password does not match");
            return false;
        }
        confrimPasswordInputLayout.setErrorEnabled(false);
        return true;
    }

    private boolean validatePasswordRecovery() {
        if (passwordRecoveryEditText.getText().toString().trim().isEmpty()) {
            passwordRecoveryInputLayout.setError("Answer cannot be empty");
            return false;
        }
        passwordRecoveryInputLayout.setErrorEnabled(false);
        return true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            validator();
        }

        public void afterTextChanged(Editable editable) {
            validator();
        }

        void validator() {
            switch (view.getId()) {
                case R.id.et_email:
                    validateEmail();
                    break;
                case R.id.et_confirm_password:
                    validateConfirmPassword();
                    break;
                case R.id.et_password:
                    validatePassword();
                    break;
            }
        }
    }
}

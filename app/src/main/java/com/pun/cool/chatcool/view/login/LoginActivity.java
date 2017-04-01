package com.pun.cool.chatcool.view.login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pun.cool.chatcool.R;
import com.pun.cool.chatcool.view.register.RegisterActivity;
import com.pun.cool.chatcool.view.room.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.emailView)
    AutoCompleteTextView emailView;

    @BindView(R.id.passwordView)
    EditText passwordView;

    @BindView(R.id.loginButton)
    Button loginButton;

    @BindView(R.id.registerButton)
    Button registerButton;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initFireBase();
        initInstance();
    }

    private void initFireBase() {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    goToMain();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    private void initInstance() {
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);

        passwordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    login();
                }

                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void goToMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void goToRegister() {
        startActivity(new Intent(this, RegisterActivity.class));
        finish();
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    private boolean validate() {
        emailView.setError(null);
        passwordView.setError(null);

        if (TextUtils.isEmpty(passwordView.getText())) {
            passwordView.setError("null");
            passwordView.requestFocus();
            return true;
        }

        if (TextUtils.isEmpty(emailView.getText())) {
            emailView.setError("null");
            emailView.requestFocus();
            return true;
        } else if (!emailView.getText().toString().contains("@")) {
            emailView.setError("@");
            emailView.requestFocus();
            return true;
        }

        return false;
    }

    private void login() {
        if (validate()) return;

        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();

        showProgress();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        hideProgress();

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(getApplicationContext(), R.string.auth_failed, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        goToMain();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v == loginButton) {
            login();
        } else if (v == registerButton) {
            goToRegister();
        }
    }
}

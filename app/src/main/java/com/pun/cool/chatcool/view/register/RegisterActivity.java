package com.pun.cool.chatcool.view.register;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.pun.cool.chatcool.R;
import com.pun.cool.chatcool.view.room.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RegisterActivity";

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.emailView)
    EditText emailView;

    @BindView(R.id.passwordView)
    EditText passwordView;

    @BindView(R.id.passwordConfirmView)
    EditText passwordConfirmView;

    @BindView(R.id.registerButton)
    Button registerButton;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        initFireBase();
        initInstance();
    }

    private void initFireBase() {
        mAuth = FirebaseAuth.getInstance();
    }

    private void initInstance() {
        registerButton.setOnClickListener(this);
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    private void goToMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private boolean validate() {
        emailView.setError(null);
        passwordView.setError(null);
        passwordConfirmView.setError(null);

        if (TextUtils.isEmpty(passwordConfirmView.getText())) {
            passwordConfirmView.setError("null");
            passwordConfirmView.requestFocus();
            return true;
        }

        if (TextUtils.isEmpty(passwordView.getText())) {
            passwordView.setError("null");
            passwordView.requestFocus();
            return true;
        }

        if (!passwordView.getText().toString().equals(passwordConfirmView.getText().toString())) {
            passwordView.setError("not match");
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

    private void register() {
        if (validate()) return;

        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();

        showProgress();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), R.string.auth_failed, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        hideProgress();
                        goToMain();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v == registerButton) {
            register();
        }
    }
}

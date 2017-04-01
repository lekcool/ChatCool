package com.pun.cool.chatcool.view.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.pun.cool.chatcool.R;
import com.pun.cool.chatcool.view.room.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileNameActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ProfileNameActivity";

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.nameView)
    EditText nameView;

    @BindView(R.id.updateButton)
    Button updateButton;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_name);
        ButterKnife.bind(this);

        initFireBase();
        initInstance();
    }

    private void initFireBase() {
        mAuth = FirebaseAuth.getInstance();
    }

    private void initInstance() {
        updateButton.setOnClickListener(this);
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
        if (TextUtils.isEmpty(nameView.getText())) {
            nameView.setError("null");
            nameView.requestFocus();
            return true;
        }

        return false;
    }

    private void updateProfile() {
        if (validate()) return;

        String name = nameView.getText().toString();


        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            showProgress();
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build();

            user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        mAuth.getCurrentUser().reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Log.e(TAG, "name: " + user.getDisplayName());

                                    hideProgress();
                                    goToMain();
                                } else {
                                    hideProgress();
                                    Toast.makeText(getApplicationContext(), R.string.auth_name_failed, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        hideProgress();
                        Toast.makeText(getApplicationContext(), R.string.auth_name_failed, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }

    @Override
    public void onClick(View v) {
        if (v == updateButton) {
            updateProfile();
        }
    }
}

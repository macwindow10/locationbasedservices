package com.home.locationbasedservices;

import android.os.Bundle;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class RegistrationActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private Button button;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.edittext_email);
        editTextPassword = findViewById(R.id.edittext_password);
        button = findViewById(R.id.button_register);
        progressBar = findViewById(R.id.progressbar);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser();
            }
        });
    }

    private void registerNewUser() {
        progressBar.setVisibility(View.VISIBLE);

        // Take the value of two edit texts in Strings
        String email, password;
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();

        // Validations for input email and password
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter email",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter password!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),
                                    "Registration successful!",
                                    Toast.LENGTH_LONG)
                                    .show();

                            progressBar.setVisibility(View.GONE);

                            Intent intent = new Intent(RegistrationActivity.this,
                                    MainActivity.class);
                            startActivity(intent);
                        } else {

                            Toast.makeText(
                                    getApplicationContext(),
                                    "Registration failed"
                                            + " Please try again later",
                                    Toast.LENGTH_LONG)
                                    .show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}


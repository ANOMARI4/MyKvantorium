package com.mykvantorium;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editText_email, editText_password;
    Button button_auth;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        } else {
            setContentView(R.layout.activity_login);
            editText_email = findViewById(R.id.editText_email);
            editText_password = findViewById(R.id.editText_password);
            button_auth = findViewById(R.id.button_auth);
            button_auth.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        String email = "0";
        String password = "0";
        if (editText_email.getText().toString().equals("") || editText_password.getText().toString().equals(""))
            ;
        else {
            email = editText_email.getText().toString();
            password = editText_password.getText().toString();
        }
        switch (v.getId()) {
            case R.id.button_auth:
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(0, 0);
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Неверная почта или пароль.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
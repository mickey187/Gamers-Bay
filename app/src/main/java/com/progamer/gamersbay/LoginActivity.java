package com.progamer.gamersbay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    Button login;
    MaterialButton signup;
    TextInputEditText email;
    TextInputEditText password;
    private FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    String userID;
    String token_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.login);
        signup = findViewById(R.id.signup_button_borderless);
        email = findViewById(R.id.email_login_textField);
        password = findViewById(R.id.login_password_textField);
        mAuth = FirebaseAuth.getInstance();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_login = email.getText().toString().trim();
                String password_login = password.getText().toString().trim();
                if (TextUtils.isEmpty(email_login)){
                    email.setError("please enter your email address");
                    return;
                }

                if (TextUtils.isEmpty(password_login)){
                    password.setError("please enter your password");
                    return;
                }

                mAuth.signInWithEmailAndPassword(email_login, password_login).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            FirebaseUser user = mAuth.getCurrentUser();
                            userID = user.getUid();

                            token_number = FirebaseInstanceId.getInstance().getToken();


                            Toast.makeText(LoginActivity.this, "Authentication successful", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                        }

                        else {
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}
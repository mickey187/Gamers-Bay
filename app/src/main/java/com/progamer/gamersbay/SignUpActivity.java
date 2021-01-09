package com.progamer.gamersbay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    TextInputEditText fullname;
    TextInputEditText email_address;
    TextInputEditText user_password;
    TextInputEditText phone_number;
    Button signup;
    int balance = 0;


    private FirebaseAuth mAuth;
    private static final String TAG = "DocSnippets";
    String userID;
    String token_number;

    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        fullname = findViewById(R.id.textField_full_name);
        phone_number = findViewById(R.id.textField_phone_number_signUp);
        email_address = findViewById(R.id.textField_email);
        user_password = findViewById(R.id.textField_password_signUp);
        signup = findViewById(R.id.signup);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = email_address.getText().toString().trim();
                String password = user_password.getText().toString().trim();
                final String fName = fullname.getText().toString();
                String phone_no = phone_number.getText().toString().trim();


                if (TextUtils.isEmpty(fName)){
                    fullname.setError("first name is required");
                    return;
                }


                if (TextUtils.isEmpty(email)){
                    email_address.setError("Email is required");
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    user_password.setError("password is required");
                    return;
                }

                if (TextUtils.isEmpty(phone_no)){
                    user_password.setError("phone number is required");
                    return;
                }

                if (password.length() < 8){

                    user_password.setError("password length must be greater then eight characters");
                    return;
                }

                if (phone_number.length() < 10){

                    user_password.setError("missing digits");
                    return;
                }



                final Map<String, Object> user = new HashMap<>();
                user.put("Full name",fName);
                user.put("Email",email);
                user.put("Balance",balance);
                user.put("Phone number",phone_no);


                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){

                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            userID = firebaseUser.getUid();

                          /*  db.collection("Users").document(userID)
                                    .set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot successfully written!");
                                        }


                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding document", e);
                                }
                            });

                           */

                            Intent phone = new Intent(getApplicationContext(),PhoneVerification.class);
                            phone.putExtra("phone","+"+phone_number.getText().toString());
                            phone.putExtra("user data",(Serializable) user);
                            startActivity(phone);
                            Log.d(TAG, "onComplete: "+"+"+phone_number.getText().toString());
                        }

                        else {
                            Toast.makeText(SignUpActivity.this, "sign up failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
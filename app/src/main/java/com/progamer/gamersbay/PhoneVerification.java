package com.progamer.gamersbay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class PhoneVerification extends AppCompatActivity {

    private static final String TAG = "TAG" ;
    EditText otp_1;
    EditText otp_2;
    EditText otp_3;
    EditText otp_4;
    EditText otp_5;
    EditText otp_6;

    Button verifyPhone;
    Button resendOTP;

    Boolean otpValid;

    FirebaseAuth firebaseAuth;
    PhoneAuthCredential phoneAuthCredential;
    PhoneAuthProvider.ForceResendingToken token;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks changedCallbacks;

    String verificationId;
    String phone;
    private String userID;
    Map user = new HashMap<>();
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);

        otp_1 = findViewById(R.id.otpNumberOne);
        otp_2 = findViewById(R.id.optNumberTwo);
        otp_3 = findViewById(R.id.otpNumberThree);
        otp_4 = findViewById(R.id.otpNumberFour);
        otp_5 = findViewById(R.id.otpNumberFive);
        otp_6 = findViewById(R.id.optNumberSix);

        verifyPhone = findViewById(R.id.verifyPhoneBTn);
        resendOTP = findViewById(R.id.resendOTP);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        Intent data = getIntent();
        phone = data.getStringExtra("phone");
        user = (Map)data.getSerializableExtra("user data");

        verifyPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateField(otp_1);
                validateField(otp_2);
                validateField(otp_3);
                validateField(otp_4);
                validateField(otp_5);
                validateField(otp_6);

                if (otpValid){


                    String otp = otp_1.getText().toString()+otp_2.getText().toString()+otp_3.getText().toString()+otp_4.getText()
                            .toString()+otp_5.getText().toString()+otp_6.getText().toString();

                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId,otp);
                    verifyAuthentication(phoneAuthCredential);

                }
            }
        });

        changedCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                verificationId = s;
                token = forceResendingToken;

                resendOTP.setVisibility(View.GONE);
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);

                resendOTP.setVisibility(View.VISIBLE);
            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                verifyAuthentication(phoneAuthCredential);
                resendOTP.setVisibility(View.GONE);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                Toast.makeText(PhoneVerification.this, "Verification failed", Toast.LENGTH_SHORT).show();

            }
        };

        sendOTP(phone);



        resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendOTP(phone);
            }
        });
    }

    private void verifyAuthentication(PhoneAuthCredential phoneAuthCredential) {

        Objects.requireNonNull(firebaseAuth.getCurrentUser()).linkWithCredential(phoneAuthCredential).addOnSuccessListener(authResult -> {

            Toast.makeText(PhoneVerification.this, "Account created", Toast.LENGTH_SHORT).show();

            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            userID = firebaseUser.getUid();

            db.collection("Users").document(userID)
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

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });

    }

    public void sendOTP(String phoneNumber){

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(changedCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
      //  PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber,60, TimeUnit.SECONDS,this,changedCallbacks);
    }

    public void resendOTP(String phoneNumber){


        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(changedCallbacks)
                        .setForceResendingToken(token)// OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
       // PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber,60, TimeUnit.SECONDS,this,changedCallbacks,token);
    }

    public void validateField(EditText field){

        if (field.getText().toString().isEmpty()){

            field.setError("Required");
            otpValid = false;
        }
        else
            otpValid = true;

    }
}
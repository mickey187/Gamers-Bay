package com.progamer.gamersbay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneVerification extends AppCompatActivity {

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

        Intent data = getIntent();
        phone = data.getStringExtra("phone");

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

        firebaseAuth.getCurrentUser().linkWithCredential(phoneAuthCredential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                Toast.makeText(PhoneVerification.this, "Account created", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }

    public void sendOTP(String phoneNumber){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber,60, TimeUnit.SECONDS,this,changedCallbacks);
    }

    public void resendOTP(String phoneNumber){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber,60, TimeUnit.SECONDS,this,changedCallbacks,token);
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
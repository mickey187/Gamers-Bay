package com.progamer.gamersbay;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class TopUpActivity extends AppCompatActivity {

    // intialization of buttos
    private TextView userBalance, userName, userEmail;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up);

        userBalance = findViewById(R.id.topup_user_balance);
        userName = findViewById(R.id.topup_user_name);
        userEmail = findViewById(R.id.topup_user_email);


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        userId = user.getUid();

        final DocumentReference documentReference = db.collection("Users").document(userId);
        documentReference.addSnapshotListener( new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                userName.setText(documentSnapshot.getString("Full name"));
                userEmail.setText(documentSnapshot.getString("Email"));
                userBalance.setText(documentSnapshot.getLong("Balance").toString()+" ETB");
//                phone_number.setText("Phone number: "+documentSnapshot.getString("Phone number"));
            }
        });



    }


    public void launchCbeTopUp(View view) {


    }
}
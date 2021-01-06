package com.progamer.gamersbay;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class JoinPubgMatch extends AppCompatActivity {
    TextInputEditText in_game_name;
    TextInputEditText in_game_Id;
    Button submit;
    FirebaseFirestore firestore;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String userID;
    String full_name;
    String in_game_username;
    String gaming_Id;

    String phone_number;
    String match_name;
    long entry_fee;

    DocumentReference documentReference;
    DocumentReference documentReference_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_pubg_match);

        in_game_name = findViewById(R.id.textField_in_game_name_pubg);
        in_game_Id = findViewById(R.id.textField_in_game_Id);
        submit = findViewById(R.id.submit_pubg);

        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userID = user.getUid();
        Intent data = getIntent();
        match_name = data.getStringExtra("match_name");

         documentReference = firestore.collection("Users").document(userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                full_name = documentSnapshot.getString("Full name");
                phone_number = documentSnapshot.getString("Phone number");



            }
        });
         documentReference_1 = firestore.collection("pubg_matches").document(match_name);
         documentReference_1.addSnapshotListener(new EventListener<DocumentSnapshot>() {
             @Override
             public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {

                 entry_fee = documentSnapshot.getLong("entrance_fee");
             }
         });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                in_game_username = in_game_name.getText().toString().toString();
                gaming_Id = in_game_Id.getText().toString().trim();
                Map<String, Object> user_details = new HashMap<>();

                user_details.put("Full name", full_name);
                user_details.put("Phone number", phone_number);
                user_details.put("in_game_user_name", in_game_username);
                user_details.put("gaming_id", gaming_Id);
                user_details.put("match_name", match_name);

                firestore.collection("pubg_list").document(full_name).collection("matches").document(match_name)
                         .set(user_details)
                         .addOnSuccessListener(new OnSuccessListener<Void>() {
                             @Override
                             public void onSuccess(Void aVoid) {

                                 firestore.collection("pubg_matches").document(match_name)
                                          .update(
                                                  "players_that_joined", FieldValue.increment(1)
                                 );

                                 firestore.collection("Users").document(userID).update("Balance",FieldValue.increment(-entry_fee));
                                 Toast.makeText(JoinPubgMatch.this, "submitted successfully", Toast.LENGTH_SHORT).show();
                             }
                         }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(JoinPubgMatch.this, "failed to submit", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


    }
}
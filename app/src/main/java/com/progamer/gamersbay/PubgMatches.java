package com.progamer.gamersbay;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class PubgMatches extends AppCompatActivity {

    private static final String TAG = "TAG" ;
    private FirebaseFirestore firestore;
    private RecyclerView pubg_match_list_firestore;
    private FirestoreRecyclerAdapter adapter;
    DocumentReference documentReference;
    DocumentReference documentReference_1;

    String userID;
    int  balance;
    int  entry_fee;
    FirebaseAuth mAuth;
    Boolean balance_checker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pubg_matches);

        pubg_match_list_firestore = findViewById(R.id.pubg_match_list_recycler);
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        userID = user.getUid();




        documentReference = firestore.collection("Users").document(userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {

                balance = documentSnapshot.getLong("Balance").intValue();
            }
        });



        Query query = firestore.collection("pubg_matches");
        FirestoreRecyclerOptions<PubgMatchesModel> options = new FirestoreRecyclerOptions.Builder<PubgMatchesModel>()
                .setQuery(query, PubgMatchesModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<PubgMatchesModel, PubgViewHolder>(options) {
            @NonNull
            @Override
            public PubgViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pubg_match_list,parent,false);
                return new PubgViewHolder(view);
            }


            @Override
            protected void onBindViewHolder(@NonNull PubgViewHolder holder, int position, @NonNull PubgMatchesModel model) {

                holder.match_description.setText("Match Description: "+model.getMatch_description());
                holder.game_map.setText("Map: "+model.getGame_map());
                holder.match_date.setText(model.getMatch_date());
                holder.reward.setText(String.valueOf(model.getReward())+" ETB");
                holder.match_time.setText(model.getMatch_time());
                holder.type.setText("Type: "+model.getType());
                entry_fee =   model.getEntrance_fee();
                holder.join_match.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                       if (balance >= model.getEntrance_fee()){
                           Intent intent = new Intent(getApplicationContext(), JoinPubgMatch.class);
                           intent.putExtra("match_name",model.getMatch_name());
                           startActivity(intent);
                       }
                       else {
                           Toast.makeText(PubgMatches.this, "your balance is low", Toast.LENGTH_SHORT).show();
                       }
                    }
                });

                holder.num_of_players_joined.setText(String.valueOf(model.getPlayers_that_joined())+"/"+String.valueOf(model.getMaximum_number_of_players()));
                holder.player_counter.setProgress(model.getPlayers_that_joined());
                holder.player_counter.setMax(model.getMaximum_number_of_players());
                holder.entrance_fee.setText(String.valueOf(model.getEntrance_fee())+" ETB");
                holder.match_number.setText(model.getMatch_name());


            }


        };

        pubg_match_list_firestore.setHasFixedSize(true);
        pubg_match_list_firestore.setLayoutManager(new LinearLayoutManager(this));
        pubg_match_list_firestore.setAdapter(adapter);

    }




    private class PubgViewHolder extends RecyclerView.ViewHolder {

        private TextView match_description;
        private TextView game_map;
        private TextView match_date;
        private TextView reward;
        private TextView match_time;
        private TextView type;
        private TextView num_of_players_joined;
        private Button join_match;
        private ProgressBar player_counter;
        private TextView entrance_fee;
        private TextView match_number;
        CardView cardView;


        public PubgViewHolder(@NonNull View itemView) {
            super(itemView);

            match_description = itemView.findViewById(R.id.match_description);
            game_map = itemView.findViewById(R.id.game_map_textview);
            match_date = itemView.findViewById(R.id.match_date);
            reward = itemView.findViewById(R.id.match_reward);
            match_time = itemView.findViewById(R.id.match_time);
            type = itemView.findViewById(R.id.match_type_textview);
            join_match = itemView.findViewById(R.id.join_match_button);
            player_counter = itemView.findViewById(R.id.determinateBar);
            num_of_players_joined = itemView.findViewById(R.id.num_of_players_joined);
            entrance_fee = itemView.findViewById(R.id.entrance_fee);
            match_number = itemView.findViewById(R.id.match_number);
            cardView = itemView.findViewById(R.id.cardview_recycler);

            

        }




    }

    public void checkBalance(){

         firestore.collection("pubg_matches").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
             @Override
             public void onComplete(@NonNull Task<QuerySnapshot> task) {

                 if (task.isSuccessful()){

                     for (QueryDocumentSnapshot documentSnapshot: task.getResult()){
                         Map<String, Object> match_data = documentSnapshot.getData();
                        // entry_fee = match_data.get("entrance_fee").toString();
                         Toast.makeText(PubgMatches.this, entry_fee, Toast.LENGTH_SHORT).show();
                     }
                 }
             }
         });

    }

    @Override
    protected void onStop() {
        super.onStop();

        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();

        adapter.startListening();
    }
}
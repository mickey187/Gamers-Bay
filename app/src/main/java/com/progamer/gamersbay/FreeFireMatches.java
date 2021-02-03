package com.progamer.gamersbay;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

public class FreeFireMatches extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    private FirebaseFirestore firestore;
    private RecyclerView free_fire_match_list_firestore;
    private FirestoreRecyclerAdapter adapter;

    String userID;
    int  balance;
    int  entry_fee;
    FirebaseAuth mAuth;
    DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setUpSharedPreferences();
        setContentView(R.layout.activity_free_fire_matches);

        free_fire_match_list_firestore = findViewById(R.id.free_fire_match_list_recycler);
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

        Query query = firestore.collection("free_fire_matches").orderBy("match_name");
        FirestoreRecyclerOptions<FreeFireModel> options = new FirestoreRecyclerOptions.Builder<FreeFireModel>()
                .setQuery(query, FreeFireModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<FreeFireModel, FreeFireMatches.FreeFireViewHolder>(options) {
            @NonNull
            @Override
            public FreeFireMatches.FreeFireViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.free_fire_match_list,parent,false);
                return new FreeFireViewHolder(view);
            }


            @Override
            protected void onBindViewHolder(@NonNull FreeFireMatches.FreeFireViewHolder holder, int position, @NonNull FreeFireModel model) {

                holder.match_description.setText("Match Description: "+model.getMatch_description());
                holder.game_map.setText("Map: "+model.getGame_map());
                holder.match_date.setText("Match Date: "+model.getMatch_date());
                holder.reward.setText(String.valueOf("Reward: "+model.getReward())+" ETB");
                holder.match_time.setText("Match Time: "+model.getMatch_time());
                holder.type.setText("Type: "+model.getType());
                holder.match_status.setText("Match status: "+model.getMatch_status());
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
                            Toast.makeText(FreeFireMatches.this, "your balance is low", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                holder.num_of_players_joined.setText(String.valueOf(model.getPlayers_that_joined())+"/"+String.valueOf(model.getMaximum_number_of_players()));
                holder.player_counter.setProgress(model.getPlayers_that_joined());
                holder.player_counter.setMax(model.getMaximum_number_of_players());
                holder.entrance_fee.setText(String.valueOf("Entrance Fee: "+model.getEntrance_fee())+" ETB");
                holder.match_number.setText(model.getMatch_name());


            }


        };

        free_fire_match_list_firestore.setHasFixedSize(true);
        free_fire_match_list_firestore.setLayoutManager(new LinearLayoutManager(this));
        free_fire_match_list_firestore.setAdapter(adapter);


    }




    private class FreeFireViewHolder extends RecyclerView.ViewHolder {

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
        private TextView match_status;
        CardView cardView;


        public FreeFireViewHolder(@NonNull View itemView) {
            super(itemView);

            match_description = itemView.findViewById(R.id.match_description_free_fire);
            game_map = itemView.findViewById(R.id.game_map_textview_free_fire);
            match_date = itemView.findViewById(R.id.match_date_free_fire);
            reward = itemView.findViewById(R.id.match_reward_free_fire);
            match_time = itemView.findViewById(R.id.match_time_free_fire);
            type = itemView.findViewById(R.id.match_type_textview_free_fire);
            join_match = itemView.findViewById(R.id.join_match_button_free_fire);
            player_counter = itemView.findViewById(R.id.determinateBar_free_fire);
            num_of_players_joined = itemView.findViewById(R.id.num_of_players_joined_free_fire);
            entrance_fee = itemView.findViewById(R.id.entrance_fee_free_fire);
            match_number = itemView.findViewById(R.id.match_number_free_fire);
            match_status = itemView.findViewById(R.id.match_status_free_fire);
            cardView = itemView.findViewById(R.id.cardview_recycler_free_fire_matches);



        }




    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.dark_mode))){
            loadDarkModeFromPreference(sharedPreferences);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }

    private void loadDarkModeFromPreference(SharedPreferences sharedPreferences) {

        if (sharedPreferences.getBoolean(getString(R.string.dark_mode),false)){
            setTheme(R.style.darkTheme);

        }
    }

    public void setUpSharedPreferences(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        loadDarkModeFromPreference(sharedPreferences);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
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

    @Override
    protected void onResume() {
        super.onResume();

        PreferenceManager.getDefaultSharedPreferences(this);
    }
}
package com.progamer.gamersbay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class PubgMatches extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private RecyclerView pubg_match_list_firestore;
    private FirestoreRecyclerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pubg_matches);

        pubg_match_list_firestore = findViewById(R.id.pubg_match_list_recycler);
        firestore = FirebaseFirestore.getInstance();

        Query query = firestore.collection("pubg_matches");
        FirestoreRecyclerOptions<PubgMatchesModel> options =new FirestoreRecyclerOptions.Builder<PubgMatchesModel>()
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
                holder.match_date.setText("Match Date: "+model.getMatch_date());
                holder.reward.setText("Reward: "+model.getReward());
                holder.match_time.setText("Match Time: "+model.getMatch_time());
                holder.type.setText("Type: "+model.getType());
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

        public PubgViewHolder(@NonNull View itemView) {
            super(itemView);

            match_description = itemView.findViewById(R.id.match_description);
            game_map = itemView.findViewById(R.id.game_map_textview);
            match_date = itemView.findViewById(R.id.match_date);
            reward = itemView.findViewById(R.id.match_reward);
            match_time = itemView.findViewById(R.id.match_time);
            type = itemView.findViewById(R.id.match_type_textview);
        }
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
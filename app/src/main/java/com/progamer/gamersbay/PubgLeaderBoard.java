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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class PubgLeaderBoard extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private RecyclerView pubg_leaderboard_list_firestore;
    private FirestoreRecyclerAdapter adapter;
    DocumentReference documentReference;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pubg_leader_board);

        pubg_leaderboard_list_firestore = findViewById(R.id.pubg_leaderboard_list_recycler);

        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();


        Query query = firestore.collection("pubg_leaderboard").orderBy("Rank");

        FirestoreRecyclerOptions<PubgLeaderboardModel> options = new FirestoreRecyclerOptions.Builder<PubgLeaderboardModel>()
                .setQuery(query, PubgLeaderboardModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<PubgLeaderboardModel, PubgLeaderBoard.PubgLeadeboardViewHolder>(options){


            @NonNull
            @Override
            public PubgLeadeboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pubg_leaderboard_list,parent,false);
                return new PubgLeadeboardViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull PubgLeadeboardViewHolder holder, int position, @NonNull PubgLeaderboardModel model) {

                holder.ID.setText("ID: "+String.valueOf(model.getID()));
                holder.Rank.setText(String.valueOf(model.getRank()));
                holder.Wins.setText("Wins: "+String.valueOf(model.getWins()));
                holder.Games.setText("Games: "+String.valueOf(model.getGames()));
                holder.Kills.setText("Kills: "+String.valueOf(model.getKills()));
            }
        };

        pubg_leaderboard_list_firestore.setHasFixedSize(true);
        pubg_leaderboard_list_firestore.setLayoutManager(new LinearLayoutManager(this));
        pubg_leaderboard_list_firestore.setAdapter(adapter);
    }

    private class PubgLeadeboardViewHolder extends RecyclerView.ViewHolder{

        private TextView ID;
        private TextView Games;
        private TextView Wins;
        private TextView Kills;
        private TextView Rank;


        public PubgLeadeboardViewHolder(@NonNull View itemView) {
            super(itemView);

            ID = itemView.findViewById(R.id.pubg_id);
            Games = itemView.findViewById(R.id.number_of_games);
            Wins = itemView.findViewById(R.id.number_of_wins);
            Kills = itemView.findViewById(R.id.number_of_kills);
            Rank = itemView.findViewById(R.id.pubg_rank_textview);
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
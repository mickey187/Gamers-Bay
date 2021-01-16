package com.progamer.gamersbay.notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.iid.FirebaseInstanceId;
import com.progamer.gamersbay.R;

public class NotificationsFragment extends Fragment {
    public static final String TAG = "TEST";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notificationRef;
    private RecyclerViewNotificationAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        System.out.println("TAG: "+FirebaseInstanceId.getInstance().getToken());

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();
        Toast.makeText(getActivity(), userID, Toast.LENGTH_SHORT).show();
        notificationRef =  db.collection("NotificationTest").document(userID).collection("notification");
        notificationRef.document(userID).get();
        Query query = notificationRef.orderBy("notificationTimeStamp",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<NotificationModel> options = new FirestoreRecyclerOptions.Builder<NotificationModel>()
                .setQuery(query, NotificationModel.class)
                .build();
        adapter = new RecyclerViewNotificationAdapter(options);
        RecyclerView recyclerView = view.findViewById(R.id.notification_recyclerview_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.deleteItem(viewHolder.getAdapterPosition());
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);


        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
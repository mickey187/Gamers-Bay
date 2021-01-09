package com.progamer.gamersbay;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AccountFragment extends Fragment implements DialogClass.DialogClassListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // Intializiation
    private Button btn_topup;

    TextView username;
    TextView email;
    TextView balance;
    TextView phone_number;

    String userID;

    FirebaseAuth mAuth;
    FirebaseFirestore firestore;

    // created a new FirebaseAuth, FirebaseFirestore, and FirebaseUser objects. I couldn't access the one created above.
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuthObj= FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuthObj.getCurrentUser();
    String userUid = currentUser.getUid();


    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        username = view.findViewById(R.id.user_name);
        email = view.findViewById(R.id.user_email);
        balance = view.findViewById(R.id.user_balance);
//        phone_number = view.findViewById(R.id.textview_phone_number);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        userID = user.getUid();

        final DocumentReference documentReference = firestore.collection("Users").document(userID);

        documentReference.addSnapshotListener( new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                username.setText(documentSnapshot.getString("Full name"));
                email.setText(documentSnapshot.getString("Email"));
                balance.setText(String.valueOf(documentSnapshot.get("Balance")+" ETB"));
//                phone_number.setText("Phone number: "+documentSnapshot.getString("Phone number"));
            }
        });

        // mannzzy touch
        btn_topup = view.findViewById(R.id.topup);

        btn_topup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                    Decided to use pop up rather than an activity
                 */
//                Intent toTopup = new Intent(view.getContext(), TopUpActivity.class);
//                startActivity(toTopup);

                openDialog();
            }
        });

        return view;
    }

    private void openDialog() {
        DialogClass dialogClass = new DialogClass();
        dialogClass.show(getActivity().getSupportFragmentManager(), "TopUp dialog");
    }

    @Override
    public void applyTexts(String phonenum, String amount, String option) {

        String date = new SimpleDateFormat("dd-M-yyyy hh:mm:ss").format(new Date());
        Map<String,String> mdata = new HashMap<>();
        mdata.put("paymentOption", option);
        mdata.put("date", date);
        mdata.put("phone",phonenum);
        mdata.put("amount",amount);
        addtoDb(mdata);



    }

    public void addtoDb(Map m){
//        final DocumentReference dr = firestore.collection("Topup").document(userID);
        String date = new SimpleDateFormat("dd-M-yyyy hh:mm:ss").format(new Date());


        db.collection("Topup").document(userUid + "---" + date).set(m);
    }
}
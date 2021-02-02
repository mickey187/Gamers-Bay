package com.progamer.gamersbay;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static final String TAG = "test";

    String url_pubg;
    String url_codm;
    String  url_freefire;

    ImageView pubg_imageview;
    ImageView freefire_imageview;
    ImageView codm_imageview;

    Button browse_pubg_matches;
    Button browse_freefire_matches;
    Button browse_codm_matches;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference gsReference_pubg = storage.getReferenceFromUrl("gs://gamers-bay-4958c.appspot.com/PUBG_wallpaper.jpg");
        StorageReference gsReference_freefire = storage.getReferenceFromUrl("gs://gamers-bay-4958c.appspot.com/FREEFIRE_wallpaper.jpg");
        StorageReference gsReference_codm = storage.getReferenceFromUrl("gs://gamers-bay-4958c.appspot.com/CODM_wallpaper.jpg");


        pubg_imageview = view.findViewById(R.id.pubg_imageview);
        freefire_imageview = view.findViewById(R.id.freefire_imageview);
        codm_imageview = view.findViewById(R.id.codm_imageview);

        browse_pubg_matches = view.findViewById(R.id.browse_pubg_matches);
        browse_freefire_matches = view.findViewById(R.id.browse_freefire_matches);
        browse_codm_matches = view.findViewById(R.id.browse_codm_matches);

        url_pubg = "gs://gamers-bay-4958c.appspot.com/PUBG_wallpaper.jpg";
        url_codm = "gs://gamers-bay-4958c.appspot.com/CODM_wallpaper.jpg";
        url_freefire = "https://wallpapercave.com/wp/wp3144211.jpg";


      GlideApp.with(this).load(gsReference_pubg).into(pubg_imageview);
      GlideApp.with(this).load(gsReference_freefire).into(freefire_imageview);
      GlideApp.with(this).load(gsReference_codm).into(codm_imageview);




        browse_pubg_matches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), PubgMatches.class);
                startActivity(intent);
            }
        });

        browse_freefire_matches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(),FreeFireMatches.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
package de.android.apptemplate2.Fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.android.apptemplate2.MainActivity;
import de.android.apptemplate2.R;

public class NeighborHome extends Fragment implements View.OnClickListener {
    // Store instance variables
    private String title;
    private int page;
    private Typeface tf;

    public static NeighborHome newInstance(int page, String title) {
        NeighborHome fragmentFirst = new NeighborHome();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/coffeetininitials.ttf");
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_neighbor_home, container, false);
        TextView tvLabel = view.findViewById(R.id.tvLabel);
        TextView tvOptions = view.findViewById(R.id.tvOptions);
        tvOptions.setOnClickListener(this);
        TextView tvGame = view.findViewById(R.id.tvGame);
        tvGame.setOnClickListener(this);

        tvOptions.setTypeface(tf);
        tvGame.setTypeface(tf);
        tvLabel.setTypeface(tf);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvGame:
                MainActivity.movePager(page,true);
                break;
            case R.id.tvOptions:
                MainActivity.movePager(page,false);
                break;
        }
    }
}
package de.android.apptemplate2.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import de.android.apptemplate2.NeighborGameActivity;
import de.android.apptemplate2.R;

import static android.content.Context.MODE_PRIVATE;

public class NeighborReadyFragment extends Fragment implements View.OnClickListener {

    private Typeface tf;
    private SharedPreferences pref;
    private SharedPreferences.Editor edit;

    private TextView tvNumberQuestion, tvDuplicateNumbers, tvNumNeighbors, tvBoard,
            tvVisibleDigits, tvHighScore, tvRndNeighbors, tvRightAnswers;

    private Button btnStart;

    // newInstance constructor for creating fragment with arguments
    public static NeighborReadyFragment newInstance(int page, String title) {
        NeighborReadyFragment fragmentFirst = new NeighborReadyFragment();
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/cthulhumbus.ttf");
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_neighbor_ready, container, false);

        tvNumberQuestion = view.findViewById(R.id.tvNumQuestions);
        tvNumberQuestion.setTypeface(tf);
        tvDuplicateNumbers = view.findViewById(R.id.tvDuplicateNumbers);
        tvDuplicateNumbers.setTypeface(tf);
        tvNumNeighbors = view.findViewById(R.id.tvNumNeighbors);
        tvNumNeighbors.setTypeface(tf);
        tvBoard = view.findViewById(R.id.tvBoard);
        tvBoard.setTypeface(tf);
        tvVisibleDigits = view.findViewById(R.id.tvVisibleDigits);
        tvVisibleDigits.setTypeface(tf);
        tvHighScore = view.findViewById(R.id.tvHighScore);
        tvHighScore.setTypeface(tf);
        tvRndNeighbors = view.findViewById(R.id.tvRndNeighbors);
        tvRndNeighbors.setTypeface(tf);
        tvRightAnswers = view.findViewById(R.id.tvRightAnswers);
        tvRightAnswers.setTypeface(tf);

        ((TextView) view.findViewById(R.id.tv1)).setTypeface(tf);
        ((TextView) view.findViewById(R.id.tv2)).setTypeface(tf);
        ((TextView) view.findViewById(R.id.tv3)).setTypeface(tf);
        ((TextView) view.findViewById(R.id.tv4)).setTypeface(tf);
        ((TextView) view.findViewById(R.id.tv5)).setTypeface(tf);
        ((TextView) view.findViewById(R.id.tv6)).setTypeface(tf);
        ((TextView) view.findViewById(R.id.tv7)).setTypeface(tf);
        ((TextView) view.findViewById(R.id.tv8)).setTypeface(tf);
        ((TextView) view.findViewById(R.id.tv9)).setTypeface(tf);
        ((TextView) view.findViewById(R.id.tv10)).setTypeface(tf);


        btnStart = view.findViewById(R.id.btnStart);
        btnStart.setTypeface(tf);
        btnStart.setOnClickListener(this);

        pref = getActivity().getSharedPreferences("prefs",MODE_PRIVATE);
        edit = pref.edit();

        return view;
    }

    public void initTextViews(){
        if(pref.getBoolean("frBoard", true))
            tvBoard.setText("FR");
        else
            tvBoard.setText("US");

        if(pref.getBoolean("rndNeighbors", false))
            tvRndNeighbors.setText("YES");
        else
            tvRndNeighbors.setText("NO");

        if(pref.getBoolean("repeatNums", false))
            tvDuplicateNumbers.setText("YES");
        else
            tvDuplicateNumbers.setText("NO");

        if(pref.getBoolean("showDigits", true))
            tvVisibleDigits.setText("YES");
        else
            tvVisibleDigits.setText("NO");

        tvNumberQuestion.setText(""+pref.getInt("nums", 1));
        tvNumNeighbors.setText(""+pref.getInt("neighbors", 1));

        tvHighScore.setText(pref.getString("neighborHighscore","-"));
        tvRightAnswers.setText(""+pref.getInt("neighborRightAnswers",0));

        edit.putBoolean("neighborOptionsChanges",true).apply();

    }

    @Override
    public void onResume(){
        super.onResume();

        if(!pref.getBoolean("neighborOptionsChanges",false))
            initTextViews();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnStart:
                Intent intent = new Intent(getActivity(), NeighborGameActivity.class);
                startActivity(intent);
                break;
        }
    }
}
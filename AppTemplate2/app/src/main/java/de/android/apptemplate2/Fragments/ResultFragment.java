package de.android.apptemplate2.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import de.android.apptemplate2.R;

import static android.content.Context.MODE_PRIVATE;


public class ResultFragment extends Fragment implements View.OnClickListener {

    private TextView tvTime, tvRightAnswers, tvWrongAnswers;
    private Button btnDone;

    private SharedPreferences pref;
    private SharedPreferences.Editor edit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_neighbor_result, container, false);

        tvTime = (TextView) rootView.findViewById(R.id.tvTime);
        tvRightAnswers = (TextView) rootView.findViewById(R.id.tvRightAnswers);
        tvWrongAnswers = (TextView) rootView.findViewById(R.id.tvWrongAnswers);

        btnDone = (Button) rootView.findViewById(R.id.btnDone);
        btnDone.setOnClickListener(this);

        pref = getActivity().getSharedPreferences("prefs",MODE_PRIVATE);
        edit = pref.edit();

        return rootView;
    }

    @Override
    public void onClick(View v) {
        //Intent intent = new Intent(getContext(), OptionsActivity.class);
        //startActivity(intent);
        getActivity().finish();
    }

    public void setResults(int rightAnswers, int wrongAnswers, long different){

        long timeinMillis = different;

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        String result = "over a day";

        if(elapsedDays > 0)
            tvTime.setText(result);
        else
            result = String.format("%02d", elapsedHours)+":"+ String.format("%02d", elapsedMinutes)+":"+
                    String.format("%02d", elapsedSeconds);

        tvTime.setText(result);
        tvRightAnswers.setText(""+rightAnswers);
        tvWrongAnswers.setText(""+wrongAnswers);


        //save Highscore if better
        if(rightAnswers >= pref.getInt("neighborRightAnswers",0))
        if(timeinMillis < pref.getLong("neighborHighscoreNumber", 90000000)) {
            edit.putLong("neighborHighscoreNumber",different).apply();
            edit.putString("neighborHighscore", result).apply();
            edit.putInt("neighborRightAnswers", rightAnswers).apply();
            edit.putBoolean("neighborOptionsChanges", false).apply();
        }
    }
}

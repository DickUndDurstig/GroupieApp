package de.android.apptemplate2.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import com.nex3z.togglebuttongroup.SingleSelectToggleGroup;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import de.android.apptemplate2.MainActivity;
import de.android.apptemplate2.R;

import static android.content.Context.MODE_PRIVATE;

public class NeighborOptions extends Fragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private CheckBox cbRepeat, cbRndNeighbors, cbShowDigits;
    private SeekBar sbNumQuestions, sbNumNeighbors;
    private TextView tvQuestions, tvNeighbors;
    private SingleSelectToggleGroup ssBoard;
    private RangeSeekBar<Integer> rangeSeekBar;
    private SharedPreferences pref;
    private SharedPreferences.Editor edit;

    private boolean repeatNums = false;
    private boolean rndNeighbors = false;
    private boolean frBoard = true;
    private boolean showDigits = false;
    private int nums;
    private int neighbors;
    private int minNeighbors, maxNeighbors;

    private boolean readyToSave = false;

    private boolean startRepeatNums = false;
    private boolean startRndNeighbors = false;
    private boolean startFrBoard = true;
    private boolean startShowDigits = false;
    private int startNums;
    private int startNeighbors;
    private int startMinNeighbors, startMaxNeighbors;



    public static NeighborOptions newInstance() {
        NeighborOptions fragmentFirst = new NeighborOptions();
        return fragmentFirst;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_neighbor_options, container, false);

        tvQuestions = view.findViewById(R.id.tvNumQuestions);
        tvNeighbors = view.findViewById(R.id.tvNumNeighbors);

        cbRepeat = view.findViewById(R.id.cbRepeat);
        cbRepeat.setOnClickListener(this);

        cbRndNeighbors = view.findViewById(R.id.cbRndNeighbors);
        cbRndNeighbors.setOnClickListener(this);

        cbShowDigits = view.findViewById(R.id.cbShowDigits);
        cbShowDigits.setOnClickListener(this);

        sbNumQuestions = view.findViewById(R.id.sbNumQuestions);
        sbNumQuestions.setOnSeekBarChangeListener(this);
        sbNumNeighbors = view.findViewById(R.id.sbNumNeighbors);
        sbNumNeighbors.setOnSeekBarChangeListener(this);


        tvNeighbors.setText("" + (sbNumNeighbors.getProgress() + 1));
        tvQuestions.setText("" + (sbNumQuestions.getProgress() + 1));


        ssBoard = view.findViewById(R.id.group_choices);
        ssBoard.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {

                if (checkedId == R.id.choice_FR)
                    frBoard = true;
                else
                    frBoard = false;

                updateVars();

            }
        });

        rangeSeekBar = view.findViewById(R.id.rangeSeekbar);
        rangeSeekBar.setRangeValues(1,4);
        rangeSeekBar.setNotifyWhileDragging(true);
        rangeSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                if(minNeighbors == minValue && maxNeighbors == maxValue)
                    return;

                tvNeighbors.setText(minValue+" - "+maxValue);
                minNeighbors = minValue;
                maxNeighbors = maxValue;

                updateVars();
            }
        });

        pref = getActivity().getSharedPreferences("prefs",MODE_PRIVATE);
        edit = pref.edit();

        frBoard = pref.getBoolean("frBoard", true);
        if (!frBoard)
            ssBoard.check(R.id.choice_US);
        else
            ssBoard.check(R.id.choice_FR);

        rndNeighbors = pref.getBoolean("rndNeighbors", false);
        cbRndNeighbors.setChecked(rndNeighbors);
        if(rndNeighbors) {
            sbNumNeighbors.setVisibility(View.GONE);
            rangeSeekBar.setVisibility(View.VISIBLE);
            rangeSeekBar.setSelectedMinValue(1);
            rangeSeekBar.setSelectedMaxValue((sbNumNeighbors.getProgress()+1));
            tvNeighbors.setText(rangeSeekBar.getSelectedMinValue()+" - "+rangeSeekBar.getSelectedMaxValue());
        }

        repeatNums = pref.getBoolean("repeatNums", false);
        cbRepeat.setChecked(repeatNums);
        if (!repeatNums) {
            if (sbNumQuestions.getProgress() > 36)
                sbNumQuestions.setProgress(36);

            sbNumQuestions.setMax(36);
        } else {
            sbNumQuestions.setMax(49);
        }

        showDigits = pref.getBoolean("showDigits", true);
        cbShowDigits.setChecked(showDigits);

        nums = pref.getInt("nums", 1);
        tvQuestions.setText("" + nums);
        sbNumQuestions.setProgress((nums - 1));

        neighbors = pref.getInt("neighbors", 1);
        tvNeighbors.setText("" + neighbors);
        sbNumNeighbors.setProgress((neighbors - 1));

        if(rndNeighbors){
            minNeighbors = pref.getInt("minNeighbors",1);
            maxNeighbors = pref.getInt("maxNeighbors",4);
            rangeSeekBar.setSelectedMaxValue(maxNeighbors);
            rangeSeekBar.setSelectedMinValue(minNeighbors);
            tvNeighbors.setText(minNeighbors+" - " + maxNeighbors);
        }

        readyToSave = true;

        setStartVariables();

        return view;
    }


    private void setStartVariables(){
        startRepeatNums = repeatNums;
        startRndNeighbors = rndNeighbors;
        startFrBoard = frBoard;
        startShowDigits = showDigits;
        startNums = nums;
        startNeighbors = neighbors;
        startMinNeighbors = minNeighbors;
        startMaxNeighbors = maxNeighbors;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        detectChanges();
    }

    private void detectChanges(){

        if(startRepeatNums == repeatNums && startRndNeighbors == rndNeighbors && startFrBoard == frBoard
                && startShowDigits == showDigits && startNums == nums && startNeighbors == neighbors
                && startMinNeighbors == minNeighbors && startMaxNeighbors == maxNeighbors)
            edit.putBoolean("neighborOptionsChanges",false).apply();
        else {
            //reseting highscore
            edit.putBoolean("neighborOptionsChanges", true).apply();
            edit.putString("neighborHighscore","-").apply();
            edit.putLong("neighborHighscoreNumber",90000000).apply();
            edit.putInt("neighborRightAnswers",0).apply();
        }

        try{
            ((MainActivity) getActivity()).reloadNRFViews();
        }catch (Exception e){}

    }

    private void saveData(){
        if(!readyToSave)
            return;

        edit.putBoolean("frBoard",frBoard).apply();

        edit.putBoolean("rndNeighbors",rndNeighbors).apply();

        edit.putBoolean("repeatNums",repeatNums).apply();

        edit.putBoolean("showDigits",showDigits).apply();

        edit.putInt("nums",nums).apply();

        edit.putInt("neighbors",neighbors).apply();

        edit.putInt("minNeighbors",minNeighbors).apply();

        edit.putInt("maxNeighbors",maxNeighbors).apply();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cbRepeat:
                if (cbRepeat.isChecked()) {
                    repeatNums = true;

                    sbNumQuestions.setMax(49);

                } else {
                    if (sbNumQuestions.getProgress() > 36)
                        sbNumQuestions.setProgress(36);

                    sbNumQuestions.setMax(36);
                }

                tvQuestions.setText("" + (sbNumQuestions.getProgress() + 1));

                break;
            case R.id.cbRndNeighbors:
                if (cbRndNeighbors.isChecked()) {
                    rndNeighbors = true;
                    sbNumNeighbors.setVisibility(View.GONE);
                    rangeSeekBar.setVisibility(View.VISIBLE);
                    rangeSeekBar.setSelectedMinValue(1);
                    rangeSeekBar.setSelectedMaxValue((sbNumNeighbors.getProgress()+1));
                    tvNeighbors.setText(rangeSeekBar.getSelectedMinValue()+" - "+rangeSeekBar.getSelectedMaxValue());
                } else {
                    rndNeighbors = false;
                    sbNumNeighbors.setVisibility(View.VISIBLE);
                    rangeSeekBar.setVisibility(View.GONE);
                    tvNeighbors.setText("" + (sbNumNeighbors.getProgress() + 1));
                }

                break;
            case R.id.cbShowDigits:
                if (cbShowDigits.isChecked())
                    showDigits = true;
                else
                    showDigits = false;
                break;
        }

        updateVars();
    }

    private void updateVars(){
        nums = Integer.parseInt((String) tvQuestions.getText());
        if(rndNeighbors) {
            if(rangeSeekBar.getSelectedMaxValue() == rangeSeekBar.getSelectedMinValue())
                   neighbors = rangeSeekBar.getSelectedMaxValue();
        else {
                minNeighbors = rangeSeekBar.getSelectedMinValue();
                maxNeighbors = rangeSeekBar.getSelectedMaxValue();
            }
        }else
            neighbors = Integer.parseInt((String) tvNeighbors.getText());

        saveData();

}

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        progress++;

        switch (seekBar.getId()) {
            case R.id.sbNumNeighbors:
                tvNeighbors.setText("" + progress);
                break;
            case R.id.sbNumQuestions:
                tvQuestions.setText("" + progress);
                break;
        }

        updateVars();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

}

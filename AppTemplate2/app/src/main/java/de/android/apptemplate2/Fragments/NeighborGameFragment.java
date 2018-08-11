package de.android.apptemplate2.Fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.LinkedList;

import de.android.apptemplate2.NeighborGameActivity;
import de.android.apptemplate2.R;
import de.android.apptemplate2.Roulette;

public class NeighborGameFragment extends Fragment implements View.OnClickListener {

    private Button btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn10, btn11, btn12,
            btn13, btn14, btn15, btn16, btn17, btn18, btn19, btn20, btn21, btn22, btn23, btn24,
            btn25, btn26, btn27, btn28, btn29, btn30, btn31, btn32, btn33, btn34, btn35, btn36, btnNext, btn00;

    private TextView tvQuestion;

    private ViewGroup rootView;

    private Button[] buttons = new Button[38];

    private LinkedList<Integer> list;
    private int neighbors;
    private int maxListSize;
    private int[] results;
    private int rolledNum;
    private int position;
    private boolean showDigits;
    private boolean frBoard;

    private Typeface tf;

    private Drawable bgBlack, bgRed, bgWhite, bgWhiteSelected, bgBlackSelected, bgRedSelected, drawableCorrect, drawableFalse;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/cthulhumbus.ttf");

        drawableCorrect = (ContextCompat.getDrawable(getActivity(), R.drawable.correct) );
        drawableCorrect.setBounds(0,0,60,60);

        drawableFalse = (ContextCompat.getDrawable(getActivity(), android.R.drawable.ic_delete) );
        drawableFalse.setBounds(0,0,60,60);

        bgBlack = (ContextCompat.getDrawable(getActivity(), R.drawable.black_num) );
        bgBlackSelected = (ContextCompat.getDrawable(getActivity(), R.drawable.selected_black_num) );
        bgRed = (ContextCompat.getDrawable(getActivity(), R.drawable.red_num) );
        bgRedSelected = (ContextCompat.getDrawable(getActivity(), R.drawable.selected_red_num) );
        bgWhite = (ContextCompat.getDrawable(getActivity(), R.drawable.white_num) );
        bgWhiteSelected = (ContextCompat.getDrawable(getActivity(), R.drawable.selected_white_num) );

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_neighbor_page, container, false);

        initBtns();
        putButtonsInArray();
        setButtonTextSize_Font();

        if (!NeighborGameActivity.evaluateAnswers) {

            addOnClickListeners();

        }

        position = getArguments().getInt("position", 0);

        neighbors = getArguments().getInt("neighbors", -1);

        //if -1, dann random nachbarn
        if(neighbors == -1)
            neighbors = NeighborGameActivity.rndNeighbor[position];

        showDigits = getArguments().getBoolean("showDigits", true);

        frBoard = getArguments().getBoolean("frBoard",true);

        if (!showDigits)
            hideDigits();

        maxListSize = neighbors * 2 + 1;

        list = new LinkedList<>();

        rolledNum = NeighborGameActivity.rolledNums.get(position);

        if(frBoard)
            results = Roulette.getNumberWithNeighborsFR(rolledNum, neighbors);
        else{
            results = Roulette.getNumberWithNeighborsUS(rolledNum, neighbors);

            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            );

            int dp2 = (int) convertDpToPixel(2, getContext());

            //Todo testen ob padding eingehatlen wird

            param.setMargins(dp2,dp2,dp2,dp2);
            btn0.setLayoutParams(param);
            param.setMargins(0,dp2,dp2,dp2);
            btn00.setLayoutParams(param);
        }

        if (NeighborGameActivity.answers[position] != null)
            list = NeighborGameActivity.answers[position];
        else
            list = new LinkedList<>();

        checkButtons();

        displayRolledNum();

        //falls nicht alle felder angeklickt wurden, next button schwarz färben
        if (list == null || list.size() < maxListSize)
            btnNext.setVisibility(View.INVISIBLE);

        return rootView;
    }

    private void checkButtons() {

        if (list != null)
            for (Integer btn : list) {
                if(btn.equals(0) || btn.equals(37))
                    buttons[btn].setBackground(bgWhiteSelected);
                else if(btn % 2 == 0)
                    buttons[btn].setBackground(bgBlackSelected);
                else
                    buttons[btn].setBackground(bgRedSelected);
            }

        if (NeighborGameActivity.evaluateAnswers)
            evaluateInput();

    }

    private void hideDigits() {
        for (View button : buttons)
            ((Button) button).setText("");
    }

    private void evaluateInput() {

        for (int res : results) {

            Log.e("Results ",""+res);

            Drawable color = buttons[res].getBackground();

            //missing mark
            if (color.equals(bgBlack) || color.equals(bgRed) || color.equals(bgWhite)) {
                buttons[res].setCompoundDrawables(drawableFalse,null,drawableFalse,null);
            }

            //right mark
            if (color.equals(bgBlackSelected) || color.equals(bgRedSelected) || color.equals(bgWhiteSelected)){
                buttons[res].setCompoundDrawables(drawableCorrect,null,drawableCorrect,null);
            }

            //TODO falsch angekreuzt muss noch berücksichtigt werden

        }

        Log.e("Results ","ende");


    }

    private void displayRolledNum() {
        if(rolledNum == 37)
            tvQuestion.setText("00" + " with " + neighbors);
        else
            tvQuestion.setText(rolledNum + " with " + neighbors);
    }

    private void initBtns() {
        btn0 = (Button) rootView.findViewById(R.id.btn0);
        btn1 = (Button) rootView.findViewById(R.id.btn1);
        btn2 = (Button) rootView.findViewById(R.id.btn2);
        btn3 = (Button) rootView.findViewById(R.id.btn3);
        btn4 = (Button) rootView.findViewById(R.id.btn4);
        btn5 = (Button) rootView.findViewById(R.id.btn5);
        btn6 = (Button) rootView.findViewById(R.id.btn6);
        btn7 = (Button) rootView.findViewById(R.id.btn7);
        btn8 = (Button) rootView.findViewById(R.id.btn8);
        btn9 = (Button) rootView.findViewById(R.id.btn9);
        btn10 = (Button) rootView.findViewById(R.id.btn10);
        btn11 = (Button) rootView.findViewById(R.id.btn11);
        btn12 = (Button) rootView.findViewById(R.id.btn12);
        btn13 = (Button) rootView.findViewById(R.id.btn13);
        btn14 = (Button) rootView.findViewById(R.id.btn14);
        btn15 = (Button) rootView.findViewById(R.id.btn15);
        btn16 = (Button) rootView.findViewById(R.id.btn16);
        btn17 = (Button) rootView.findViewById(R.id.btn17);
        btn18 = (Button) rootView.findViewById(R.id.btn18);
        btn19 = (Button) rootView.findViewById(R.id.btn19);
        btn20 = (Button) rootView.findViewById(R.id.btn20);
        btn21 = (Button) rootView.findViewById(R.id.btn21);
        btn22 = (Button) rootView.findViewById(R.id.btn22);
        btn23 = (Button) rootView.findViewById(R.id.btn23);
        btn24 = (Button) rootView.findViewById(R.id.btn24);
        btn25 = (Button) rootView.findViewById(R.id.btn25);
        btn26 = (Button) rootView.findViewById(R.id.btn26);
        btn27 = (Button) rootView.findViewById(R.id.btn27);
        btn28 = (Button) rootView.findViewById(R.id.btn28);
        btn29 = (Button) rootView.findViewById(R.id.btn29);
        btn30 = (Button) rootView.findViewById(R.id.btn30);
        btn31 = (Button) rootView.findViewById(R.id.btn31);
        btn32 = (Button) rootView.findViewById(R.id.btn32);
        btn33 = (Button) rootView.findViewById(R.id.btn33);
        btn34 = (Button) rootView.findViewById(R.id.btn34);
        btn35 = (Button) rootView.findViewById(R.id.btn35);
        btn36 = (Button) rootView.findViewById(R.id.btn36);
        btn00 = (Button) rootView.findViewById(R.id.btn00);

        btnNext = (Button) rootView.findViewById(R.id.btnNext);
        btnNext.setTypeface(tf);

        tvQuestion = (TextView) rootView.findViewById(R.id.tvQuestion);
        tvQuestion.setTypeface(tf);
    }

    private void putButtonsInArray() {
        buttons[0] = btn0;
        buttons[1] = btn1;
        buttons[2] = btn2;
        buttons[3] = btn3;
        buttons[4] = btn4;
        buttons[5] = btn5;
        buttons[6] = btn6;
        buttons[7] = btn7;
        buttons[8] = btn8;
        buttons[9] = btn9;
        buttons[10] = btn10;
        buttons[11] = btn11;
        buttons[12] = btn12;
        buttons[13] = btn13;
        buttons[14] = btn14;
        buttons[15] = btn15;
        buttons[16] = btn16;
        buttons[17] = btn17;
        buttons[18] = btn18;
        buttons[19] = btn19;
        buttons[20] = btn20;
        buttons[21] = btn21;
        buttons[22] = btn22;
        buttons[23] = btn23;
        buttons[24] = btn24;
        buttons[25] = btn25;
        buttons[26] = btn26;
        buttons[27] = btn27;
        buttons[28] = btn28;
        buttons[29] = btn29;
        buttons[30] = btn30;
        buttons[31] = btn31;
        buttons[32] = btn32;
        buttons[33] = btn33;
        buttons[34] = btn34;
        buttons[35] = btn35;
        buttons[36] = btn36;
        buttons[37] = btn00;
    }

    private void addOnClickListeners() {

        for(Button b: buttons){
            b.setOnClickListener(this);
        }

    }

    private void setButtonTextSize_Font(){
        for(Button b: buttons) {
            b.setTypeface(tf);
            b.setTextSize(20);


            int color = Integer.parseInt(b.getTag().toString());

            if(color == 0 || color == 37)
                b.setBackground(bgWhite);
            else if(color % 2 == 0)
                b.setBackground(bgBlack);
            else
                b.setBackground(bgRed);
        }
    }

    @Override
    public void onClick(View v) {

        boolean checked = true;
        int color = Integer.parseInt(v.getTag().toString());

        Drawable.ConstantState bg = v.getBackground().getConstantState();

        if(bg.equals(bgBlack.getConstantState())
                || bg.equals(bgRed.getConstantState())
                || bg.equals(bgWhite.getConstantState())){
            checked = false;
        }

        if (!checked) {
            //mark and add field to list

            if (list.size() >= maxListSize)
                return;

            list.add(Integer.parseInt((String) v.getTag()));

            if(color == 0 || color == 37)
                v.setBackground(bgWhiteSelected);
            else if(color % 2 == 0)
                v.setBackground(bgBlackSelected);
            else
                v.setBackground(bgRedSelected);

            //allows to continue to next question / result
            if (list.size() == maxListSize) {

                btnNext.setVisibility(View.VISIBLE);

                NeighborGameActivity.answers[position] = list;

                NeighborGameActivity.progress++;
                //NeighborGameActivity.progressUpdate();
            }

        } else {
            //unmark and delete field from list

            //forbids to continue to next question / result

            btnNext.setVisibility(View.INVISIBLE);

            if (list.size() == maxListSize) {

                NeighborGameActivity.answers[position] = list;

                list.remove(Integer.valueOf((String) v.getTag()));

                if(color == 0 || color == 37)
                    v.setBackground(bgWhite);
                else if(color % 2 == 0)
                    v.setBackground(bgBlack);
                else
                    v.setBackground(bgRed);

                NeighborGameActivity.progress--;
                //NeighborGameActivity.progressUpdate();
            }
            list.remove(Integer.valueOf((String) v.getTag()));

            if(color == 0 || color == 37)
                v.setBackground(bgWhite);
            else if(color % 2 == 0)
                v.setBackground(bgBlack);
            else
                v.setBackground(bgRed);
        }
    }

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

}


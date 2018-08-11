package de.android.apptemplate2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

import de.android.apptemplate2.Fragments.ResultFragment;
import de.android.apptemplate2.Fragments.NeighborGameFragment;
import de.android.apptemplate2.view.SlidingTabLayout;

public class NeighborGameActivity extends FragmentActivity {

    private ViewPager mPager;
    private SlidingTabLayout mSlidingTabLayout;

    private static PagerAdapter mPagerAdapter;
    public static boolean evaluateAnswers;
    public static LinkedList<Integer>[] answers;
    public static ArrayList<Integer> rolledNums;

    private ResultFragment resFrag;

    private int numPages;
    public static int progress = 1;
    public int wrongAnswers;

    private boolean repeatAction = false;

    private int neighbors;
    public static int[] rndNeighbor;

    private int currentPosition;

    private boolean frBoard;
    private boolean rndNeighbors;
    private boolean repeatNums;
    private boolean showDigits;

    private long startTime;
    public long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neighbor_game);

        progress = 1;

        SharedPreferences pref = getSharedPreferences("prefs",MODE_PRIVATE);

        frBoard = pref.getBoolean("frBoard", true);
        repeatNums = pref.getBoolean("repeatNums", false);
        showDigits = pref.getBoolean("showDigits", true);
        numPages = pref.getInt("nums", 1)+1;
        rndNeighbors = pref.getBoolean("rndNeighbors", false);
        if(rndNeighbors){
            int min = pref.getInt("minNeighbors",1);
            int max = pref.getInt("maxNeighbors",4);
            rndNeighbor = new int[(numPages-1)];

            fillRndRange(min,max);

        }else
            neighbors = pref.getInt("neighbors", 1);


        resFrag = new ResultFragment();
        answers = new LinkedList[numPages-1];

        evaluateAnswers = false;

        //roll rnd numbers with no duplicates
        ArrayList<Integer> allNums;
        if(!repeatNums) {
            if(frBoard)
                allNums = new ArrayList<Integer>(Arrays.asList(Roulette.rouletteNumbersFR));
            else
                allNums = new ArrayList<Integer>(Arrays.asList(Roulette.rouletteNumbersUS));

            Collections.shuffle(allNums);

            rolledNums = new ArrayList<Integer>(allNums.subList(0,(numPages-1)));

        }
        //roll rnd numbers with duplicates allowed
        else{
            rolledNums = new ArrayList<>((numPages-1));
            for(int i = 0; i < (numPages-1); i++)
                if(frBoard)
                    rolledNums.add(Roulette.getRndNumberFR());
                else
                    rolledNums.add(Roulette.getRndNumberUS());
        }

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setCustomTabView(R.layout.custom_tab_view,R.id.tvTabName,R.id.tabBackground);
        mSlidingTabLayout.setViewPager(mPager);


        mSlidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if(repeatAction) {
                    repeatAction = false;
                    return;
                }

                if(position < progress)
                    mPager.setCurrentItem(position, true);
                else {
                    Toast.makeText(NeighborGameActivity.this, "Access denied", Toast.LENGTH_SHORT).show();
                    repeatAction = true;
                    mPager.setCurrentItem(currentPosition);
                    return;
                }

                currentPosition = position;

                if (position == numPages - 1 && !evaluateAnswers) {
                    evaluateAnswers = true;
                    stopTime();
                    evalAns();
                    progressUpdate();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        startTime = System.currentTimeMillis();

    }

    private void fillRndRange(int min, int max){

        max++;

        for(int i = 0; i < rndNeighbor.length; i++){
            int neighbor = min + (int)(Math.random() * (max - min));
            rndNeighbor[i] = neighbor;
        }

    }

    private void stopTime() {

        long endTime = System.currentTimeMillis();

        time = (endTime - startTime);

    }

    private void evalAns() {

        boolean[] results = new boolean[numPages-1];


        for (int i = 0; i < rolledNums.size(); i++) {

            boolean correct = true;
            int[] correctAnswers;

            if(rndNeighbors){
                if(frBoard)
                    correctAnswers = Roulette.getNumberWithNeighborsFR(rolledNums.get(i), rndNeighbor[i]);
                else
                    correctAnswers = Roulette.getNumberWithNeighborsUS(rolledNums.get(i), rndNeighbor[i]);
            }
            else {
                if(frBoard)
                    correctAnswers = Roulette.getNumberWithNeighborsFR(rolledNums.get(i), neighbors);
                else
                    correctAnswers = Roulette.getNumberWithNeighborsUS(rolledNums.get(i), neighbors);
            }

            for (int y = 0; y < correctAnswers.length; y++) {
                if (!answers[i].contains(correctAnswers[y])) {
                    correct = false;
                    wrongAnswers++;
                    break;
                }
            }

            results[i] = correct;

        }

        mSlidingTabLayout.setBackgroundColors(results);

    }


    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == numPages - 1) {
                return resFrag;

            }

            NeighborGameFragment frag = new NeighborGameFragment();
            Bundle bundle = new Bundle();

            if(rndNeighbors)
                bundle.putInt("neighbors", -1);
            else
                bundle.putInt("neighbors", neighbors);

            bundle.putInt("position", position);
            bundle.putBoolean("showDigits",showDigits);
            bundle.putBoolean("frBoard",frBoard);
            frag.setArguments(bundle);

            return frag;
        }

        @Override
        public int getCount() {
            return numPages;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            if(position == numPages-1)
                return "Results";
            return "      " + (position + 1)+"      ";
        }

    }

    public void progressUpdate() {
        mSlidingTabLayout.setViewPager(mPager);
        mPagerAdapter.notifyDataSetChanged();
        resFrag.setResults(numPages-1-wrongAnswers, wrongAnswers, time);
    }

    public void nextPage(View v) {
        mPager.setCurrentItem(currentPosition + 1, true);
    }

}

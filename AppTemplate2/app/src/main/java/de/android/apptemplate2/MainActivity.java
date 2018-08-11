package de.android.apptemplate2;

import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import de.android.apptemplate2.Fragments.NeighborHome;
import de.android.apptemplate2.Fragments.FirstFragment;
import de.android.apptemplate2.Fragments.NeighborOptions;
import de.android.apptemplate2.Fragments.NeighborReadyFragment;

public class MainActivity extends AppCompatActivity {

    private static final String SCREEN_HEIGHT = "screenHeight";
    private SharedPreferences sharedPreferences;
    private float screenHeight;
    private LinearLayout linearLayout;
    private CustomScrollView scrollView;

    private NeighborReadyFragment neighborReadyFragment;

    private int currentPosition = 0;
    float firstTouch = -1;
    float lastTouch = -1;

    private static ArrayList<ViewPager> pagerList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);

        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        screenHeight = sharedPreferences.getFloat(SCREEN_HEIGHT, 0);

        if (screenHeight == 0)
            getLayoutHeight();
        else
            init();

        scrollView = findViewById(R.id.sv);

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_MOVE){
                    if(firstTouch == -1)
                        firstTouch = motionEvent.getY();

                    lastTouch = motionEvent.getY();

                }else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    int direction = getDirection();

                    firstTouch = -1;
                    lastTouch = -1;

                    snapToView(direction);
                }
                return false;
            }
        });

    }

    private void snapToView(final int direction){
        if(currentPosition == 0 && direction >= 0 || direction == 0)
            scrollView.scrollTo(0,0);
        else if(direction == -1) {
            currentPosition = currentPosition+(int) screenHeight;
            scrollView.scrollTo(0, currentPosition);
        }else if(direction == 1) {
            currentPosition = currentPosition-(int) screenHeight;
            scrollView.scrollTo(0, currentPosition);
        }

    }

    private int getDirection(){
        float minScrollAmount = screenHeight / 100 * 15;
        if(Math.abs(firstTouch - lastTouch) < minScrollAmount)
            return 0;
        if(firstTouch > lastTouch)
            return -1;
        else
            return 1;
    }

    private void init(){
        linearLayout = findViewById(R.id.ll);

        pagerList = new ArrayList<>();

        neighborReadyFragment = NeighborReadyFragment.newInstance(0, "Game");

        ArrayList<Fragment> frags = new ArrayList<>();
        frags.add(NeighborOptions.newInstance());
        frags.add(NeighborHome.newInstance(0, "Home"));
        frags.add(neighborReadyFragment);


        ArrayList<Fragment> frags2 = new ArrayList<>();
        frags2.add(NeighborOptions.newInstance());
        frags2.add(FirstFragment.newInstance(1, "Home"));
        frags2.add(FirstFragment.newInstance(1, "Game"));

        addViewPager(frags, true);
        addViewPager(frags2, false);

    }

    private ViewPager addViewPager(ArrayList<Fragment> fragments, boolean isNeighborhoodGame ){
        CardView cardView = new CardView(this);
        cardView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                (int) screenHeight));

        linearLayout.addView(cardView);

        ViewPager viewPager = new ViewPager(this);
        viewPager.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {

            viewPager.setId(Utils.generateViewId());

        } else {

            viewPager.setId(View.generateViewId());

        }

        cardView.addView(viewPager);

        //add Adapter
        FragmentPagerAdapter adapterViewPager = new MyPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapterViewPager);
        viewPager.setCurrentItem(1);

        pagerList.add(viewPager);

        return viewPager;
    }

    private void getLayoutHeight() {
        linearLayout = findViewById(R.id.ll);

        ViewTreeObserver vto = linearLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    linearLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    linearLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

                screenHeight = linearLayout.getMeasuredHeight();

                sharedPreferences.edit().putFloat(SCREEN_HEIGHT, screenHeight).commit();

                init();
            }
        });
    }

    public static void movePager(int pager, boolean right){
        ViewPager viewPager = pagerList.get(pager);

        if(right)
            viewPager.setCurrentItem(viewPager.getCurrentItem()+1,true);
        else
            viewPager.setCurrentItem(viewPager.getCurrentItem()-1,true);
    }

    public void nextPage(View v) {
        //Todo testen!! evtl. fehler
        ViewPager viewPager = null;
        if(currentPosition == 0){
            viewPager = pagerList.get(0);
        }else {
            int currentViewPager = currentPosition / (int) screenHeight;
            viewPager = pagerList.get(currentViewPager);
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
        }
    }

    public void reloadNRFViews(){
        neighborReadyFragment.initTextViews();
    }

}

package de.android.apptemplate2;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wajahatkarim3.easyflipview.EasyFlipView;
import com.yuyakaido.android.cardstackview.CardStackView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FlashCards extends FragmentActivity {

    CardStackView cardStackView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_cards);

        cardStackView = (CardStackView) findViewById(R.id.cardStackView);

        ArrayList<Integer> values = new ArrayList<>(Arrays.asList(Roulette.rouletteNumbersFR));
        Collections.shuffle(values);

        ArrayAdapter<Integer> adapter = new FlashCardAdapter(this,
                R.layout.flashcard, R.id.tvFlashCardQuestion, values);

        cardStackView.setAdapter(adapter);
    }


    private class FlashCardAdapter extends ArrayAdapter<Integer> {

        private int layoutId;
        private Context context;
        private List<Integer> values;

        public FlashCardAdapter(Context context, int layoutId, int tvId, List<Integer> values) {
            super(context, layoutId, tvId, values);
            this.layoutId = layoutId;
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater inflater = ((FragmentActivity) context).getLayoutInflater();
                convertView = inflater.inflate(layoutId, parent, false);

            }

            final EasyFlipView mYourFlipView = (EasyFlipView) convertView.findViewById(R.id.flipView);

            mYourFlipView.setFlipOnTouch(false);

            if(mYourFlipView.isBackSide()){
                mYourFlipView.flipTheView(false);
            }


            ((TextView) convertView.findViewById(R.id.tvFlashCardQuestion)).setText(""+values.get(position));

            int[] answers = Roulette.getNumberWithNeighborsFR(values.get(position),4);
            int counter = 0;

            ((TextView) convertView.findViewById(R.id.tvleftNeighbor1)).setText(""+answers[counter++]);
            ((TextView) convertView.findViewById(R.id.tvleftNeighbor2)).setText(""+answers[counter++]);
            ((TextView) convertView.findViewById(R.id.tvleftNeighbor3)).setText(""+answers[counter++]);
            ((TextView) convertView.findViewById(R.id.tvleftNeighbor4)).setText(""+answers[counter++]);
            ((TextView) convertView.findViewById(R.id.tvRolledNum)).setText(""+answers[counter++]);
            ((TextView) convertView.findViewById(R.id.tvrightNeighbor1)).setText(""+answers[counter++]);
            ((TextView) convertView.findViewById(R.id.tvrightNeighbor2)).setText(""+answers[counter++]);
            ((TextView) convertView.findViewById(R.id.tvrightNeighbor3)).setText(""+answers[counter++]);
            ((TextView) convertView.findViewById(R.id.tvrightNeighbor4)).setText(""+answers[counter++]);


            mYourFlipView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((EasyFlipView) v).flipTheView();
                    v.setClickable(false);
                }
            });

            return convertView;
        }
    }


}

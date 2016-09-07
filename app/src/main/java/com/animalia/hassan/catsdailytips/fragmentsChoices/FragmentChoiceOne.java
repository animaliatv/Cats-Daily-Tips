package com.animalia.hassan.catsdailytips.fragmentsChoices;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.animalia.hassan.catsdailytips.activties.TipsFragActivity;
import com.animalia.hassan.catsdailytips.firstStart.FirstStartActivity;
import com.animalia.hassan.catsdailytips.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentChoiceOne extends Fragment {


    public FragmentChoiceOne() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_choice_one, container, false);
        CardView cardone = (CardView) view.findViewById(R.id.card_view);
        cardone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do something

                Intent intent = new Intent(getActivity(), TipsFragActivity.class);
                intent.setAction("open_Daily");
                startActivity(intent);

            }

        });
        return view;
    }
}

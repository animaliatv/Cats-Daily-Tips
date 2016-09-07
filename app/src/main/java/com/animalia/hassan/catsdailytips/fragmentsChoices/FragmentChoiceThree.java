package com.animalia.hassan.catsdailytips.fragmentsChoices;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.animalia.hassan.catsdailytips.R;
import com.animalia.hassan.catsdailytips.youTube.YouTubeActivityX;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentChoiceThree extends Fragment {


    public FragmentChoiceThree() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_choice_three, container, false);
        CardView cardThree = (CardView) view.findViewById(R.id.card_view);
        cardThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), YouTubeActivityX.class);
                startActivity(intent);
            }
        });

        return view;
    }

}

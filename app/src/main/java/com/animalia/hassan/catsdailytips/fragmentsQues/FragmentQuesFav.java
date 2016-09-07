package com.animalia.hassan.catsdailytips.fragmentsQues;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.animalia.hassan.catsdailytips.CatsDTRecyclerView;
import com.animalia.hassan.catsdailytips.database.CatsTandQHelper;
import com.animalia.hassan.catsdailytips.KenBurnsView;
import com.animalia.hassan.catsdailytips.R;
import com.animalia.hassan.catsdailytips.recyclerAdaptors.RecyclerAdapterFav;

import java.util.ArrayList;
import java.util.Collections;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentQuesFav extends Fragment {



    LinearLayoutManager layoutManager;
    KenBurnsView mHeaderPicture;
    ArrayList<Ques> arrayListQF = new ArrayList<>();
    RecyclerView.Adapter adapter;
    CatsDTRecyclerView recyclerView1;
    public FragmentQuesFav() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ques_fav, container, false);
        recyclerView1 =(CatsDTRecyclerView) view.findViewById(android.R.id.list);

        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView1.setLayoutManager(layoutManager);
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setEmptyView(view.findViewById(android.R.id.empty));
        CatsTandQHelper catsTandQHelper = CatsTandQHelper.getInstance(view.getContext());
        SQLiteDatabase sqLiteDatabase = catsTandQHelper.getReadableDatabase();

        Cursor cursorQF = catsTandQHelper.getFavQuesInfo(sqLiteDatabase);
        cursorQF.moveToFirst();
        boolean cursorNdone;
        do {
            try {
                Ques ques = new Ques(cursorQF.getInt(0), cursorQF.getString(1), cursorQF.getString(2),
                        cursorQF.getString(3), cursorQF.getInt(4), cursorQF.getString(5));
                arrayListQF.add(ques);
                Log.d("Ques add", "to Fav list");
                if (ques.equals(null)){
                    cursorNdone = false;
                }
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
                cursorNdone = false;
            }catch (IllegalStateException e)
            {
                e.printStackTrace();

            }

        } while (cursorQF.moveToNext());
        catsTandQHelper.close();
        Collections.reverse(arrayListQF);
        adapter = new RecyclerAdapterFav(arrayListQF);
        recyclerView1.setAdapter(adapter);




        return view;
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */



    @Override
    public void onResume() {
        super.onResume();
        setUserVisibleHint(true);

    }


    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public void setUserVisibleHint(boolean isVisible) {
    super.setUserVisibleHint(isVisible);
    if (isVisible) {
        // bind to stuff using value of fromPage from here

        if (adapter == null) {

        }else{
            arrayListQF.clear();

            CatsTandQHelper catsTandQHelper = CatsTandQHelper.getInstance(getContext());
            SQLiteDatabase sqLiteDatabase = catsTandQHelper.getReadableDatabase();

            Cursor cursorQF = catsTandQHelper.getFavQuesInfo(sqLiteDatabase);
            cursorQF.moveToFirst();
            boolean cursorNdone;
            do {
                try {
                    Ques ques = new Ques(cursorQF.getInt(0), cursorQF.getString(1), cursorQF.getString(2),
                            cursorQF.getString(3), cursorQF.getInt(4), cursorQF.getString(5));
                    arrayListQF.add(ques);
                    Log.d("Ques add", "to Fav list");
                    if (ques.equals(null)){
                        cursorNdone = false;
                    }
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                    cursorNdone = false;
                }

            } while (cursorQF.moveToNext());
            catsTandQHelper.close();
            Collections.reverse(arrayListQF);
            adapter = new RecyclerAdapterFav(arrayListQF);
            recyclerView1.setAdapter(adapter);

        }


    }
}
}

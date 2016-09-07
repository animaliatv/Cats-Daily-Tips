package com.animalia.hassan.catsdailytips.fragmentsTips;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.animalia.hassan.catsdailytips.CatsDTRecyclerView;
import com.animalia.hassan.catsdailytips.database.CatsTandQHelper;
import com.animalia.hassan.catsdailytips.KenBurnsView;
import com.animalia.hassan.catsdailytips.R;
import com.animalia.hassan.catsdailytips.fragmentsQues.Ques;
import com.animalia.hassan.catsdailytips.recyclerAdaptors.RecyclerAdapterFav;

import java.util.ArrayList;
import java.util.Collections;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTipsFav extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    LinearLayoutManager layoutManager;
    KenBurnsView mHeaderPicture;
    ArrayList<Ques> arrayListTF = new ArrayList<>();
    CatsDTRecyclerView.Adapter adapter;
    CatsDTRecyclerView recyclerView1;
    public FragmentTipsFav() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tips_fav, container, false);

        recyclerView1 =(CatsDTRecyclerView) view.findViewById(android.R.id.list);
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView1.setLayoutManager(layoutManager);
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setEmptyView(view.findViewById(android.R.id.empty));
        CatsTandQHelper catsTandQHelper = CatsTandQHelper.getInstance(view.getContext());
        SQLiteDatabase sqLiteDatabase = catsTandQHelper.getReadableDatabase();

        Cursor cursor = catsTandQHelper.getFavTipsInfo(sqLiteDatabase);
        cursor.moveToFirst();
        boolean cursorNdone;
        do {
            try {
                Ques ques = new Ques(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getInt(4), cursor.getString(5));
                arrayListTF.add(ques);
                if (ques.equals(null)){
                    cursorNdone = false;
                }
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
                cursorNdone = false;
            }

        } while (cursor.moveToNext());
        catsTandQHelper.close();
        Collections.reverse(arrayListTF);
        adapter = new RecyclerAdapterFav(arrayListTF);
        recyclerView1.setAdapter(adapter);



        return view;
    }



    public void setUserVisibleHint(boolean isVisible) {
        super.setUserVisibleHint(isVisible);
        if (isVisible) {
            // bind to stuff using value of fromPage from here

            if (adapter == null) {

            }else{
                arrayListTF.clear();

                CatsTandQHelper catsTandQHelper = CatsTandQHelper.getInstance(getContext());
                SQLiteDatabase sqLiteDatabase = catsTandQHelper.getReadableDatabase();

                Cursor cursor = catsTandQHelper.getFavTipsInfo(sqLiteDatabase);
                cursor.moveToFirst();
                boolean cursorNdone;
                do {
                    try {
                        Ques ques = new Ques(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                                cursor.getString(3), cursor.getInt(4), cursor.getString(5));
                        arrayListTF.add(ques);
                        if (ques.equals(null)){
                            cursorNdone = false;
                        }
                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                        cursorNdone = false;
                    } catch (IllegalStateException e)
                    {
                        e.printStackTrace();

                    }

                } while (cursor.moveToNext());
                catsTandQHelper.close();
                Collections.reverse(arrayListTF);
                adapter = new RecyclerAdapterFav(arrayListTF);
                recyclerView1.setAdapter(adapter);
                if (cursorNdone = false) {
                    recyclerView1.smoothScrollToPosition(recyclerView1.getAdapter().getItemCount() - 1);
                }

            }


        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setUserVisibleHint(true);

    }

}

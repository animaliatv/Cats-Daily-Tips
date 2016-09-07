package com.animalia.hassan.catsdailytips.fragmentsTips;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.animalia.hassan.catsdailytips.AsyncResponse;
import com.animalia.hassan.catsdailytips.BackGroundTask;
import com.animalia.hassan.catsdailytips.BuildConfig;
import com.animalia.hassan.catsdailytips.CatsDTRecyclerView;
import com.animalia.hassan.catsdailytips.database.CatsTandQHelper;
import com.animalia.hassan.catsdailytips.KenBurnsView;
import com.animalia.hassan.catsdailytips.R;
import com.animalia.hassan.catsdailytips.recyclerAdaptors.RecyclerAdapterNorm;

import java.util.ArrayList;
import java.util.Collections;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTips extends Fragment implements AsyncResponse {

    LinearLayoutManager layoutManager;
    KenBurnsView mHeaderPicture;
    ArrayList<Tips> arrayListT = new ArrayList<>();
    CatsDTRecyclerView.Adapter adapter;
    CatsDTRecyclerView recyclerView;
    BackGroundTask backGroundTask;
    private SwipeRefreshLayout swipeContainer;
    String json_url = BuildConfig.json_url_t;


    public FragmentTips() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_tips, container, false);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        recyclerView =(CatsDTRecyclerView) view.findViewById(android.R.id.list);
        layoutManager = new LinearLayoutManager(view.getContext());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setEmptyView(view.findViewById(android.R.id.empty));
        refreshRecycler();


        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Setup refresh listener which triggers new data loading
        BackGroundTask backGroundTask0 = (BackGroundTask) new BackGroundTask(new AsyncResponse(){

            @Override
            public void processFinish(String output){
                //Here you will receive the result fired from async class
                //of onPostExecute(result) method.
                refreshRecycler();
            }
        }).execute(json_url);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.

                BackGroundTask backGroundTask = (BackGroundTask) new BackGroundTask(new AsyncResponse(){

                    @Override
                    public void processFinish(String output){
                        //Here you will receive the result fired from async class
                        //of onPostExecute(result) method.
                       /* swipeContainer.setRefreshing(false);*/
                        refreshRecycler();
                       /* swipeContainer.setRefreshing(false);*/
                        Log.d("RESULTS CAME BACK ...",". . . . SWIPE CONTAINER CANCELED");
                    }
                }).execute(json_url);



            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }



    public void setUserVisibleHint(boolean isVisible) {
        super.setUserVisibleHint(isVisible);
        if (isVisible) {
            // bind to stuff using value of fromPage from here

            if (adapter == null) {

            }else{
            refreshRecycler();


            }


        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setUserVisibleHint(true);

    }

    public void refreshRecycler() {


            arrayListT.clear();

            CatsTandQHelper catsTandQHelper = CatsTandQHelper.getInstance(getContext());
            SQLiteDatabase sqLiteDatabase = catsTandQHelper.getReadableDatabase();

            Cursor cursor = catsTandQHelper.getTipsInfo(sqLiteDatabase);
            cursor.moveToFirst();
            boolean cursorNdone;
            do {
                try {
                    Tips tips = new Tips(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                            cursor.getString(3), cursor.getInt(4), cursor.getString(5));
                    arrayListT.add(tips);
                    if (tips.equals(null)) {
                        cursorNdone = false;
                    }
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                    cursorNdone = false;
                }catch (IllegalStateException e)
                {
                    e.printStackTrace();

                }

            } while (cursor.moveToNext());
            catsTandQHelper.close();

            Collections.reverse(arrayListT);
            adapter = new RecyclerAdapterNorm(arrayListT);
            recyclerView.setAdapter(adapter);
        if (swipeContainer == null){

        }else {

            swipeContainer.setRefreshing(false);
        }
    }


    @Override
    public void processFinish(String output) {
    }
}

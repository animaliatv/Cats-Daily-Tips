package com.animalia.hassan.catsdailytips.fragmentsQues;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.animalia.hassan.catsdailytips.AsyncResponse;
import com.animalia.hassan.catsdailytips.BackGroundTask;
import com.animalia.hassan.catsdailytips.BuildConfig;
import com.animalia.hassan.catsdailytips.CatsDTRecyclerView;
import com.animalia.hassan.catsdailytips.database.CatsTandQHelper;
import com.animalia.hassan.catsdailytips.R;
import com.animalia.hassan.catsdailytips.recyclerAdaptors.RecyclerAdapterNorm;
import com.animalia.hassan.catsdailytips.fragmentsTips.Tips;

import java.util.ArrayList;
import java.util.Collections;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentQues extends Fragment implements AsyncResponse {


    LinearLayoutManager layoutManager;
    ArrayList<Tips> arrayListQ = new ArrayList<>();
    RecyclerView.Adapter adapter;
    CatsDTRecyclerView recyclerView;
    BackGroundTask backGroundTask;
    private SwipeRefreshLayout swipeContainer;
    String json_url = BuildConfig.json_url_q;


    public FragmentQues() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ques, container, false);


        recyclerView =(CatsDTRecyclerView) view.findViewById(android.R.id.list);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        layoutManager = new LinearLayoutManager(view.getContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setEmptyView(view.findViewById(android.R.id.empty));
        refreshRecycler();




        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Setup refresh listener which triggers new data loading
        backGroundTask = new BackGroundTask(getContext());
        backGroundTask.delegate = this;
        BackGroundTask backGroundTask0 = (BackGroundTask) new BackGroundTask(new AsyncResponse(){

            @Override
            public void processFinish(String output){
                //Here you will receive the result fired from async class
                //of onPostExecute(result) method.
                        /*swipeContainer.setRefreshing(false);*/
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
                        /*swipeContainer.setRefreshing(false);*/
                        refreshRecycler();
                        Log.d("RESULTS CAME BACK ...",". . . . SWIPE CONTAINER CANCELED");
                    }
                }).execute(json_url);
                // set to false in the results


            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    public void onResume() {
        super.onResume();
        setUserVisibleHint(true);
    }


    @Override
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

    public void refreshRecycler() {

        arrayListQ.clear();

        CatsTandQHelper catsTandQHelper = CatsTandQHelper.getInstance(getContext());
        SQLiteDatabase sqLiteDatabase = catsTandQHelper.getReadableDatabase();

        Cursor cursorQ = catsTandQHelper.getQuesInfo(sqLiteDatabase);
        cursorQ.moveToFirst();
        boolean cursorNdone;
        do {
            try {
                Tips tips = new Tips(cursorQ.getInt(0), cursorQ.getString(1), cursorQ.getString(2),
                        cursorQ.getString(3), cursorQ.getInt(4), cursorQ.getString(5));
                arrayListQ.add(tips);
                Log.d("Ques add", "to list");
                if (tips.equals(null)){
                    cursorNdone = false;
                }
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
                cursorNdone = false;
            }catch (IllegalStateException e)
            {
                e.printStackTrace();

            }

        } while (cursorQ.moveToNext());
        catsTandQHelper.close();
        Collections.reverse(arrayListQ);
        adapter = new RecyclerAdapterNorm(arrayListQ);
        recyclerView.setAdapter(adapter);
        if (swipeContainer == null){

        }else {

            swipeContainer.setRefreshing(false);
        }

    }


    @Override
    public void processFinish(String output) {
        /*swipeContainer.setRefreshing(false);*/
        refreshRecycler();
        Log.d("RESULTS CAME BACK ...",". . . . SWIPE CONTAINER CANCELED");
    }
}

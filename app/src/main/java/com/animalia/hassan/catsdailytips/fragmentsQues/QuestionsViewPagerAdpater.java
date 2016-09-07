package com.animalia.hassan.catsdailytips.fragmentsQues;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

/**
 * Created by hassan on 27/09/2015.
 */
public class QuestionsViewPagerAdpater extends FragmentStatePagerAdapter {

    private CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    private int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created


    // Build a Constructor and assign the passed Values to appropriate values in the class
    public QuestionsViewPagerAdpater(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

/*    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        if(position == 0) // if the position is 0 we are returning the First tab
        {
            new FragmentQues();
            return FragmentQues.newInstance(0);
        }
        else             // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
        {
            new FragmentQuesFav();
            return FragmentQuesFav.newInstance(1);
        }
        }*/
        @Override
        public Fragment getItem(int position) {

            Fragment fragment = null;
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    fragment = new FragmentQues();
                    break;
                case 1: // Fragment # 1 - This will show SecondFragment
                    fragment = new FragmentQuesFav();
                    break;

            }

            return fragment;
        }



    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }

    public  void RequestChildFocus(View child, View focused)
    {
        //Do nothing, disables automatic focus behaviour
    }




}


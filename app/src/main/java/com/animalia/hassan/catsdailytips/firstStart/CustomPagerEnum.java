package com.animalia.hassan.catsdailytips.firstStart;

import com.animalia.hassan.catsdailytips.R;

/**
 * Created by Hassan on 8/22/2016.
 */

public enum CustomPagerEnum {

    RED(R.string.red, R.layout.sc1),
    BLUE(R.string.blue, R.layout.sc2),
    ORANGE(R.string.orange, R.layout.sc3);

    private int mTitleResId;
    private int mLayoutResId;

    CustomPagerEnum(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }

}
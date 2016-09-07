package com.animalia.hassan.catsdailytips;

import java.util.Random;

/**
 * Created by Hassan on 6/20/2016.
 */

public class HeaderImage {

    private static final Random RANDOM = new Random();

    public static int getRandomHeaderDrawable() {
        switch (RANDOM.nextInt(5)) {
            default:
            case 0:
                return R.drawable.headerb;
            case 1:
                return R.drawable.headerc;
            case 2:
                return R.drawable.headerd;
            case 3:
                return R.drawable.headere;
            case 4:
                return R.drawable.headerh;
            case 5:
                return R.drawable.headerx;
            case 6:
                return R.drawable.headery;
        }
    }
}

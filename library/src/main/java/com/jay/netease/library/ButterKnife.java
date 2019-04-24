package com.jay.netease.library;

import android.app.Activity;
import android.util.Log;

/**
 * Created by zengqingjie on 19/4/24.
 */

public class ButterKnife {
    private static final String TAG = "ButterKnife";

    public static void bind(Activity activity) {
        //MainActivity$ViewBinder
        String activityName = activity.getClass().getName() + "$ViewBinder";
        Log.i(TAG, "activityName=" + activityName);
        try {
            Class<?> viewBindingClass = Class.forName(activityName);
            ViewBinder viewBinder = (ViewBinder) viewBindingClass.newInstance();
            viewBinder.bind(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

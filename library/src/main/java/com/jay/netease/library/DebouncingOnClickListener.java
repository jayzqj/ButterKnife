package com.jay.netease.library;

import android.view.View;

/**
 * Created by zengqingjie on 19/4/25.
 */

public abstract class DebouncingOnClickListener implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        doClick(v);
    }

    public abstract void doClick(View view);
}

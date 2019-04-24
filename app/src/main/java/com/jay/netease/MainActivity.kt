package com.jay.netease

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jay.netease.library.ButterKnife

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this);
    }
}

package com.jay.netease;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jay.netease.annotations.BindView;
import com.jay.netease.annotations.OnClick;
import com.jay.netease.library.ButterKnife;

/**
 * Created by zengqingjie on 19/4/25.
 */

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.tv1)
    TextView tv1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn1, R.id.btn2})
    public void onClick(View view) {
        Toast.makeText(this, "onClick", Toast.LENGTH_SHORT).show();
        switch (view.getId()) {
            case R.id.btn1:
                tv1.setText("btn1");
                break;
            case R.id.btn2:
                tv1.setText("btn2");
                break;
        }
    }
}

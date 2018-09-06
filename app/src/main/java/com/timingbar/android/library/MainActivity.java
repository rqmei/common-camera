package com.timingbar.android.library;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.btn_scan)
    Button btnScan;
    @BindView(R.id.btn_camera)
    Button btnCamera;
    @BindView(R.id.btn_hand_sign)
    Button btnHandSign;
    @BindView(R.id.btn_player)
    Button btnPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        ButterKnife.bind (this);
        btnScan.setOnClickListener (this);
        btnCamera.setOnClickListener (this);
        btnHandSign.setOnClickListener (this);
        btnPlayer.setOnClickListener (this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId ()) {
            case R.id.btn_scan://二维码
                startActivity (new Intent (this, ScanActivity.class));
                break;
            case R.id.btn_camera://相机
                startActivity (new Intent (this, CameraActivity.class));
                break;
            case R.id.btn_hand_sign://手写签名
                startActivity (new Intent (this, HandSignActivity.class));
                break;
            case R.id.btn_player://播放器
                startActivity (new Intent (this, PlayerActivity.class));
                break;
        }
    }
}

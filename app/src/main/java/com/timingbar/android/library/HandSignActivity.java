package com.timingbar.android.library;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import lib.android.timingbar.com.view.handSign.HandSignView;

import java.io.File;
import java.io.IOException;

/**
 * HandSignActivity
 * -----------------------------------------------------------------------------------------------------------------------------------
 * 测试手写签名View的示例
 *
 * @author rqmei on 2018/9/5
 */

public class HandSignActivity extends AppCompatActivity implements View.OnClickListener {
    public static String path = Environment.getExternalStorageDirectory ().getAbsolutePath () + File.separator + "qm.png";
    @BindView(R.id.tv_promit)
    TextView tvPromit;
    @BindView(R.id.btn_clear)
    Button btnClear;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.view_hand_sign)
    HandSignView viewHandSign;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.hand_sign);
        ButterKnife.bind (this);
        registerListener ();
    }

    public void registerListener() {
        btnSave.setOnClickListener (this);
        btnClear.setOnClickListener (this);
        viewHandSign.setHandWriteInterface (new HandSignView.HandWriteInterface () {
            @Override
            public void onStartWrite(boolean isStartWrite) {
                if (isStartWrite) {
                    tvPromit.setVisibility (View.GONE);
                } else {
                    tvPromit.setVisibility (View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId ()) {
            case R.id.btn_save:
                if (viewHandSign.isSign ()) {
                    try {
                        viewHandSign.save (path, true, 10, false);
                    } catch (IOException e) {
                        e.printStackTrace ();
                        Log.i ("HandSignActivity", "save保存签名后生成的图片出现异常！" + e.getMessage ());
                    }
                } else {
                    Toast.makeText (HandSignActivity.this, "您还没有签名", Toast.LENGTH_SHORT).show ();
                }
                break;
            case R.id.btn_clear:
                viewHandSign.clear ();
                break;
        }
    }
}

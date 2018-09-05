package com.timingbar.android.library;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import lib.android.timingbar.com.camera.ScanCallback;
import lib.android.timingbar.com.camera.ScanCameraPreview;

public class ScanActivity extends AppCompatActivity {
    @BindView(R.id.scan_preview)
    ScanCameraPreview scanPreview;
    @BindView(R.id.scan_restart)
    Button scanRestart;
    @BindView(R.id.scan_result)
    TextView scanResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.scan);
        ButterKnife.bind (this);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        scanPreview.setScanCallback (resultCallback);
        scanRestart.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                scanPreview.start ();
                scanRestart.setVisibility (View.GONE);
                scanResult.setVisibility (View.VISIBLE);
            }
        });
    }

    private ScanCallback resultCallback = new ScanCallback () {
        @Override
        public void onScanResult(String result) {
            scanPreview.stop ();
            scanRestart.setVisibility (View.VISIBLE);
            scanResult.setVisibility (View.GONE);
            Toast.makeText (ScanActivity.this, "ScanActivity 扫描结果--->" + result, Toast.LENGTH_LONG).show ();
        }

        @Override
        public void openCameraFail() {

        }
    };
}

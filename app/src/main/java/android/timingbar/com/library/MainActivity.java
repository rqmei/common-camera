package android.timingbar.com.library;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnScan, btnCamera, btnGps;
    TextView tvLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        btnScan = findViewById (R.id.btn_scan);
        btnCamera = findViewById (R.id.btn_camera);
        btnGps = findViewById (R.id.btn_gps);
        tvLocation = findViewById (R.id.tv_location);
        btnScan.setOnClickListener (this);
        btnCamera.setOnClickListener (this);
        btnGps.setOnClickListener (this);
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
            case R.id.btn_gps://


                break;
        }
    }
}

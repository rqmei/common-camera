package com.timingbar.android.library;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import butterknife.BindView;
import butterknife.ButterKnife;
import lib.android.timingbar.com.camera.CameraPreview;
import lib.android.timingbar.com.camera.PictureCallback;

/**
 * CameraActivity
 * -----------------------------------------------------------------------------------------------------------------------------------
 *
 * @author rqmei on 2018/4/25
 */

public class CameraActivity extends AppCompatActivity {

    @BindView(R.id.camera)
    CameraPreview camera;

    public void initData() {
        camera.setPictureCallback (new PictureCallback () {
            @Override
            public void onPictureTakenResult(Bitmap bitmap) {
                Log.i ("CameraActivity", " setPictureCallback拍照成功了" + bitmap);
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.camera);
        ButterKnife.bind (this);
        initData ();
    }
}

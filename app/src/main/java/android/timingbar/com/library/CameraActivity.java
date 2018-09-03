package android.timingbar.com.library;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import lib.android.timingbar.com.camera.CameraPreview;
import lib.android.timingbar.com.camera.PictureCallback;

/**
 * CameraActivity
 * -----------------------------------------------------------------------------------------------------------------------------------
 *
 * @author rqmei on 2018/4/25
 */

public class CameraActivity extends AppCompatActivity {

    CameraPreview camera;

    public void initData() {

        camera.setPictureCallback (new PictureCallback () {
            @Override
            public void onPictureTakenResult(Bitmap bitmap) {
               Log.i ("CameraActivity"," setPictureCallback拍照成功了" + bitmap);
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.camera);
        camera=findViewById (R.id.camera);
        initData ();
    }
}

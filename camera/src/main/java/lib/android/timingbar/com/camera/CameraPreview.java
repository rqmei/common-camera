package lib.android.timingbar.com.camera;

import android.Manifest;
import android.content.Context;
import android.hardware.Camera;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.*;
import android.widget.FrameLayout;
import android.widget.ImageView;
import camera.android.timingbar.com.cameralibrary.R;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.List;

/**
 * CameraPreview
 * -----------------------------------------------------------------------------------------------------------------------------------
 * 拍照预览页面
 *
 * @author rqmei on 2018/4/25
 */

public class CameraPreview extends FrameLayout implements SurfaceHolder.Callback {
    CameraPictureAnalysis cameraPictureAnalysis;
    private CameraManager mCameraManager;
    private SurfaceView mSurfaceView;
    private View headView, buttomView;
    private HeadViewHolder headViewHolder;

    public CameraPreview(@NonNull Context context) {
        this (context, null);
    }

    public CameraPreview(@NonNull Context context, @Nullable AttributeSet attrs) {
        this (context, attrs, 0);
    }

    public CameraPreview(@NonNull final Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super (context, attrs, defStyleAttr);
        mCameraManager = new CameraManager (context);
        cameraPictureAnalysis = new CameraPictureAnalysis (getContext (), mCameraManager.isFrontCamera () ? mCameraManager.getDirection () : "back");
        //  动态获取相机权限并打开camera
        AndPermission.with (context)
                .permission (Manifest.permission.CAMERA)
                .callback (new PermissionListener () {
                    @Override
                    public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                        start (Camera.CameraInfo.CAMERA_FACING_BACK);
                    }

                    @Override
                    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                        AndPermission.defaultSettingDialog (context).show ();
                    }
                })
                .start ();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i ("CameraPreview","cameraPreview surfaceCreated");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i ("CameraManager","cameraPreview surfaceChanged");
        if (holder.getSurface () == null) {
            return;
        }
        mCameraManager.stopPreview ();
        startCameraPreview (holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    /**
     * Camera start preview.
     */
    public boolean start(int cameraId) {
        try {
            mCameraManager.openDriver (cameraId);
        } catch (Exception e) {
            Log.i ("CameraPreview","CameraPreview 打开相机失败。。。" + e.getMessage ());
            return false;
        }
        if (mSurfaceView == null) {
            Log.i ("CameraPreview","CameraPreview 开起预览。。。");
            mSurfaceView = new SurfaceView (getContext ());
            addView (mSurfaceView, new LayoutParams (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            SurfaceHolder holder = mSurfaceView.getHolder ();
            holder.addCallback (this);
            holder.setType (SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        if (headView == null) {
            headView = LayoutInflater.from (getContext ()).inflate (R.layout.camera_header_bar, this, false);
            addView (headView);
            headViewHolder = new HeadViewHolder (headView);
            //设置闪光灯
            headViewHolder.ivFlashMode.setOnClickListener (new OnClickListener () { 
                @Override
                public void onClick(View v) {
                    CameraManager.FlashMode flashMode = mCameraManager.getFlashMode ();
                    if (flashMode == CameraManager.FlashMode.AUTO) {
                        mCameraManager.setFlashMode (CameraManager.FlashMode.TORCH);
                        headViewHolder.ivFlashMode.setImageResource (R.drawable.camera_flash_torch);
                    } else if (flashMode == CameraManager.FlashMode.OFF) {
                        mCameraManager.setFlashMode (CameraManager.FlashMode.AUTO);
                        headViewHolder.ivFlashMode.setImageResource (R.drawable.camera_flash_auto);
                    } else if (flashMode == CameraManager.FlashMode.ON) {
                        mCameraManager.setFlashMode (CameraManager.FlashMode.OFF);
                        headViewHolder.ivFlashMode.setImageResource (R.drawable.camera_flash_off);
                    } else if (flashMode == CameraManager.FlashMode.TORCH) {
                        mCameraManager.setFlashMode (CameraManager.FlashMode.ON);
                        headViewHolder.ivFlashMode.setImageResource (R.drawable.camera_flash_on);
                    }
                }
            });
            //切换摄像头
            headViewHolder.ivSwitchCamera.setOnClickListener (new OnClickListener () {
                @Override
                public void onClick(View v) {
                    stop ();
                    Log.i ("CameraPreview","cameraPreView 开始切换camera摄像头isFrontCamera=" + mCameraManager.isFrontCamera () + "," + mCameraManager.isOpen ());
                    if (mCameraManager.isFrontCamera ()) {
                        start (Camera.CameraInfo.CAMERA_FACING_BACK);
                    } else {
                        start (Camera.CameraInfo.CAMERA_FACING_FRONT);
                    }
                }
            });
        }
        if (buttomView == null) {
            buttomView = LayoutInflater.from (getContext ()).inflate (R.layout.camera_bottom_bar, this, false);
            addView (buttomView);
            ImageView ivShutterCamera = buttomView.findViewById (R.id.iv_shutter_camera);
            ivShutterCamera.setOnClickListener (new OnClickListener () {
                @Override
                public void onClick(View v) {

                    mCameraManager.doTakePicture (cameraPictureAnalysis);
                }
            });
        }
        startCameraPreview (mSurfaceView.getHolder ());
        return true;
    }

    class HeadViewHolder {
        ImageView ivFlashMode;
        ImageView ivSwitchCamera;

        HeadViewHolder(View view) {
            ivFlashMode = view.findViewById (R.id.iv_flash_mode);
            ivSwitchCamera = view.findViewById (R.id.iv_switch_camera);
        }
    }

    /**
     * Camera stop preview.
     */
    public void stop() {
        removeCallbacks (mAutoFocusTask);
        mCameraManager.stopPreview ();
        mCameraManager.closeDriver ();
    }

    /**
     * camera开起预览
     *
     * @param holder
     */
    private void startCameraPreview(SurfaceHolder holder) {
        try {
            mCameraManager.startOrientationChangeListener (getContext ());
            mCameraManager.startPreview (holder, null);
            mCameraManager.autoFocus (mFocusCallback);
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }

    private Camera.AutoFocusCallback mFocusCallback = new Camera.AutoFocusCallback () {
        public void onAutoFocus(boolean success, Camera camera) {
            postDelayed (mAutoFocusTask, 1000);
        }
    };
    /**
     * 自动对焦
     */
    private Runnable mAutoFocusTask = new Runnable () {
        public void run() {
            mCameraManager.autoFocus (mFocusCallback);
        }
    };

    @Override
    protected void onDetachedFromWindow() {
        stop ();
        super.onDetachedFromWindow ();
    }

    public void setPictureCallback(PictureCallback callback) {
        if (cameraPictureAnalysis != null) {
            cameraPictureAnalysis.setPictureCallback (callback);
        }
    }
}

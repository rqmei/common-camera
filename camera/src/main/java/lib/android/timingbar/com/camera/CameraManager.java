package lib.android.timingbar.com.camera;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;


/**
 * CameraManager
 * -----------------------------------------------------------------------------------------------------------------------------------
 * 相机管理类
 *
 * @author rqmei on 2018/4/4
 */
public final class CameraManager {
    private final CameraConfiguration mConfiguration;
    private Camera mCamera;
    private boolean isFrontCamera = false;

    public CameraManager(Context context) {
        this.mConfiguration = new CameraConfiguration (context);
    }

    /**
     * 打开相机，获取camera（扫描二维码）
     **/
    public synchronized void openDriver(int cameraId) throws Exception {
        Log.i ("CameraManager","CameraManager 。。。" + mCamera);
        if (mCamera != null)
            return;
        mCamera = Camera.open (cameraId);
        if (mCamera == null) {
            Log.i ("CameraManager","CameraManager 相机对象为空。。。");
            throw new IOException ("The camera is occupied.");
        }
        mConfiguration.initFromCameraParameters (mCamera);
        Camera.Parameters parameters = mCamera.getParameters ();
        String parametersFlattened = parameters == null ? null : parameters.flatten ();
        try {
            mConfiguration.setDesiredCameraParameters (mCamera);
        } catch (RuntimeException re) {
            Log.i ("CameraManager","CameraManager RuntimeException11。。。");
            if (parametersFlattened != null) {
                parameters = mCamera.getParameters ();
                parameters.unflatten (parametersFlattened);
                try {
                    mCamera.setParameters (parameters);
                    mConfiguration.setDesiredCameraParameters (mCamera);
                } catch (RuntimeException e) {
                    Log.i ("CameraManager","CameraManager RuntimeException。。。");
                    e.printStackTrace ();
                }
            }
        }
    }
    /**
     * 打开相机，获取camera（拍照）
     **/
    public synchronized void openCamera(int cameraId) throws Exception {
        Log.i ("CameraManager","CameraManager 。。。" + mCamera);
        if (mCamera != null)
            return;
        mCamera = Camera.open (cameraId);
        if (mCamera == null) {
            Log.i ("CameraManager","CameraManager 相机对象为空。。。");
            throw new IOException ("The camera is occupied.");
        }
        if (cameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            isFrontCamera = true;
        } else {
            isFrontCamera = false;
        }
        mConfiguration.initFromCameraParameters (mCamera);
        Camera.Parameters parameters = mCamera.getParameters ();
        //camera参数Parameters赋值是否有出错
        String parametersFlattened = parameters == null ? null : parameters.flatten ();
        try {
            mConfiguration.setDesiredCameraParameters (mCamera);
        } catch (RuntimeException re) {
            Log.i ("CameraManager","CameraManager RuntimeException11。。。");
            if (parametersFlattened != null) {
                //Parameters出现异常
                parameters = mCamera.getParameters ();
                parameters.unflatten (parametersFlattened);
                try {
                    mCamera.setParameters (parameters);
                    mConfiguration.setDesiredCameraParameters (mCamera);
                } catch (RuntimeException e) {
                    Log.i ("CameraManager","CameraManager RuntimeException。。。");
                    e.printStackTrace ();
                }
            }
        }
    }

    /**
     * 关闭相机，释放相关资源
     */
    public synchronized void closeDriver() {
        if (mCamera != null) {
            mCamera.setPreviewCallback (null);
            mCamera.release ();
            mCamera = null;
        }
    }

    /**
     * Camera is opened.
     *
     * @return true, other wise false.
     */
    public boolean isOpen() {
        return mCamera != null;
    }


    public boolean isFrontCamera() {
        return isFrontCamera;
    }

    /**
     * Get camera configuration.
     *
     * @return {@link CameraConfiguration}.
     */
    public CameraConfiguration getConfiguration() {
        return mConfiguration;
    }
    public int getStatusBarHeight(Context context) {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    /**
     * Camera resolution.
     *
     * @return {@link Point}.
     */
    public Point getCameraResolution() {
        return mConfiguration.getCameraResolution ();
    }
    public Point getScreenResolution() {
        return mConfiguration.getScreenResolution ();
    }
    /**
     * Camera start preview.
     *
     * @param holder          {@link SurfaceHolder}.
     * @param previewCallback {@link Camera.PreviewCallback}.
     * @throws IOException if the method fails (for example, if the surface is unavailable or unsuitable).
     */
    public void startPreview(SurfaceHolder holder, Camera.PreviewCallback previewCallback) throws IOException {
        if (mCamera != null) {
            mConfiguration.setDesiredCameraParameters (mCamera, getRotation ());
           // mCamera.setDisplayOrientation (90);
            mCamera.setPreviewDisplay (holder);
            mCamera.setPreviewCallback (previewCallback);
            mCamera.startPreview ();
            isPreviewing = true;
        }
    }

    /**
     * Camera stop preview.
     */
    public void stopPreview() {
        if (mCamera != null) {
            try {
                mCamera.stopPreview ();
                isPreviewing = false;
            } catch (Exception ignored) {
                // nothing.
            }
            try {
                mCamera.setPreviewDisplay (null);
            } catch (IOException ignored) {
                // nothing.
            }
        }
    }

    /**
     * Focus on, make a scan action.
     *
     * @param callback {@link Camera.AutoFocusCallback}.
     */
    public void autoFocus(Camera.AutoFocusCallback callback) {
        if (mCamera != null&&mCamera.getParameters ().isSmoothZoomSupported ())
            try {
                mCamera.autoFocus (callback);
            } catch (Exception e) {
                e.printStackTrace ();
            }
    }
    
    //----------------------------闪关灯控制----------------------------------------------------
    /**
     * 闪光灯类型枚举 默认为关闭
     */
    public enum FlashMode {
        /**
         * ON:拍照时打开闪光灯
         */
        ON,
        /**
         * OFF：不打开闪光灯
         */
        OFF,
        /**
         * AUTO：系统决定是否打开闪光灯
         */
        AUTO,
        /**
         * TORCH：一直打开闪光灯
         */
        TORCH
    }
    /**
     * 当前闪光灯类型，默认为关闭
     */
    private FlashMode mFlashMode = FlashMode.ON;

    /**
     * 设置闪光灯
     *
     * @param flashMode
     */
    public void setFlashMode(FlashMode flashMode) {
        if (mCamera == null)
            return;
        mFlashMode = flashMode;
        Camera.Parameters parameters = mCamera.getParameters ();
        List<String> FlashModes = parameters.getSupportedFlashModes ();
        if (FlashModes != null) {
            Log.i ("CameraManager","setFlashMode================================FlashModes==" + FlashModes);
            switch (flashMode) {
                case ON://拍照时闪光灯；
                    if (FlashModes.contains (Camera.Parameters.FLASH_MODE_ON)) {
                        parameters.setFlashMode (Camera.Parameters.FLASH_MODE_ON);
                    }
                    break;
                case AUTO:// 自动模式，当光线较暗时自动打开闪光灯
                    if (FlashModes.contains (Camera.Parameters.FLASH_MODE_AUTO)) {
                        parameters.setFlashMode (Camera.Parameters.FLASH_MODE_AUTO);
                    }
                    break;
                case TORCH://打开
                    if (FlashModes.contains (Camera.Parameters.FLASH_MODE_TORCH)) {
                        parameters.setFlashMode (Camera.Parameters.FLASH_MODE_TORCH);
                    }
                    break;
                default://关闭闪光灯
                    if (FlashModes.contains (Camera.Parameters.FLASH_MODE_OFF)) {
                        parameters.setFlashMode (Camera.Parameters.FLASH_MODE_OFF);
                    }
                    break;
            }
            mCamera.setParameters (parameters);
        } else {
            // Toasts.show("无闪关灯！");
            Log.i ("CameraManager","无闪关灯=========================");
        }
    }

    /**
     * 获取当前闪光灯类型
     *
     * @return
     */
    public FlashMode getFlashMode() {
        return mFlashMode;
    }

    //---------------------------------------拍照---------------------------------------------------------
    boolean isPreviewing = false;

    /**
     * 拍照
     */
    public void doTakePicture(CameraPictureAnalysis cameraPictureAnalysis) {
        if (isPreviewing && (mCamera != null)) {
            mCamera.takePicture (null, null, cameraPictureAnalysis);
            isPreviewing = false;
        }
    }

    private String direction = "up";
    /**
     * 当前屏幕旋转角度
     */
    private int mOrientation = 0;

    /**
     * 屏幕方向监听
     *
     * @param context
     */
    public void startOrientationChangeListener(Context context) {
        OrientationEventListener mOrEventListener = new OrientationEventListener (context) {
            @Override
            public void onOrientationChanged(int rotation) {
                if (((rotation >= 0) && (rotation <= 45)) || (rotation > 315)) {
                    rotation = 0;
                    direction = "up";
                } else if ((rotation > 45) && (rotation <= 135)) {
                    rotation = 90;
                    direction = "right";
                } else if ((rotation > 135) && (rotation <= 225)) {
                    rotation = 180;
                    direction = "down";
                } else if ((rotation > 225) && (rotation <= 315)) {
                    rotation = 270;
                    direction = "left";
                } else {
                    rotation = 0;
                    direction = "up";
                }
                if (rotation == mOrientation)
                    return;
                mOrientation = rotation;
            }
        };
        mOrEventListener.enable ();
    }

    public int getRotation() {
        // rotation参数为 0、90、180、270。水平方向为0。
        int rotation = 90 + mOrientation == 360 ? 0 : 90 + mOrientation;
        // 前置摄像头需要对垂直方向做变换，否则照片是颠倒的
        if (isFrontCamera) {
            if (rotation == 90) {
                rotation = 270;
            } else if (rotation == 270) {
                rotation = 90;
            }
        }
        return rotation;
    }

    public String getDirection() {
        return direction;
    }
}

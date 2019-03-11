package com.timingbar.android.app;

import lib.android.timingbar.com.base.app.BaseApplication;
import lib.android.timingbar.com.http.EasyHttp;
import lib.android.timingbar.com.imageloader.ImageLoader;

/**
 * App
 * -----------------------------------------------------------------------------------------------------------------------------------
 *
 * @author rqmei on 2018/9/7
 */

public class ModuleApp extends BaseApplication {
    private static ModuleApp app;
    //图片管理器,用于加载图片的管理类,默认使用glide,使用策略模式,可替换框架

    ImageLoader imageLoader;
    public static ModuleApp getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate ();
        app = this;
        EasyHttp.init (this);
        EasyHttp.getInstance ().debug ("lib-camera", true).setBaseUrl ("http://www.jsyxx.cn/edu/mobile/");
        Thread.setDefaultUncaughtExceptionHandler (AppException.getAppExceptionHandler (this));
    }
    public ImageLoader imageLoader() {
        return this.imageLoader == null ? new ImageLoader () : imageLoader;
    }
}

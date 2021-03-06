package lib.android.timingbar.com.base.app;


import lib.android.timingbar.com.base.integration.AppManager;
import lib.android.timingbar.com.base.mvp.IRepositoryManager;

/**
 * App
 * -----------------------------------------------------------------------------------------------------------------------------------
 * 获取底层通用的连接器AppComponent对象
 * app通用的一些方法
 *
 * @author rqmei on 2018/1/29
 */

public interface IApp {
    AppManager appManager();
    //用于管理所有仓库(网络请求层),以及数据缓存层
    IRepositoryManager repositoryManager();
  
}

package lib.android.timingbar.com.base.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import lib.android.timingbar.com.base.mvp.IPresenter;

/**
 * IFragment
 * -----------------------------------------------------------------------------------------------------------------------------------
 * Fragment 通用的一些方法
 *
 * @author rqmei on 2018/1/30
 */

public interface IFragment<P extends IPresenter> {
    /**
     * 是否使用eventBus传递事件处理
     *
     * @return true 表示需要使用eventBus传递事件，这个时候需要注册订阅
     */
    boolean isRegisterEventBus();

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /**
     * 初始化数据
     *
     * @param savedInstanceState
     */
    void initData(Bundle savedInstanceState);

    /**
     * @return
     */
    P obtainPresenter();

    /**
     * 设置协调Model和View模块工作，处理交互的Presenter
     *
     * @param presenter
     */
    void setPresenter(P presenter);
}

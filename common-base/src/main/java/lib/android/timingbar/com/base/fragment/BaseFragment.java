package lib.android.timingbar.com.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lib.android.timingbar.com.base.mvp.EventMessage;
import lib.android.timingbar.com.base.mvp.IPresenter;
import lib.android.timingbar.com.base.util.EventBusUtils;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * BaseFragment
 * -----------------------------------------------------------------------------------------------------------------------------------
 * 因为java只能单继承,所以如果有需要继承特定Fragment的三方库,那你就需要自己自定义Fragment
 * 继承于这个特定的Fragment,然后按照将BaseFragment的格式,复制过去,记住一定要实现{@link IFragment}
 *
 * @author rqmei on 2018/1/30
 */

public abstract class BaseFragment<P extends IPresenter> extends Fragment implements IFragment<P> {
    protected P mPresenter;
    Unbinder unbinder;
    private boolean isInit = false; // 标识fragment视图已经初始化完毕
    private boolean isLoad = false; // 标识已经触发过懒加载数据

    public BaseFragment() {
        //必须确保在Fragment实例化时setArguments()
        setArguments (new Bundle ());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = initView (inflater, container, savedInstanceState);
        if (isRegisterEventBus ()) {
            EventBusUtils.register (this);
        }
        unbinder = ButterKnife.bind (this, rootView);
        return rootView;
    }

    /**
     * onViewCreated是在onCreateView后被触发的事件
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);
        isInit = true;
        isCanLoadData (savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated (savedInstanceState);
        if (mPresenter == null) {
            mPresenter = obtainPresenter ();
        }
    }

    /**
     * 通知fragment，该视图层已保存
     * 每次新建Fragment都会发生.
     * 它并不是实例状态恢复的方法, 只是一个View状态恢复的回调.
     * 在onActivityCreated()和onStart()之间调用
     *
     * @param savedInstanceState
     */
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored (savedInstanceState);
        if (mPresenter == null) {
            mPresenter = obtainPresenter ();
        }
    }

    /**
     * 视图是否已经对用户可见，系统的方法,在fragment的所有生命周期之前执行
     *
     * @param isVisibleToUser true:ragment被用户可见;false 不可见
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint (isVisibleToUser);
        isCanLoadData (getArguments ());
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView ();
        if (isRegisterEventBus ()) {
            EventBusUtils.unregister (this);
        }
        if (unbinder != null) {
            unbinder.unbind ();
        }
    }

    @Override
    public void setPresenter(P presenter) {
        this.mPresenter = presenter;
    }

    /**
     * 是否使用eventBus,默认为使用(true)，
     */
    @Override
    public boolean isRegisterEventBus() {
        return true;
    }


    /**
     * 是否可以加载数据
     * 可以加载数据的条件：
     * 1.视图已经初始化
     * 2.视图对用户可见
     */
    private void isCanLoadData(Bundle savedInstanceState) {
        if (!isInit) {
            return;
        }
        if (getUserVisibleHint ()) {
            //fragment 可见
            initData (savedInstanceState);
            isLoad = true;
        } else {
            if (isLoad) {
                onStopLoad ();
            }
        }
    }

    /**
     * 当视图已经对用户不可见并且加载过数据，如果需要在切换到其他页面时停止加载数据，可以调用此方法
     */
    protected void onStopLoad() {
    }

    /**
     * 接收到分发的事件
     *
     * @param event 事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveEvent(EventMessage event) {
    }

    /**
     * 接受到分发的粘性事件
     *
     * @param event 粘性事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onReceiveStickyEvent(EventMessage event) {
    }
}


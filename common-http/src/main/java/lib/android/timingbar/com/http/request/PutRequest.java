
package lib.android.timingbar.com.http.request;

import com.google.gson.reflect.TypeToken;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import lib.android.timingbar.com.http.cache.module.CacheResult;
import lib.android.timingbar.com.http.callback.CallBack;
import lib.android.timingbar.com.http.callback.CallBackProxy;
import lib.android.timingbar.com.http.callback.CallClazzProxy;
import lib.android.timingbar.com.http.func.ApiResultFunc;
import lib.android.timingbar.com.http.func.CacheResultFunc;
import lib.android.timingbar.com.http.func.RetryExceptionFunc;
import lib.android.timingbar.com.http.module.ApiResult;
import lib.android.timingbar.com.http.subsciber.CallBackSubsciber;
import lib.android.timingbar.com.http.util.RxUtil;
import okhttp3.ResponseBody;

import java.lang.reflect.Type;

/**
 * <p>描述：Put请求</p>
 * 作者： zhouyou<br>
 * 日期： 2017/5/22 16:30 <br>
 * 版本： v1.0<br>
 */
public class PutRequest extends BaseBodyRequest<PutRequest> {
    public PutRequest(String url) {
        super(url);
    }

    public <T> Observable<T> execute(Class<T> clazz) {
        return execute(new CallClazzProxy<ApiResult<T>, T> (clazz) {
        });
    }

    public <T> Observable<T> execute(Type type) {
        return execute(new CallClazzProxy<ApiResult<T>, T>(type) {
        });
    }

    @SuppressWarnings(value={"unchecked", "deprecation"})
    public <T> Observable<T> execute(CallClazzProxy<? extends ApiResult<T>, T> proxy) {
        return build().generateRequest()
                .map(new ApiResultFunc (proxy.getType()))
                .compose(isSyncRequest ? RxUtil._main() : RxUtil._io_main())
                .compose(rxCache.transformer(cacheMode, proxy.getCallType()))
                .retryWhen(new RetryExceptionFunc (retryCount, retryDelay, retryIncreaseDelay))
                .compose(new ObservableTransformer () {
                    @Override
                    public ObservableSource apply(@NonNull Observable upstream) {
                        return upstream.map(new CacheResultFunc<T> ());
                    }
                });
    }

    public <T> Disposable execute(CallBack<T> callBack) {
        return execute(new CallBackProxy<ApiResult<T>, T> (callBack) {
        });
    }

    @SuppressWarnings("unchecked")
    public <T> Disposable execute(CallBackProxy<? extends ApiResult<T>, T> proxy) {
        Observable<CacheResult<T>> observable = build().toObservable(generateRequest(), proxy);
        if (CacheResult.class != proxy.getCallBack().getRawType()) {
            return observable.compose(new ObservableTransformer<CacheResult<T>, T> () {
                @Override
                public ObservableSource<T> apply(@NonNull Observable<CacheResult<T>> upstream) {
                    return upstream.map(new CacheResultFunc<T>());
                }
            }).subscribeWith(new CallBackSubsciber<T> (context, proxy.getCallBack()));
        } else {
            return observable.subscribeWith(new CallBackSubsciber<CacheResult<T>>(context, proxy.getCallBack()));
        }
    }

    @SuppressWarnings("unchecked")
    private <T> Observable<CacheResult<T>> toObservable(Observable observable, CallBackProxy<? extends ApiResult<T>, T> proxy) {
        return observable.map(new ApiResultFunc(proxy != null ? proxy.getType() : new TypeToken<ResponseBody>() {
        }.getType()))
                .compose(isSyncRequest ? RxUtil._main() : RxUtil._io_main())
                .compose(rxCache.transformer(cacheMode, proxy.getCallBack().getType()))
                .retryWhen(new RetryExceptionFunc(retryCount, retryDelay, retryIncreaseDelay));
    }

    @Override
    protected Observable<ResponseBody> generateRequest() {
        if (this.object != null) {//自定义的请求object
            return apiManager.putBody(url, object);
        } else {
            return apiManager.put(url, params.urlParamsMap);
        }
    }
}

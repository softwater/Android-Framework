package com.nerd.android.framework.util;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.nerd.android.framework.model.FrameworkModel;
import java.util.List;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 对获取的数据进行处理
 * Created by softwater on 16/2/23.
 * Modified by softwater on 16/2/23.
 */
public class RxDataUtil {
  private static final String TAG = RxDataUtil.class.getCanonicalName();

  /**
   * 加载本地缓存数据
   *
   * @param activity Activity实例
   * @param observable 被观察者
   * @param next onNext处理
   */
  public static void loadLocalData(Activity activity, Observable<? extends List> observable,
      @Nullable Action1<List> next) {
    loadLocalData(activity, observable, next, null);
  }

  /**
   * 加载本地缓存数据
   *
   * @param activity Activity实例
   * @param observable 被观察者
   * @param next onNext处理
   * @param complete 加载完成时的回调
   */
  public static void loadLocalData(Activity activity, Observable<? extends List> observable,
      @Nullable Action1<List> next, @Nullable Action0 complete) {
    if (observable != null) {
      observable.subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(onLocalDataNext(next), onError(activity, null), onComplete(complete));
    } else {
      Log.e(TAG, "loadLocalData observable is null");
    }
  }

  /**
   * 从服务端获取数据
   *
   * @param activity Activity实例
   * @param observable 被观察者
   * @param next onNext处理
   */
  public static void refresh(Activity activity, Observable<? extends FrameworkModel> observable,
      @Nullable Action1<FrameworkModel> next) {
    refresh(activity, observable, next, null);
  }

  /**
   * 从服务端获取数据
   *
   * @param activity Activity实例
   * @param observable 被观察者
   * @param next onNext处理
   * @param complete onComplete处理
   */
  public static void refresh(Activity activity, Observable<? extends FrameworkModel> observable,
      @Nullable Action1<? extends FrameworkModel> next, @Nullable Action0 complete) {
    refresh(activity, observable, next, complete, null);
  }

  /**
   * 从服务端获取数据
   *
   * @param activity Activity实例
   * @param observable 被观察者
   * @param next onNext处理
   * @param complete onComplete处理
   */
  public static void refresh(Activity activity, Observable<? extends FrameworkModel> observable,
      @Nullable Action1<? extends FrameworkModel> next, @Nullable Action0 complete,
      Action1<Throwable> error) {
    if (observable != null) {
      observable.subscribeOn(Schedulers.io())
          .filter(filterUnsuccess(activity))
          .flatMap(flatMapResponseData())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(onNext((Action1<FrameworkModel>) next), onError(activity, error),
              onComplete(complete));
    } else {
      Log.e(TAG, "refresh observable is null");
    }
  }

  /**
   * 加载更多
   *
   * @param activity Activity实例
   * @param observable 被观察者
   * @param next onNext处理
   * @param complete onComplete处理
   */
  public static void loadMore(Activity activity, Observable<? extends FrameworkModel> observable,
      @Nullable Action1<? super FrameworkModel> next, @Nullable Action0 complete) {
    if (observable != null) {
      observable.subscribeOn(Schedulers.io())
          .filter(filterUnsuccess(activity))
          .flatMap(flatMapLoadMoreData())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(onNext((Action1<FrameworkModel>) next), onError(activity, null),
              onComplete(complete));
    } else {
      Log.e(TAG, "loadMore observable is null");
    }
  }

  /**
   * 对网络请求进行处理
   *
   * @param activity Activity实例
   * @param observable 被观察者
   * @param next onNext处理
   */
  public static void request(Activity activity, Observable<? extends FrameworkModel> observable,
      @Nullable Action1<? extends FrameworkModel> next) {
    request(activity, observable, next, null);
  }

  /**
   * 对网络请求进行处理
   *
   * @param activity Activity实例
   * @param observable 被观察者
   * @param next onNext处理
   * @param error onError处理
   */
  public static void request(Activity activity, Observable<? extends FrameworkModel> observable,
      @Nullable Action1<? extends FrameworkModel> next, @Nullable Action1<Throwable> error) {
    if (observable != null) {
      observable.subscribeOn(Schedulers.io())
          .filter(filterUnsuccess(activity))
          .flatMap(flatMapResponseData())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(onNext((Action1<FrameworkModel>) next), onError(activity, error));
    } else {
      Log.e(TAG, "request observable is null");
    }
  }

  /**
   * 对网络请求进行处理
   *
   * @param activity Activity实例
   * @param observable 被观察者
   */
  public static Observable request(Activity activity,
      Observable<? extends FrameworkModel> observable) {
    if (observable == null) {
      throw new IllegalArgumentException("observable not be null");
    }
    return observable.subscribeOn(Schedulers.io())
        .filter(filterUnsuccess(activity))
        .flatMap(flatMapResponseData())
        .observeOn(AndroidSchedulers.mainThread());
  }

  /**
   * filter：过滤未成功数据
   *
   * @param activity Activity实例
   */
  public static Func1<FrameworkModel, Boolean> filterUnsuccess(final Activity activity) {
    return new Func1<FrameworkModel, Boolean>() {
      @Override public Boolean call(FrameworkModel model) {
        return isSuccess(activity, model);
      }
    };
  }

  /**
   * 判断是否成功
   *
   * @param activity Activity实例
   * @param model Model实例
   */
  @NonNull public static Boolean isSuccess(final Activity activity, final FrameworkModel model) {
    boolean success = model.isSuccess();
    if (!success) {
      activity.runOnUiThread(new Runnable() {
        @Override public void run() {
          //new ErrorDialog(activity).showError(model.getMessage());
        }
      });
    }
    return success;
  }

  /**
   * 错误处理通用方法
   *
   * @param activity Activity实例
   */
  public static Action1<Throwable> onError(final Activity activity, Action1<Throwable> error) {
    if (error == null) {
      return new Action1<Throwable>() {
        @Override public void call(Throwable throwable) {
          //new ErrorDialog(activity).showError(throwable);
        }
      };
    } else {
      return error;
    }
  }

  /**
   * 对数据进行处理：插入数据库
   */
  public static Func1<FrameworkModel, Observable<FrameworkModel>> flatMapResponseData() {
    return new Func1<FrameworkModel, Observable<FrameworkModel>>() {
      @Override public Observable<FrameworkModel> call(FrameworkModel baseModel) {
        if (baseModel.getData() instanceof List) {
          // data为集合并且其中的数据类型为DataSupport时，处理数据库
          //List list = (List) baseModel.getData();
          //DbHelper.clear(list);
          //if (list.size() != 0) {
          //  if (list.get(0) instanceof DataSupport) {
          //    DbHelper.insert(list);
          //  }
          //}
        }
        return Observable.just(baseModel);
      }
    };
  }

  /**
   * 处理加载更多数据：插入数据库，只处理了列表
   */
  private static Func1<FrameworkModel, Observable<FrameworkModel>> flatMapLoadMoreData() {
    return new Func1<FrameworkModel, Observable<FrameworkModel>>() {
      @Override public Observable<FrameworkModel> call(FrameworkModel model) {
        if (model.getData() instanceof List) {
          //List l = (List) model.getData();
          //if (l != null && l.size() > 0 && l.get(0) instanceof DataSupport) {
          //  DbHelper.insert(l);
          //}
        }
        return Observable.just(model);
      }
    };
  }

  /**
   * onNext处理
   */
  private static Action1<FrameworkModel> onNext(Action1<FrameworkModel> action1) {
    if (action1 != null) {
      return action1;
    } else {
      return new Action1<FrameworkModel>() {
        @Override public void call(FrameworkModel baseModel) {
          Log.d(TAG, "onNext do nothing");
        }
      };
    }
  }

  /**
   * 本地数据 onNext处理
   */
  private static Action1<List> onLocalDataNext(Action1<List> action1) {
    if (action1 != null) {
      return action1;
    } else {
      return new Action1<List>() {
        @Override public void call(List baseModel) {
          Log.d(TAG, "onNext do nothing");
        }
      };
    }
  }

  /**
   * onComplete 处理
   */
  private static Action0 onComplete(Action0 complete) {
    if (complete != null) {
      return complete;
    } else {
      return new Action0() {
        @Override public void call() {
          Log.d(TAG, "onComplete do nothing");
        }
      };
    }
  }
}

package com.nerd.android.framework.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import com.nerd.android.framework.adapter.IAdapter;
import com.nerd.android.framework.model.FrameworkModel;
import com.nerd.android.framework.util.RxDataUtil;
import com.nerd.android.framework.util.Utils;
import com.nerd.android.framework.view.EmptyView;
import com.nerd.framework.R;
import java.util.List;
import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * 所有展示列表的Fragment的父类
 * Created by softwater on 2016/1/8.
 */
public abstract class FrameworkListFragment<A extends IAdapter> extends FrameworkFragment {
  private static final String TAG = FrameworkListFragment.class.getCanonicalName();
  /**
   * 界面展示的集合
   */
  protected List mData;
  /**
   * 当前页数
   */
  protected int mPageNo = 1;

  /**
   * 每页加载的条数，子类可在程序开始时对其修改，如onCreate(Bundle savedInstanceState)中
   */
  protected int mPageSize = 10;
  /**
   * 最新一次服务端返回的条数
   */
  protected int mCurrentSize = 0;
  /**
   * 是否加载更多
   */
  protected boolean mLoading = false;
  /**
   * 是否有加载更多这个功能，即是否可翻页，默认true
   */
  private boolean mHasLoadingMoreAbility = true;
  /**
   * 刷新控件
   */
  protected SwipeRefreshLayout mSwipeRefreshLayout;

  /**
   * 获取本地缓存数据
   */
  protected abstract Observable<? extends List> getCachedDataObservable();

  /**
   * 从服务端获取新的数据
   */
  protected abstract Observable<? extends FrameworkModel> getFreshDataObservable();

  /**
   * 获取Adapter
   */
  protected abstract A getAdapter();

  /**
   * 获取列表视图控件
   */
  protected abstract View getListView();

  /**
   * 空视图
   */
  protected EmptyView mEmptyView;

  /**
   * 是否有空视图
   */
  private boolean hasEmptyView = true;

  /**
   * Adapter
   */
  protected A mAdapter;

  /**
   * 获取数据的Adapter
   */
  protected abstract A setAdapter();

  @Nullable
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState,
      int layoutId) {
    mSwipeRefreshLayout =
        (SwipeRefreshLayout) inflater.inflate(R.layout.view_swipe_refresh_layout, container, false);
    mEmptyView = (EmptyView) mSwipeRefreshLayout.findViewById(R.id.empty_view);

    RelativeLayout rl = (RelativeLayout) mSwipeRefreshLayout.findViewById(R.id.rl);
    View childView = inflater.inflate(layoutId, rl, false);
    rl.addView(childView);

    mSwipeRefreshLayout.setColorSchemeColors(Utils.getSwipeRefreshColors(mActivity));
    return mSwipeRefreshLayout;
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override public void onRefresh() {
        refresh();
      }
    });
    // 从本地加载缓存，并从服务端获取新鲜数据
    loadLocalData();
    //refresh();
  }

  /**
   * 从本地加载缓存
   */
  protected void loadLocalData() {
    RxDataUtil.loadLocalData(mActivity, getCachedDataObservable(), new Action1<List>() {
      @Override public void call(List list) {
        mData = list;
        notifyDataSetChanged();
      }
    });
  }

  /**
   * 从服务端获取数据
   */
  protected void refresh() {
    Observable<? extends FrameworkModel> observable = getFreshDataObservable();
    if (observable == null) {
      Log.e(TAG, "refresh observable is null");
      mSwipeRefreshLayout.post(new Runnable() {
        @Override public void run() {
          mSwipeRefreshLayout.setRefreshing(false);
        }
      });
      return;
    }
    mPageNo = 1;
    RxDataUtil.refresh(mActivity, observable, new Action1<FrameworkModel>() {
      @Override public void call(FrameworkModel baseModel) {

        List temp = getListData(baseModel);
        if (temp != null) {
          mCurrentSize = temp.size();
        } else {
          mCurrentSize = 0;
          if (mData != null) {
            mData.clear();
          }
        }

        setData(temp);
        notifyDataSetChanged();
        if (getListView() instanceof AbsListView) {
          getListView().post(new Runnable() {
            @Override public void run() {
              ((AbsListView) getListView()).setSelection(0);
            }
          });
        }
      }
    }, mOnComplete, new Action1<Throwable>() {
      @Override public void call(Throwable throwable) {
        //new ErrorDialog(mActivity).showError(throwable);
        if (mSwipeRefreshLayout.isRefreshing()) {
          mSwipeRefreshLayout.setRefreshing(false);
        }
      }
    });
  }

  /**
   * 加载更多
   */
  protected void loadMore() {
    if (isHasLoadingMoreAbility()) {
      mLoading = false;
      mPageNo++;
      RxDataUtil.loadMore(mActivity, getFreshDataObservable(), new Action1<FrameworkModel>() {
        @Override public void call(FrameworkModel baseModel) {
          List temp = getListData(baseModel);
          if (temp != null) {
            mCurrentSize = temp.size();
            addData(temp);
          } else {
            mCurrentSize = 0;
          }
          notifyDataSetChanged();
        }
      }, mOnComplete);
    }
  }

  /**
   * 没有更多数据
   */
  protected void notMore() {
    mLoading = false;
    Utils.showToast(mActivity, getResources().getString(R.string.pull_to_refresh_no_more));
  }

  /**
   * 数据处理完成后，通知刷新完成
   */
  protected Action0 mOnComplete = new Action0() {
    @Override public void call() {
      mSwipeRefreshLayout.post(new Runnable() {
        @Override public void run() {
          mSwipeRefreshLayout.setRefreshing(false);
        }
      });
      mLoading = true;
    }
  };

  /**
   * 通知adapter刷新
   */
  protected void notifyDataSetChanged() {
    View listView = getListView();
    if (mData == null || mData.size() == 0) {
      if (hasEmptyView) {
        if (mEmptyView.getVisibility() == View.GONE) {
          mEmptyView.setVisibility(View.VISIBLE);
        }
        if (listView.getVisibility() == View.GONE) {
          listView.setVisibility(View.GONE);
        }
      } else if (mEmptyView.getVisibility() == View.VISIBLE) {
        mEmptyView.setVisibility(View.GONE);
      }
    } else {
      if (mEmptyView.getVisibility() == View.VISIBLE) {
        mEmptyView.setVisibility(View.GONE);
      }
      if (listView.getVisibility() == View.GONE) {
        listView.setVisibility(View.VISIBLE);
      }
    }
    if (getAdapter() != null) {
      getAdapter().notifyDataSetChanged(mData);
    }
  }

  /**
   * 设置是否有空视图
   *
   * @param has true，有
   */
  protected void hasEmptyView(boolean has) {
    hasEmptyView = has;
  }

  /**
   * 用于获取列表显示的集合。当返回的数据不是纯粹的集合时，子类可继承该方法。
   */
  protected List getListData(FrameworkModel baseModel) {
    return (List) baseModel.getData();
  }

  protected void setData(List data) {
    mData = data;
  }

  protected void addData(List data) {
    mData.addAll(data);
  }

  protected boolean isHasLoadingMoreAbility() {
    return mHasLoadingMoreAbility;
  }

  protected void setHasLoadingMoreAbility(boolean hasLoadingMoreAbility) {
    mHasLoadingMoreAbility = hasLoadingMoreAbility;
  }
}

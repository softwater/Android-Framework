package com.nerd.android.framework.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.nerd.android.framework.adapter.IAdapter;
import com.nerd.framework.R;

/**
 * App 用于展示RecyclerView的Fragment的基类
 * Created by softwater on 2016/1/8.
 */
public abstract class FrameworkRecyclerViewFragment<A extends IAdapter> extends
    FrameworkListFragment<A> {
  protected RecyclerView mRecyclerView;

  /**
   * 设置界面布局
   */
  protected abstract void setLayoutManager();

  /**
   * RecyclerView滚动监听
   *
   * @param recyclerView The RecyclerView which scrolled.
   * @param dx The amount of horizontal scroll.
   * @param dy The amount of vertical scroll.
   */
  protected abstract void onRecyclerViewScrolled(RecyclerView recyclerView, int dx, int dy);

  protected int pastVisiblesItems, visibleItemCount, totalItemCount;

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return super.onCreateView(inflater, container, savedInstanceState, R.layout.view_recycler);
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mRecyclerView = (RecyclerView) view.findViewById(R.id.swiperefresh_recycler_view);
    setLayoutManager();
    mRecyclerView.setAdapter((RecyclerView.Adapter) getAdapter());
    mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        onRecyclerViewScrolled(recyclerView, dx, dy);
      }
    });
  }

  /**
   * 获取adapter
   */
  @SuppressWarnings("unchecked") protected A getAdapter() {
    if (mAdapter == null) {
      mAdapter = setAdapter();
    }
    return mAdapter;
  }

  @Override protected View getListView() {
    return mRecyclerView;
  }
}

package com.nerd.android.framework.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ListAdapter;
import com.nerd.android.framework.adapter.IAdapter;
import com.nerd.android.framework.listener.SwipeListViewOnScrollListener;
import com.nerd.framework.R;

/**
 * App 用于展示GridView的Fragment的基类
 * Created by softwater on 2015/12/22.
 */
public abstract class FrameworkGridViewFragment<A extends IAdapter> extends
    FrameworkListFragment<A> {

  protected GridView mGridView;

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return onCreateView(inflater, container, savedInstanceState, R.layout.view_grid);
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mGridView = (GridView) view.findViewById(R.id.swiperefresh_grid_view);
    mGridView.setAdapter((ListAdapter) getAdapter());
    mGridView.setOnScrollListener(
        new SwipeListViewOnScrollListener(mSwipeRefreshLayout, new AbsListView.OnScrollListener() {
          @Override public void onScrollStateChanged(AbsListView view, int scrollState) {

          }

          @Override
          public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount,
              int totalItemCount) {
            if (mLoading) {
              final int lastItem = firstVisibleItem + visibleItemCount;
              if (lastItem == totalItemCount) {
                if (mCurrentSize == mPageSize) {
                  loadMore();
                } else {
                  notMore();
                }
              }
            }
          }
        }));
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
    return mGridView;
  }
}

package com.nerd.android.framework.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import com.nerd.android.framework.adapter.IAdapter;
import com.nerd.android.framework.listener.SwipeListViewOnScrollListener;
import com.nerd.framework.R;

/**
 * App 用于展示ListView的Fragment的基类<br/>
 * 必须自己使用{@link FrameworkListViewFragment#mListView}设置Adapter
 * Created by softwater on 15/12/9.
 */
public abstract class FrameworkListViewFragment<A extends IAdapter>
    extends FrameworkListFragment<A> {

  protected ListView mListView;

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return super.onCreateView(inflater, container, savedInstanceState, R.layout.view_list);
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    mListView = (ListView) view.findViewById(R.id.swiperefresh_list_view);
    mListView.setOnScrollListener(
        new SwipeListViewOnScrollListener(mSwipeRefreshLayout, new AbsListView.OnScrollListener() {
          @Override public void onScrollStateChanged(AbsListView view, int scrollState) {
          }

          @Override
          public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
              int totalItemCount) {
            if (mLoading) {
              final int lastItem = firstVisibleItem + visibleItemCount;
              if (lastItem == totalItemCount) {
                //Do pagination.. i.e. fetch new data
                if (mCurrentSize == mPageSize) {
                  loadMore();
                } else {
                  notMore();
                }
              }
            }
          }
        }));
    super.onViewCreated(view, savedInstanceState);
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

  @Override public ListView getListView() {
    return mListView;
  }
}

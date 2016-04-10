package com.nerd.android.framework.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import com.nerd.android.framework.adapter.IAdapter;

/**
 * RecyclerView StaggeredView形式展示
 * Created by softwater on 2016/1/13.
 */
public abstract class FrameworkRecyclerStaggeredGridViewFragment<A extends IAdapter>
    extends FrameworkRecyclerViewFragment<A> {
  protected StaggeredGridLayoutManager mLayoutManager;
  int[] firstVisibleItems = null;

  /**
   * If orientation is vertical, spanCount is number of columns. If
   * orientation is horizontal, spanCount is number of rows.
   */
  protected abstract int getSpanCount();

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mLayoutManager =
        new StaggeredGridLayoutManager(getSpanCount(), StaggeredGridLayoutManager.VERTICAL);
  }

  @Override protected void setLayoutManager() {
    mRecyclerView.setLayoutManager(mLayoutManager);
  }

  @Override protected void onRecyclerViewScrolled(RecyclerView recyclerView, int dx, int dy) {
    if (dy > 0) //check for scroll down
    {
      if (mSwipeRefreshLayout.isEnabled()) {
        mSwipeRefreshLayout.setEnabled(false);
      }
      visibleItemCount = mLayoutManager.getChildCount();
      totalItemCount = mLayoutManager.getItemCount();

      firstVisibleItems = mLayoutManager.findFirstVisibleItemPositions(firstVisibleItems);
      if (firstVisibleItems != null && firstVisibleItems.length > 0) {
        pastVisiblesItems = firstVisibleItems[0];
      }

      if (mLoading) {
        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
          if (mCurrentSize == mPageSize) {
            loadMore();
          } else {
            notMore();
          }
        }
      }
    } else {
      int[] arr = mLayoutManager.findFirstCompletelyVisibleItemPositions(null);
      for (int a : arr) {
        if (a == 0) {
          mSwipeRefreshLayout.setEnabled(true);
          break;
        }
      }
    }
  }
}

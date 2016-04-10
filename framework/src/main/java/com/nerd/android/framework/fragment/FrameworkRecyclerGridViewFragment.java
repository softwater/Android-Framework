package com.nerd.android.framework.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.nerd.android.framework.adapter.IAdapter;

/**
 * RecyclerView GridView形式展示
 * Created by softwater on 2016/1/13.
 */
public abstract class FrameworkRecyclerGridViewFragment<A extends IAdapter>
    extends FrameworkRecyclerViewFragment<A> {
  protected GridLayoutManager mLayoutManager;

  /**
   * The number of columns in the grid
   */
  protected abstract int getSpanCount();

  @Override protected void setLayoutManager() {
    mLayoutManager = new GridLayoutManager(mActivity, getSpanCount());
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
      pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

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
      if (mLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
        mSwipeRefreshLayout.setEnabled(true);
      }
    }
  }
}

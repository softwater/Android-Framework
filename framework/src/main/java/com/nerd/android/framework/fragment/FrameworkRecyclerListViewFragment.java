package com.nerd.android.framework.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.nerd.android.framework.adapter.IAdapter;

/**
 * RecyclerView ListView形式展示
 * Created by softwater on 2016/1/13.
 */
public abstract class FrameworkRecyclerListViewFragment<A extends IAdapter>
    extends FrameworkRecyclerViewFragment<A> {
  protected LinearLayoutManager mLayoutManager;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override protected void setLayoutManager() {
    mLayoutManager = new LinearLayoutManager(mActivity);
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

package com.nerd.android.framework.fragment;

import android.app.Activity;

/**
 * App Fragment基类
 * Created by softwater on 15/12/1.
 */
public class FrameworkFragment extends android.support.v4.app.Fragment {

  protected Activity mActivity;

  @Override public void onAttach(Activity activity) {
    super.onAttach(activity);
    mActivity = activity;
  }
}

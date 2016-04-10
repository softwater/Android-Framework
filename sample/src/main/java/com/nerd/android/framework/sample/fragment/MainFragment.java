package com.nerd.android.framework.sample.fragment;

import com.nerd.android.framework.adapter.IAdapter;
import com.nerd.android.framework.fragment.FrameworkRecyclerListViewFragment;
import com.nerd.android.framework.model.FrameworkModel;
import java.util.List;
import rx.Observable;

/**
 * Created by softwater on 16/4/10.
 * Modified by softwater on 16/4/10.
 */
public class MainFragment extends FrameworkRecyclerListViewFragment {
  @Override protected Observable<? extends List> getCachedDataObservable() {
    return null;
  }

  @Override protected Observable<? extends FrameworkModel> getFreshDataObservable() {
    return null;
  }

  @Override protected IAdapter setAdapter() {
    return null;
  }
}

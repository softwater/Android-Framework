package com.nerd.android.framework.adapter;

import java.util.List;

/**
 * Created by softwater on 16/2/29.
 * Modified by softwater on 16/2/29.
 */
public abstract class FrameworkExpandableListAdapter<T extends List>
    extends android.widget.BaseExpandableListAdapter implements IAdapter<T> {

  protected T mData;

  @Override public int getGroupCount() {
    return mData == null ? 0 : mData.size();
  }

  public void notifyDataSetChanged(T data) {
    mData = data;
    notifyDataSetChanged();
  }
}

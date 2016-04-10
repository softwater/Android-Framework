package com.nerd.android.framework.adapter;

import java.util.List;

/**
 * Created by softwater on 15/12/9.
 * Modified by softwater on 15/12/9.
 */
public abstract class FrameworkAdapter<T extends List> extends android.widget.BaseAdapter
    implements IAdapter<T> {

  protected T mData;

  @Override public int getCount() {
    return mData == null ? 0 : mData.size();
  }

  @Override public Object getItem(int position) {
    return mData.get(position);
  }

  @Override public long getItemId(int position) {
    return position;
  }

  public void notifyDataSetChanged(T data) {
    mData = data;
    notifyDataSetChanged();
  }

  public void setData(T data) {
    mData = data;
  }
}

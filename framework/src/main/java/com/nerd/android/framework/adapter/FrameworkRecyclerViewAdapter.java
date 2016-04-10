package com.nerd.android.framework.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import java.util.List;

/**
 * Created by softwater on 2016/1/8.
 */
public abstract class FrameworkRecyclerViewAdapter<T extends List, VH extends RecyclerView.ViewHolder>
    extends RecyclerView.Adapter<VH> implements IAdapter<T> {
  protected T mData;
  protected Context mContext;

  public FrameworkRecyclerViewAdapter(Context context) {
    mContext = context;
  }

  @Override public int getItemCount() {
    return mData == null ? 0 : mData.size();
  }

  public void notifyDataSetChanged(T data) {
    mData = data;
    notifyDataSetChanged();
  }
}
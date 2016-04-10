package com.nerd.android.framework.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;
import com.nerd.framework.R;

/**
 * 空视图
 * Created by softwater on 2015/12/22.
 */
public class EmptyView extends TextView {

  public EmptyView(Context context, AttributeSet attrs) {
    super(context, attrs);
    setText(getResources().getString(R.string.no_data));
    setTextColor(getResources().getColor(R.color.color_50000000));
    setGravity(Gravity.CENTER);
  }
}

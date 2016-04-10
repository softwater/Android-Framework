package com.nerd.android.framework.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.nerd.framework.R;

/**
 * App Activity基类
 * Created by softwater on 15/12/1.
 * Modified by softwater on 15/12/1.
 */
public class FrameworkActivity extends FragmentActivity {
  /**
   * 等待界面
   */
  private ProgressDialog mLoadingDialog;

  /**
   * 根View
   */
  private LinearLayout mRootView;
  /**
   * app bar
   */
  private RelativeLayout mAppBarLayout;
  /**
   * 返回按钮
   */
  private ImageView mAppBarBack;
  /**
   * 右上角按钮
   */
  private TextView mAppBarOk;
  /**
   * 是否有app bar
   */
  private boolean mHasAppBar = true;

  /**
   * 右上角按钮
   */
  protected Activity mActivity;
  /**
   * 当前页标题
   */
  protected TextView mAppBarTitle;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mActivity = this;
    if (isHasAppBar()) {
      super.setContentView(R.layout.activity_base);
      initView();
      initListener();
    }
    mLoadingDialog = new ProgressDialog(this);
    mLoadingDialog.setCancelable(false);
  }

  @Override public void setContentView(int layoutResID) {
    if (isHasAppBar()) {
      setContentView(View.inflate(this, layoutResID, null));
    } else {
      super.setContentView(layoutResID);
    }
  }

  @Override public void setContentView(View view) {
    if (isHasAppBar()) {
      mRootView.addView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
          LinearLayout.LayoutParams.MATCH_PARENT));
      super.setContentView(mRootView);
    } else {
      super.setContentView(view);
    }
  }

  /**
   * 获取view
   */
  private void initView() {
    mRootView = (LinearLayout) findViewById(R.id.root_view);
    mAppBarLayout = (RelativeLayout) findViewById(R.id.app_bar_layout);
    mAppBarBack = (ImageView) findViewById(R.id.app_bar_back);
    mAppBarTitle = (TextView) findViewById(R.id.app_bar_title);
    mAppBarOk = (TextView) findViewById(R.id.app_bar_ok);
  }

  /**
   * 事件监听
   */
  private void initListener() {
    mRootView.setOnClickListener(mOnClickListener);
    mAppBarLayout.setOnClickListener(mOnClickListener);
    mAppBarBack.setOnClickListener(mOnClickListener);
    mAppBarTitle.setOnClickListener(mOnClickListener);
    mAppBarOk.setOnClickListener(mOnClickListener);
  }

  private View.OnClickListener mOnClickListener = new View.OnClickListener() {
    @Override public void onClick(View v) {
      if (v.getId() == R.id.app_bar_back) {
        onBackClick(v);
      } else if (v.getId() == R.id.app_bar_ok) {
        onOkClick(v);
      }
    }
  };

  /**
   * app bar 左上角返回按钮点击事件
   */
  protected void onBackClick(View v) {
    onBackPressed();
  }

  @Override public void onBackPressed() {
    super.onBackPressed();
  }

  /**
   * app bar 右上角点击事件
   */
  protected void onOkClick(View v) {
  }

  /**
   * 设置当前页标题
   */
  protected void setTitle(String text) {
    showTitle();
    mAppBarTitle.setText(text);
  }

  @Override public void setTitle(CharSequence title) {
    setTitle(title.toString());
  }

  @Override public void setTitle(int titleId) {
    setTitle(getText(titleId));
  }

  /**
   * 设置title视图
   */
  protected void setTitle(View view) {
    showTitle();
    mAppBarLayout.removeView(mAppBarTitle);
    mAppBarLayout.addView(view, mAppBarTitle.getLayoutParams());
  }

  /**
   * 设置右上角按钮文字
   */
  protected void setOk(String text) {
    showOk();
    mAppBarOk.setText(text);
  }

  /**
   * 设置右上角视图
   */
  protected void setOk(View view) {
    showOk();
    mAppBarLayout.removeView(mAppBarOk);
    mAppBarLayout.addView(view, mAppBarOk.getLayoutParams());
  }

  /**
   * 隐藏App Bar
   */
  protected void hideAppBar() {
    if (mAppBarLayout.getVisibility() == View.VISIBLE) {
      mHasAppBar = false;
      mAppBarLayout.setVisibility(View.GONE);
    }
  }

  /**
   * 显示App Bar
   */
  protected void showAppBar() {
    if (mAppBarLayout.getVisibility() == View.GONE) {
      mHasAppBar = true;
      mAppBarLayout.setVisibility(View.VISIBLE);
    }
  }

  /**
   * 隐藏App Bar右上角按钮
   */
  protected void hideOk() {
    if (mAppBarOk.getVisibility() == View.VISIBLE) {
      mAppBarOk.setVisibility(View.GONE);
    }
  }

  /**
   * 隐藏App Bar右上角按钮
   */
  protected void showOk() {
    if (mAppBarOk.getVisibility() == View.GONE) {
      mAppBarOk.setVisibility(View.VISIBLE);
    }
  }

  /**
   * 隐藏返回按钮
   */
  protected void hideBack() {
    if (mAppBarBack.getVisibility() == View.VISIBLE) {
      mAppBarBack.setVisibility(View.GONE);
    }
  }

  /**
   * 显示返回按钮
   */
  protected void showBack() {
    if (mAppBarBack.getVisibility() == View.GONE) {
      mAppBarOk.setVisibility(View.VISIBLE);
    }
  }

  /**
   * 显示title
   */
  protected void showTitle() {
    if (mAppBarTitle.getVisibility() == View.GONE) {
      mAppBarTitle.setVisibility(View.VISIBLE);
    }
  }

  /**
   * 隐藏title
   */
  protected void hideTitle() {
    if (mAppBarTitle.getVisibility() == View.VISIBLE) {
      mAppBarTitle.setVisibility(View.GONE);
    }
  }

  /**
   * 是否有app bar
   */
  public boolean isHasAppBar() {
    return mHasAppBar;
  }

  /**
   * 设置是否显示app bar，在调用{@link FrameworkActivity#onCreate(Bundle)}之前调用该方法。
   */
  public void setHasAppBar(boolean hasAppBar) {
    mHasAppBar = hasAppBar;
  }

  /**
   * 显示等待界面
   */
  protected void showLoadingDialog() {
    if (mLoadingDialog != null) {
      mLoadingDialog.show();
    }
  }

  /**
   * 关闭等待界面
   */
  protected void dismissLoadingDialog() {
    if (mLoadingDialog != null) {
      mLoadingDialog.dismiss();
    }
  }

  @Override protected void onDestroy() {
    dismissLoadingDialog();
    super.onDestroy();
  }
}

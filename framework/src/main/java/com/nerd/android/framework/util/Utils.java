package com.nerd.android.framework.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import com.nerd.framework.R;
import java.util.List;

/**
 * 用于操作的工具类
 * Created by softwater on 16/2/29.
 * Modified by softwater on 16/2/29.
 */
public class Utils {

  /**
   * 判断点击区域是否在view里面
   */
  public static boolean isEventInView(MotionEvent ev, View view) {
    int[] location = new int[2];
    view.getLocationInWindow(location);
    if (ev.getX() > location[0]
        && ev.getX() < location[0] + view.getWidth()
        && ev.getY() > location[1]
        && ev.getY() < location[1] + view.getHeight()) {

      return true;
    }
    return false;
  }

  public static void showToast(Context context, int contentid) {
    showToast(context, context.getString(contentid), false);
  }

  public static void showToast(Context context, String content) {
    showToast(context, content, false);
  }

  public static void showToast(Context context, int contentid, boolean longTime) {
    showToast(context, context.getString(contentid), false);
  }

  public static void showToastOnMainThread(Context context, String msg) {
    Looper.prepare();
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    Looper.loop();
  }

  public static void showToast(Context context, String content, boolean longTime) {
    int time = longTime ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
    Toast.makeText(context, content, time).show();
  }

  /**
   * 检测某ActivityUpdate是否在当前Task的栈顶
   */
  public static boolean isTopActivy(Activity activity) {
    ActivityManager manager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
    List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
    String cmpNameTemp = null;

    if (null != runningTaskInfos) {
      cmpNameTemp = (runningTaskInfos.get(0).topActivity).toString();
    }
    return null != cmpNameTemp && cmpNameTemp.equals(activity.getComponentName().toString());
  }

  /**
   * 获取刷新时的
   */
  public static int[] getSwipeRefreshColors(Context context) {
    return new int[] {
        context.getResources().getColor(R.color.swipe_refresh_color_1),
        context.getResources().getColor(R.color.swipe_refresh_color_2),
        context.getResources().getColor(R.color.swipe_refresh_color_3),
        context.getResources().getColor(R.color.swipe_refresh_color_4)
    };
  }
}

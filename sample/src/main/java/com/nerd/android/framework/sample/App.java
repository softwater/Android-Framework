package com.nerd.android.framework.sample;

import android.app.Application;
import com.nerd.android.framework.sample.di.componet.ApplicationComponent;
import com.nerd.android.framework.sample.di.componet.DaggerApplicationComponent;

/**
 * Created by softwater on 16/4/10.
 * Modified by softwater on 16/4/10.
 */
public class App extends Application {
  private ApplicationComponent mApplicationComponent;

  @Override public void onCreate() {
    super.onCreate();
    mApplicationComponent = DaggerApplicationComponent.create();
    mApplicationComponent.inject(this);
  }

  public ApplicationComponent getApplicationComponent() {
    return mApplicationComponent;
  }
}

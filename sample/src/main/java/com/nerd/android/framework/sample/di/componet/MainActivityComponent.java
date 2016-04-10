package com.nerd.android.framework.sample.di.componet;

import com.nerd.android.framework.sample.activity.MainActivity;
import com.nerd.android.framework.sample.di.module.MainActivityModule;
import dagger.Component;

/**
 * Created by softwater on 16/4/10.
 * Modified by softwater on 16/4/10.
 */
@Component(modules = MainActivityModule.class)
public interface MainActivityComponent {
  void inject(MainActivity activity);
}

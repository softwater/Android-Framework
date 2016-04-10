package com.nerd.android.framework.sample.di.componet;

import com.nerd.android.framework.sample.App;
import com.nerd.android.framework.sample.di.module.ApplicationModule;
import dagger.Component;
import javax.inject.Singleton;

/**
 * Created by softwater on 16/4/10.
 * Modified by softwater on 16/4/10.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
  void inject(App app);
}

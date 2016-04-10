package com.nerd.android.framework.sample.di.module;

import com.nerd.android.framework.sample.fragment.MainFragment;
import dagger.Module;
import dagger.Provides;

/**
 * Created by softwater on 16/4/10.
 * Modified by softwater on 16/4/10.
 */
@Module
public class MainActivityModule {

  @Provides MainFragment provideMainFragment() {
    return new MainFragment();
  }

}

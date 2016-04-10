package com.nerd.android.framework.sample.activity;

import android.os.Bundle;
import com.nerd.android.framework.activity.FrameworkActivity;
import com.nerd.android.framework.sample.R;
import com.nerd.android.framework.sample.di.componet.DaggerMainActivityComponent;
import com.nerd.android.framework.sample.fragment.MainFragment;
import javax.inject.Inject;

/**
 * Created by softwater on 16/3/31.
 * Modified by softwater on 16/3/31.
 */
public class MainActivity extends FrameworkActivity {

  @Inject MainFragment mMainFragment;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.content_main);

    DaggerMainActivityComponent.create().inject(this);

    getSupportFragmentManager().beginTransaction()
        .add(R.id.fragment_container, mMainFragment)
        .commit();
  }
}

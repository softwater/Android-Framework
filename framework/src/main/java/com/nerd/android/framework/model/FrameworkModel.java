package com.nerd.android.framework.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by softwater on 15/12/1.
 * Modified by softwater on 15/12/1.
 */
public abstract class FrameworkModel<T> {
  @SerializedName("m_istatus") private Integer mStatus;
  @SerializedName("m_strMessage") private String mMessage;

  public abstract T getData();

  public boolean isSuccess() {
    return mStatus != 0;
  }

  public Integer getStatus() {
    return mStatus;
  }

  public String getMessage() {
    return mMessage;
  }
}

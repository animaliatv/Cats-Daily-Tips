// Copyright 2014 Google Inc. All Rights Reserved.

package com.animalia.hassan.catsdailytips.youTube;

import com.animalia.hassan.catsdailytips.BuildConfig;
import com.animalia.hassan.catsdailytips.R;

import static android.provider.Settings.Global.getString;

/**
 * Static container class for holding a reference to your YouTube Developer Key.
 */
public class DeveloperKey {

  /**
   * Please replace this with a valid API key which is enabled for the
   * YouTube Data API v3 service. Go to the
   * <a href="https://console.developers.google.com/">Google Developers Console</a>
   * to register a new developer key.
   */
  public static final String DEVELOPER_KEY = BuildConfig.DEVELOPER_KEY;

 /* public static final String DEVELOPER_KEY1 = String.valueOf(R.string.DEVELOPER_KEY);*/

}

package com.avario.core.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by orly on 1/18/18.
 */

public class Auth {

  @SerializedName("ha_version")
  private String haVersion;

  @SerializedName("type")
  private String type;

  public String getHaVersion() {
    return haVersion;
  }

  public String getType() {
    return type;
  }
}

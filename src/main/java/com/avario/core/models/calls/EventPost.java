package com.avario.core.models.calls;

import com.google.gson.annotations.SerializedName;

/**
 * Created by orly on 2/1/18.
 */

public class EventPost {

  @SerializedName("type")
  private String type;

  @SerializedName("domain")
  private String domain;

  @SerializedName("service")
  private String service;



}

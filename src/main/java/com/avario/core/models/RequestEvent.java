package com.avario.core.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by orly on 1/18/18.
 */

public class RequestEvent {

  @SerializedName("id")
  private int id;

  @SerializedName("type")
  private String type;

  @SerializedName("event_type")
  private String eventType;

  @SerializedName("state_changed")
  private String stateChanged;

  public void setId(int id) {
    this.id = id;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setEventType(String eventType) {
    this.eventType = eventType;
  }

  public void setStateChanged(String stateChanged) {
    this.stateChanged = stateChanged;
  }
}

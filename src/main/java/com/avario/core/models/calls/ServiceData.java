package com.avario.core.models.calls;

import com.google.gson.annotations.SerializedName;

/**
 * Created by orly on 2/1/18.
 */

public class ServiceData {

  @SerializedName("entity_id")
  private String entityId;

  public void setEntityId(String entityId) {
    this.entityId = entityId;
  }
}

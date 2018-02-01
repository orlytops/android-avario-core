package com.avario.core.models.calls;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Created by orly on 2/1/18.
 */

public class ServicePost {

  @SerializedName("id")
  private int id;

  @SerializedName("type")
  private String type;

  @SerializedName("domain")
  private String domain;

  @SerializedName("service")
  private String service;

  @SerializedName("service_data")
  private ServiceData serviceData;

  private String serviceDataJson;


  public void setId(int id) {
    this.id = id;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

  public void setService(String service) {
    this.service = service;
  }

  public void setServiceData(ServiceData serviceData) {
    this.serviceData = serviceData;
  }

  public String toJson() {
    Gson gson = new Gson();
   /* try {
      serviceDataJson = gson.toJson(serviceData);
    } catch (Exception e) {
      Timber.e("Error preparing jsonData");
    }*/

    return gson.toJson(this);
  }
}


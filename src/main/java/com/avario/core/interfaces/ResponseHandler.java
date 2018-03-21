package com.avario.core.interfaces;

import org.json.JSONObject;

/**
 * Created by orly on 2/2/18.
 */

public class ResponseHandler {

  private String id;
  private Response response;
  private Runnable runnable;

  public interface Response {
    void onResponse(JSONObject jsonObject);

    void onError();
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setResponse(Response response) {
    this.response = response;
  }

  public String getId() {
    return id;
  }

  public Response getResponse() {
    return response;
  }

  public void setRunnable(Runnable runnable) {
    this.runnable = runnable;
  }

  public Runnable getRunnable() {
    return runnable;
  }
}

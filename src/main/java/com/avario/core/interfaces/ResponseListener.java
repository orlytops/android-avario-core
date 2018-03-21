package com.avario.core.interfaces;

import org.json.JSONObject;

/**
 * Created by orly on 1/18/18.
 */

public interface ResponseListener {

  void onResponse(JSONObject jsonObject);

  void onExpire(String entityId);

}
